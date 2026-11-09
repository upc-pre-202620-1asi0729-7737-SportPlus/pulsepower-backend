package com.sportplus.pulsepower.wellness.interfaces.acl;

import com.sportplus.pulsepower.wellness.domain.model.queries.GetCheckInsByUserIdQuery;
import com.sportplus.pulsepower.wellness.domain.services.WellnessQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * What the wellness context exposes to other bounded contexts (reports, community).
 */
@Service
public class WellnessContextFacade {

    private final WellnessQueryService wellnessQueryService;

    public WellnessContextFacade(WellnessQueryService wellnessQueryService) {
        this.wellnessQueryService = wellnessQueryService;
    }

    public List<LocalDate> fetchRecordedDates(Long userId) {
        return wellnessQueryService.handle(new GetCheckInsByUserIdQuery(userId)).stream()
                .map(checkIn -> checkIn.getDate())
                .distinct()
                .toList();
    }

    public List<WellnessEntrySummary> fetchEntriesBetween(Long userId, LocalDate from, LocalDate to) {
        return wellnessQueryService.handle(new GetCheckInsByUserIdQuery(userId)).stream()
                .filter(checkIn -> !checkIn.getDate().isBefore(from) && !checkIn.getDate().isAfter(to))
                .map(checkIn -> new WellnessEntrySummary(checkIn.getDate(),
                        "Mood %d/5, stress %d/10".formatted(checkIn.getMood(), checkIn.getStress())))
                .toList();
    }
}
