package com.sportplus.pulsepower.physiology.interfaces.acl;

import com.sportplus.pulsepower.physiology.domain.model.queries.GetPhysiologicalOverviewByUserIdQuery;
import com.sportplus.pulsepower.physiology.domain.services.PhysiologyQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * What the physiology context exposes to other bounded contexts (reports, community),
 * so they never read physiology's own tables or aggregates directly. Backs the
 * frontend's 'Recovery' report category.
 */
@Service
public class PhysiologyContextFacade {

    private final PhysiologyQueryService physiologyQueryService;

    public PhysiologyContextFacade(PhysiologyQueryService physiologyQueryService) {
        this.physiologyQueryService = physiologyQueryService;
    }

    public List<LocalDate> fetchRecordedDates(Long userId) {
        var overview = physiologyQueryService.handle(new GetPhysiologicalOverviewByUserIdQuery(userId));
        return overview.records().stream()
                .map(record -> record.getRecordedAt().atZone(ZoneOffset.UTC).toLocalDate())
                .distinct()
                .toList();
    }

    public List<PhysiologyEntrySummary> fetchEntriesBetween(Long userId, LocalDate from, LocalDate to) {
        var overview = physiologyQueryService.handle(new GetPhysiologicalOverviewByUserIdQuery(userId));
        return overview.records().stream()
                .map(record -> new PhysiologyEntrySummary(record.getRecordedAt().atZone(ZoneOffset.UTC).toLocalDate(),
                        "%s %.1f %s".formatted(record.getMetric(), record.getValue(), record.getUnit())))
                .filter(entry -> !entry.date().isBefore(from) && !entry.date().isAfter(to))
                .toList();
    }
}
