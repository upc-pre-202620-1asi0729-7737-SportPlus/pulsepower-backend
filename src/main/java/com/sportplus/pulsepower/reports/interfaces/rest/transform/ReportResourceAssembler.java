package com.sportplus.pulsepower.reports.interfaces.rest.transform;

import com.sportplus.pulsepower.reports.domain.model.aggregates.ProgressReport;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportEntry;
import com.sportplus.pulsepower.reports.domain.model.valueobjects.ReportType;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ProgressReportResource;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ReportDocumentResource;
import com.sportplus.pulsepower.reports.interfaces.rest.resources.ReportEntryResource;

import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toEnum;
import static com.sportplus.pulsepower.shared.interfaces.rest.transform.EnumValueAssembler.toValue;

public final class ReportResourceAssembler {

    private ReportResourceAssembler() {
    }

    public static ProgressReportResource toResource(ProgressReport report) {
        return new ProgressReportResource(report.getId(), report.getFrom().toString(), report.getTo().toString(),
                report.getTypes().stream().map(ReportResourceAssembler::toValuePublic).toList(),
                report.getRequestedAt().toString(), report.getFileName());
    }

    public static ReportDocumentResource toDocumentResource(ProgressReport report) {
        return new ReportDocumentResource(report.getFileName(), report.getDocumentBase64());
    }

    public static ReportEntryResource toResource(ReportEntry entry) {
        return new ReportEntryResource(entry.date().toString(), toValuePublic(entry.type()), entry.summary());
    }

    public static ReportType toType(String value) {
        return toEnum(ReportType.class, value);
    }

    private static String toValuePublic(ReportType type) {
        return toValue(type);
    }
}
