package com.sportplus.pulsepower.training.interfaces.acl;

import com.sportplus.pulsepower.training.domain.model.queries.GetSessionsByUserIdQuery;
import com.sportplus.pulsepower.training.domain.services.TrainingQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * What the training context exposes to other bounded contexts (reports, community),
 * so they never read training's own tables or aggregates directly.
 */
@Service
public class TrainingContextFacade {

    private final TrainingQueryService trainingQueryService;

    public TrainingContextFacade(TrainingQueryService trainingQueryService) {
        this.trainingQueryService = trainingQueryService;
    }

    public List<LocalDate> fetchRecordedDates(Long userId) {
        return trainingQueryService.handle(new GetSessionsByUserIdQuery(userId)).stream()
                .map(session -> session.getDate())
                .distinct()
                .toList();
    }

    public List<TrainingEntrySummary> fetchEntriesBetween(Long userId, LocalDate from, LocalDate to) {
        return trainingQueryService.handle(new GetSessionsByUserIdQuery(userId)).stream()
                .filter(session -> !session.getDate().isBefore(from) && !session.getDate().isAfter(to))
                .map(session -> new TrainingEntrySummary(session.getDate(),
                        "%s for %d min (RPE %d)".formatted(session.getActivity(), session.getDurationMinutes(),
                                session.getPerceivedEffort())))
                .toList();
    }
}
