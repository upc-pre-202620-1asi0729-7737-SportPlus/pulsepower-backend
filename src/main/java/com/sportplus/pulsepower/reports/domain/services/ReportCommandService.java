package com.sportplus.pulsepower.reports.domain.services;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.commands.CreateReportCommand;

public interface ReportCommandService {

    ProgressReport handle(CreateReportCommand command);
}
