package com.sportplus.pulsepower.reports.application.internal.queryservices;

import com.sportplus.pulsepower.reports.application.internal.outboundservices.acl.ReportEntriesGateway;
import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportByIdQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportEntriesQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;
import com.sportplus.pulsepower.reports.domain.repositories.ReportRepository;
import com.sportplus.pulsepower.reports.domain.services.ReportQueryService;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import com.sportplus.pulsepower.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportRepository reportRepository;
    private final ReportEntriesGateway reportEntriesGateway;
    private final CurrentUserProvider currentUserProvider;

    public ReportQueryServiceImpl(ReportRepository reportRepository, ReportEntriesGateway reportEntriesGateway,
                                  CurrentUserProvider currentUserProvider) {
        this.reportRepository = reportRepository;
        this.reportEntriesGateway = reportEntriesGateway;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public List<ProgressReport> handle(GetReportsByUserIdQuery query) {
        return reportRepository.findAllByUserIdOrderByRequestedAtDesc(query.userId());
    }

    @Override
    public ProgressReport handle(GetReportByIdQuery query) {
        var report = reportRepository.findById(query.reportId())
                .orElseThrow(() -> new ResourceNotFoundException("Progress report", query.reportId()));
        currentUserProvider.ensureOwnership(report.getUserId(), "Progress report");
        return report;
    }

    @Override
    public List<ReportEntry> handle(GetReportEntriesQuery query) {
        return reportEntriesGateway.fetchEntries(query.userId(), query.from(), query.to(), query.types());
    }
}
