package com.eldar.java_challenge_2025.Application.DTO.Response;

import java.util.List;

public record CsvUploadResponseDTO(
        int totalRows,
        int successCount,
        int errorCount,
        List<CsvRowResultDTO> rows
) {}