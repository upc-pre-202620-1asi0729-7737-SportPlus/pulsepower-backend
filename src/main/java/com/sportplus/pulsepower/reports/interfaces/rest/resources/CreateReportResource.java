package com.sportplus.pulsepower.reports.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateReportResource(
        @NotNull LocalDate from,
        @NotNull LocalDate to,
        @NotEmpty List<String> types,
        @Size(max = 2000) String note,
        @NotBlank String documentBase64
) {
}
