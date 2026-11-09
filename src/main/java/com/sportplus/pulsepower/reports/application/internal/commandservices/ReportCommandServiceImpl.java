package com.sportplus.pulsepower.reports.application.internal.commandservices;

import com.sportplus.pulsepower.reports.application.internal.outboundservices.acl.ReportEntriesGateway;
import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.commands.CreateReportCommand;
import com.sportplus.pulsepower.reports.domain.repositories.ReportRepository;
import com.sportplus.pulsepower.reports.domain.services.ReportCommandService;
import com.sportplus.pulsepower.reports.domain.services.ReportHistoryValidator;
import com.sportplus.pulsepower.shared.application.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@Transactional
public class ReportCommandServiceImpl implements ReportCommandService {

    private static final LocalDate HISTORY_START = LocalDate.of(1970, 1, 1);

    private final ReportRepository reportRepository;
    private final ReportEntriesGateway reportEntriesGateway;
    private final CurrentUserProvider currentUserProvider;

    public ReportCommandServiceImpl(ReportRepository reportRepository, ReportEntriesGateway reportEntriesGateway,
                                    CurrentUserProvider currentUserProvider) {
        this.reportRepository = reportRepository;
        this.reportEntriesGateway = reportEntriesGateway;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ProgressReport handle(CreateReportCommand command) {
        currentUserProvider.ensureOwnership(command.userId(), "Progress report");

        var historyUpToTo = reportEntriesGateway.fetchEntries(command.userId(), HISTORY_START, command.to(),
                command.types());
        ReportHistoryValidator.validate(command.from(), command.to(), command.types(), historyUpToTo);

        var fileName = "PulsePower-%s-%s.pdf".formatted(command.from(), command.to());
        var report = new ProgressReport(command.userId(), command.from(), command.to(), command.types(),
                Instant.now(), fileName, command.documentBase64());
        return reportRepository.save(report);
    }
}
