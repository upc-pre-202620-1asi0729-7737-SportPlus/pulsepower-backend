package com.sportplus.pulsepower.reports.domain.services;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportByIdQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportEntriesQuery;
import com.sportplus.pulsepower.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;

import java.util.List;

public interface ReportQueryService {

    List<ProgressReport> handle(GetReportsByUserIdQuery query);

    ProgressReport handle(GetReportByIdQuery query);

    List<ReportEntry> handle(GetReportEntriesQuery query);
}
