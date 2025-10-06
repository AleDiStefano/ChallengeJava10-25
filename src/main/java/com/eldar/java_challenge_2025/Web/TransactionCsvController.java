package com.eldar.java_challenge_2025.Web;

import com.eldar.java_challenge_2025.Application.DTO.Response.CsvUploadResponseDTO;
import com.eldar.java_challenge_2025.Application.Services.ITransactionCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/transactions")
public class TransactionCsvController {
    @Autowired
    private ITransactionCsvService csvService;

    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CsvUploadResponseDTO> uploadCsv(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(csvService.processCsv(file));
    }
}