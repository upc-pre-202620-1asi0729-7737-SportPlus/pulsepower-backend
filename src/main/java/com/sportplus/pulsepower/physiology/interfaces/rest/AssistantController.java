package com.sportplus.pulsepower.physiology.interfaces.rest;

import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
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
    public AssistantController(CurrentUserProvider user, SleepQueryService sleep,
                               TrainingQueryService training, WellnessQueryService wellness) {
        this.user=user; this.sleep=sleep; this.training=training; this.wellness=wellness;
    }
    public record Question(@NotBlank @Size(max=1000) String message) {}
    public record Answer(String answer, String source, String from, String to) {}
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
        lines.add("Los días sin registros no se cuentan como cero. Los datos del simulador son simulados.");
        return new Answer(String.join(" ",lines),"authenticated-user-records",from.toString(),to.toString());
    }
}
