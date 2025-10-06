package com.eldar.java_challenge_2025.Application.Services;

import com.eldar.java_challenge_2025.Application.DTO.Response.CsvUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ITransactionCsvService {
    CsvUploadResponseDTO processCsv(MultipartFile file);
}
