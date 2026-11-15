package com.sportplus.pulsepower.physiology.interfaces.rest;

import com.sportplus.pulsepower.physiology.infrastructure.ai.GeminiChatClient;
import com.sportplus.pulsepower.physiology.infrastructure.ai.GeminiUnavailableException;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.model.commands.AddPlannedActivityCommand;
import com.sportplus.pulsepower.training.domain.model.commands.SetPlanPeriodCommand;
import com.sportplus.pulsepower.training.domain.services.TrainingCommandService;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.model.valueobjects.ActivityKind;
import com.sportplus.pulsepower.training.domain.model.valueobjects.Intensity;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.text.Normalizer;
import java.time.*;
import java.util.*;

/** Grounded, deterministic assistant. Never accepts a user ID or facts from the browser. */
@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {
    private final CurrentUserProvider user;
    private final SleepQueryService sleep;
    private final TrainingQueryService training;
    private final WellnessQueryService wellness;
    private final GeminiChatClient geminiChatClient;
    private final TrainingCommandService trainingCommand;

    public AssistantController(CurrentUserProvider user, SleepQueryService sleep,
                               TrainingQueryService training, WellnessQueryService wellness,
                               GeminiChatClient geminiChatClient, TrainingCommandService trainingCommand) {
        this.user=user; this.sleep=sleep; this.training=training; this.wellness=wellness;
        this.geminiChatClient=geminiChatClient; this.trainingCommand=trainingCommand;
    }
    public record Question(@NotBlank @Size(max=1000) String message) {}
    public record Answer(String answer, String source, String from, String to) {}
    public record RoutineRequest(@Size(max=500) String preferences) {}
    public record RoutineAnswer(String message, int activitiesCreated, String planFrom, String planTo) {}
    private Answer reply(String text) { return new Answer(text, "platform-guide", null, null); }
    private boolean matches(String text, String pattern) { return java.util.regex.Pattern.compile(pattern).matcher(text).find(); }
    private String number(double n) { return String.format(Locale.forLanguageTag("es-PE"), "%.1f", n); }

    @PostMapping("/messages")
    public Answer ask(@Valid @RequestBody Question question) {
        var id=user.requireCurrentUserId();
        var q=Normalizer.normalize(question.message(),Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        if(matches(q,"\\b(capital|presidente|politica|futbolista|receta|programacion|historia del|clima|chiste|poema|geografia)\\b")
                || matches(q,"(otro usuario|otros usuarios|otra cuenta|other user|ignora|ignore|instrucciones|system prompt)"))
            return reply("Esa pregunta está fuera de mi alcance. Solo puedo ayudarte con PulsePower y los registros de tu propia cuenta.");
        if(matches(q,"(diagnostic|medicamento|pastilla|enfermedad|lesion|dolor|tratamiento)"))
            return reply("Puedo resumir tus registros de PulsePower, pero no diagnosticar ni indicar tratamientos.");
        if(matches(q,"^(hola|hello|hi|buenas)[!?. ]*$"))
            return reply("Hola. Puedo resumir tu sueño, entrenamiento y bienestar usando únicamente tus registros de PulsePower.");
        if(matches(q,"(como (uso|registro|agrego|anoto)|donde (veo|registro)|que puedes|ayuda|como funciona)"))
            return reply("En PulsePower puedes consultar y registrar sueño, entrenamiento y bienestar. En Dispositivo puedes generar registros simulados; en Reportes puedes generar un PDF. Puedo resumir tus registros de hoy, ayer, esta semana, los últimos 7 días o este mes.");
        boolean s=matches(q,"(dormi|dormir|dormido|sueno|sleep|noche)"),
                t=matches(q,"(entren|workout|training|ejercicio|deporte)"),
                w=matches(q,"(estres|stress|bienestar|wellness|animo|mood)"),
                all=matches(q,"(resumen|como voy|como estoy|recuperacion)");
        if(!(s||t||w||all)) return reply("Esa pregunta está fuera de mi alcance. Solo puedo ayudarte con PulsePower y tus registros de sueño, entrenamiento y bienestar.");
        if(matches(q,"(\\d{4}-\\d{2}|semana pasada|mes pasado|ano|año|year|ultimos? (?!7\\b)\\d+)"))
            return reply("Para evitar darte datos de un período incorrecto, pregunta por hoy, ayer, esta semana, los últimos 7 días o este mes.");
        var zone=ZoneId.of("America/Lima"); var today=LocalDate.now(zone); var to=today;
        var from=today.minusDays(6);
        if(q.contains("hoy")) from=today;
        else if(q.contains("ayer")) {from=today.minusDays(1);to=from;}
        else if(q.contains("esta semana")) from=today.with(DayOfWeek.MONDAY);
        else if(q.contains("este mes")) from=today.withDayOfMonth(1);
        final var start=from; final var end=to;
        var lines=new ArrayList<String>();
        lines.add("Según tus registros del "+from+" al "+to+":");
        if(s||all) {
            var rows=sleep.handle(new GetSleepRecordsByUserIdQuery(id)).stream().filter(r->{var d=r.getEndedAt().atZone(zone).toLocalDate();return !d.isBefore(start)&&!d.isAfter(end);}).toList();
            if(rows.isEmpty()) lines.add("No tienes registros de sueño suficientes para responder en ese período.");
            else {double total=rows.stream().mapToDouble(r->r.durationHours()).sum();long days=rows.stream().map(r->r.getEndedAt().atZone(zone).toLocalDate()).distinct().count();
                lines.add("Sueño: "+rows.size()+" registros en "+days+" días; "+number(total)+" horas registradas en total y "+number(total/days)+" horas por día con datos. No puedo deducir la calidad del sueño únicamente de su duración.");}
        }
        if(t||all) {
            var rows=training.handle(new GetSessionsByUserIdQuery(id)).stream().filter(r->!r.getDate().isBefore(start)&&!r.getDate().isAfter(end)).toList();
            lines.add(rows.isEmpty()?"No tienes entrenamientos registrados en ese período.":"Entrenamiento: "+rows.size()+" sesiones; "+rows.stream().mapToInt(r->r.getDurationMinutes()).sum()+" minutos registrados; esfuerzo percibido medio "+number(rows.stream().mapToInt(r->r.getPerceivedEffort()).average().orElseThrow())+"/10.");
        }
        if(w||all) {
            var rows=wellness.handle(new GetCheckInsByUserIdQuery(id)).stream().filter(r->!r.getDate().isBefore(start)&&!r.getDate().isAfter(end)).toList();
            lines.add(rows.isEmpty()?"No tienes registros de bienestar en ese período.":"Bienestar: "+rows.size()+" registros; estrés medio "+number(rows.stream().mapToInt(r->r.getStress()).average().orElseThrow())+"/10; ánimo medio "+number(rows.stream().mapToInt(r->r.getMood()).average().orElseThrow())+"/5.");
        }
        var snapshot=String.join(" ",lines);
        var systemPrompt="""
                Contexto: PulsePower es una app académica de seguimiento de salud deportiva. \
                Los datos pueden ser pocos o simulados — eso es normal y esperado. \
                Eres el asistente de rendimiento y bienestar de PulsePower. \
                Tu única función es interpretar los datos disponibles y dar recomendaciones útiles. \
                REGLA PRINCIPAL: Aunque los datos sean pocos, SIEMPRE das una respuesta útil. \
                Nunca dices "no tengo suficientes datos" ni "no puedo recomendar". \
                Si hay pocos datos, usa los que hay y complementa con estándares generales de salud deportiva. \
                Formato OBLIGATORIO de respuesta:
                1. Una frase interpretando lo que muestran los datos (ej: "Dormiste bien esta semana" o "Tu carga de entrenamiento fue alta").
                2. Comparación con el rango saludable de referencia (ej: "Lo recomendado es 7-9h de sueño para adultos activos").
                3. Dos sugerencias concretas con frases como "te recomendaría", "sería ideal que", "podrías intentar".
                4. Cierra con: "Estas son orientaciones generales, no reemplazan la opinión de un profesional."
                Restricciones: no prescribas medicamentos ni diagnostiques. Responde en español, tono directo y motivador.
                Datos del usuario:
                """+snapshot;
        try {
            var aiText=geminiChatClient.ask(systemPrompt,question.message());
            return new Answer(aiText,"gemini-2.0-flash",from.toString(),to.toString());
        } catch (GeminiUnavailableException e) {
            return new Answer(snapshot,"fallback-local",from.toString(),to.toString());
        }
    }

    @PostMapping("/routines")
    public RoutineAnswer generateRoutine(@Valid @RequestBody RoutineRequest request) {
        var id=user.requireCurrentUserId();
        var zone=ZoneId.of("America/Lima"); var today=LocalDate.now(zone);
        var planFrom=today.minusDays(7); var planTo=today.plusDays(90);

        var sleepRows=sleep.handle(new GetSleepRecordsByUserIdQuery(id)).stream()
            .filter(r->!r.getEndedAt().atZone(zone).toLocalDate().isBefore(today.minusDays(6))).toList();
        var trainRows=training.handle(new GetSessionsByUserIdQuery(id)).stream()
            .filter(r->!r.getDate().isBefore(today.minusDays(6))).toList();
        var wellRows=wellness.handle(new GetCheckInsByUserIdQuery(id)).stream()
            .filter(r->!r.getDate().isBefore(today.minusDays(6))).toList();

        var context=new StringBuilder();
        if(!sleepRows.isEmpty()) {
            double avg=sleepRows.stream().mapToDouble(r->r.durationHours()).average().orElse(0);
            context.append(String.format("Sueño últimos 7 días: promedio %.1fh/noche (%d registros). ", avg, sleepRows.size()));
        }
        if(!trainRows.isEmpty()) {
            double avgEff=trainRows.stream().mapToInt(r->r.getPerceivedEffort()).average().orElse(0);
            context.append(String.format("Entrenamiento: %d sesiones, esfuerzo medio %.1f/10. ", trainRows.size(), avgEff));
        }
        if(!wellRows.isEmpty()) {
            double avgStress=wellRows.stream().mapToInt(r->r.getStress()).average().orElse(0);
            double avgMood=wellRows.stream().mapToInt(r->r.getMood()).average().orElse(0);
            context.append(String.format("Bienestar: estrés medio %.1f/10, ánimo %.1f/5. ", avgStress, avgMood));
        }

        var pref=request.preferences()!=null&&!request.preferences().isBlank()
            ? "Preferencias del usuario: "+request.preferences()+". " : "";
        var systemPrompt="""
            Eres un entrenador personal experto. Basándote en los datos del usuario y sus preferencias, genera una rutina de entrenamiento para los próximos 7 días.
            Responde ÚNICAMENTE con un objeto JSON válido con esta estructura exacta, sin texto adicional:
            {"planName":"string","summary":"string con resumen motivador en español de 2-3 oraciones","activities":[{"dayOffset":1,"activity":"string","durationMinutes":number,"intensity":"LOW|MEDIUM|HIGH","kind":"WORKOUT|ACTIVE_REST","startTime":"HH:mm"}]}
            Reglas:
            - dayOffset va del 1 al 7 (días desde hoy).
            - Incluye entre 4 y 6 actividades. Alterna intensidades. Incluye al menos 1 día de descanso activo.
            - intensity debe ser exactamente LOW, MEDIUM o HIGH.
            - kind debe ser exactamente WORKOUT o ACTIVE_REST.
            - No diagnostiques ni prescribas medicamentos.
            Datos del usuario: """+context+pref;

        try {
            var dto=geminiChatClient.generateRoutine(systemPrompt);
            trainingCommand.handle(new SetPlanPeriodCommand(id, planFrom, planTo));
            int created=0;
            for(var act:dto.activities()) {
                try {
                    var date=today.plusDays(act.dayOffset());
                    var startTime=act.startTime()!=null&&!act.startTime().isBlank()
                        ? java.time.LocalTime.parse(act.startTime()) : null;
                    trainingCommand.handle(new AddPlannedActivityCommand(id, date, startTime, act.activity(),
                        act.durationMinutes(), Intensity.valueOf(act.intensity().toUpperCase()),
                        ActivityKind.valueOf(act.kind().toUpperCase())));
                    created++;
                } catch(Exception ignored) {}
            }
            return new RoutineAnswer(dto.summary(), created, today.plusDays(1).toString(), today.plusDays(7).toString());
        } catch(GeminiUnavailableException e) {
            return new RoutineAnswer("No fue posible generar la rutina en este momento. Intenta nuevamente en unos minutos.", 0, null, null);
        }
    }
}
