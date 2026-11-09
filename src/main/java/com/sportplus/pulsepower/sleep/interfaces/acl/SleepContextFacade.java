package com.sportplus.pulsepower.sleep.interfaces.acl;

import com.sportplus.pulsepower.sleep.domain.model.queries.GetSleepRecordsByUserIdQuery;
import com.sportplus.pulsepower.sleep.domain.services.SleepQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * What the sleep context exposes to other bounded contexts (reports, community).
 */
@Service
public class SleepContextFacade {

    private final SleepQueryService sleepQueryService;

    public SleepContextFacade(SleepQueryService sleepQueryService) {
        this.sleepQueryService = sleepQueryService;
    }

    public List<LocalDate> fetchRecordedDates(Long userId) {
        return sleepQueryService.handle(new GetSleepRecordsByUserIdQuery(userId)).stream()
                .map(record -> record.getStartedAt().atZone(ZoneOffset.UTC).toLocalDate())
                .distinct()
                .toList();
    }

    public List<SleepEntrySummary> fetchEntriesBetween(Long userId, LocalDate from, LocalDate to) {
        return sleepQueryService.handle(new GetSleepRecordsByUserIdQuery(userId)).stream()
                .map(record -> new SleepEntrySummary(record.getStartedAt().atZone(ZoneOffset.UTC).toLocalDate(),
                        "%.1fh sleep (%s)".formatted(record.durationHours(),
                                record.getSleepScore() == null ? "no score" : "score " + record.getSleepScore())))
                .filter(entry -> !entry.date().isBefore(from) && !entry.date().isAfter(to))
                .toList();
    }
}
