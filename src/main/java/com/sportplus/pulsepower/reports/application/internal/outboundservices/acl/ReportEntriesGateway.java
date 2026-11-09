package com.sportplus.pulsepower.reports.application.internal.outboundservices.acl;

import com.sportplus.pulsepower.physiology.interfaces.acl.PhysiologyContextFacade;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import com.sportplus.pulsepower.sleep.interfaces.acl.SleepContextFacade;
import com.sportplus.pulsepower.training.interfaces.acl.TrainingContextFacade;
import com.sportplus.pulsepower.wellness.interfaces.acl.WellnessContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * The only place in the reports context that talks to the other bounded contexts,
 * through their anti-corruption facades (never their repositories or aggregates
 * directly). Translates each context's own summary record into this context's own
 * {@link ReportEntry}, so reports stays unaffected by changes inside training, sleep,
 * wellness or physiology.
 */
@Service
public class ReportEntriesGateway {

    private final TrainingContextFacade trainingContextFacade;
    private final SleepContextFacade sleepContextFacade;
    private final WellnessContextFacade wellnessContextFacade;
    private final PhysiologyContextFacade physiologyContextFacade;

    public ReportEntriesGateway(TrainingContextFacade trainingContextFacade, SleepContextFacade sleepContextFacade,
                                WellnessContextFacade wellnessContextFacade,
                                PhysiologyContextFacade physiologyContextFacade) {
        this.trainingContextFacade = trainingContextFacade;
        this.sleepContextFacade = sleepContextFacade;
        this.wellnessContextFacade = wellnessContextFacade;
        this.physiologyContextFacade = physiologyContextFacade;
    }

    public List<ReportEntry> fetchEntries(Long userId, LocalDate from, LocalDate to, List<ReportType> types) {
        return types.stream()
                .flatMap(type -> fetchEntriesForType(userId, from, to, type).stream())
                .sorted((a, b) -> a.date().compareTo(b.date()))
                .toList();
    }

    private List<ReportEntry> fetchEntriesForType(Long userId, LocalDate from, LocalDate to, ReportType type) {
        return switch (type) {
            case TRAINING -> trainingContextFacade.fetchEntriesBetween(userId, from, to).stream()
                    .map(entry -> new ReportEntry(entry.date(), ReportType.TRAINING, entry.summary()))
                    .toList();
            case SLEEP -> sleepContextFacade.fetchEntriesBetween(userId, from, to).stream()
                    .map(entry -> new ReportEntry(entry.date(), ReportType.SLEEP, entry.summary()))
                    .toList();
            case WELLNESS -> wellnessContextFacade.fetchEntriesBetween(userId, from, to).stream()
                    .map(entry -> new ReportEntry(entry.date(), ReportType.WELLNESS, entry.summary()))
                    .toList();
            case RECOVERY -> physiologyContextFacade.fetchEntriesBetween(userId, from, to).stream()
                    .map(entry -> new ReportEntry(entry.date(), ReportType.RECOVERY, entry.summary()))
                    .toList();
        };
    }
}
