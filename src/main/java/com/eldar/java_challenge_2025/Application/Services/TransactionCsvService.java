package com.eldar.java_challenge_2025.Application.Services;

import com.eldar.java_challenge_2025.Application.DTO.Request.TransactionCreateDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.CsvRowResultDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.CsvUploadResponseDTO;
import com.eldar.java_challenge_2025.Application.DTO.Response.TransactionDTO;
import com.eldar.java_challenge_2025.Domain.Enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TransactionCsvService implements ITransactionCsvService {

    @Autowired
    private TransactionService transactionService;

    private static final String CSV_SPLIT_REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    // Cabecera esperada para validar que esten cargando el formato de csv que queremos
    private static final String[] EXPECTED_HEADER = {
            "transactionId", "accountIdFrom", "accountId", "amount", "type", "timestamp"
    };

    public CsvUploadResponseDTO processCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV está vacío");
        }

        List<CsvRowResultDTO> results = new ArrayList<>();
        int success = 0, errors = 0, total = 0;
        int lineNumber = 0;

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String header = reader.readLine();
            lineNumber = 1;
            if (header == null) {
                throw new IllegalArgumentException("El CSV no tiene contenido (falta cabecera y filas).");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;              // ahora refleja el número real de línea en el archivo
                if (line.isBlank()) continue;
                total++;

                try {
                    String[] cols = line.split(CSV_SPLIT_REGEX, -1);
                    for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();

                    String transactionId = cols[0];
                    Long accountIdFrom   = parseLong(cols[1], "accountIdFrom");
                    Long accountIdTo     = parseLong(cols[2], "accountId");
                    BigDecimal amount    = parseBigDecimal(cols[3], "amount");
                    TransactionType type = parseType(cols[4]);
                    String timestamp2    = String.valueOf(parseTimestamp(cols[5]));

                    TransactionCreateDTO dto = new TransactionCreateDTO(
                            transactionId, accountIdFrom, accountIdTo, amount, type, timestamp2
                    );

                    TransactionDTO created = transactionService.recordTransfer(dto);

                    results.add(new CsvRowResultDTO(lineNumber, transactionId, true, "Transacción guardada con éxito"));
                    success++;

                } catch (Exception ex) {
                    results.add(new CsvRowResultDTO(lineNumber, getTransactionId(line), false, ex.getMessage()));
                    errors++;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("No se pudo leer el CSV: " + e.getMessage(), e);
        }

        return new CsvUploadResponseDTO(total, success, errors, results);
    }


    private static Long parseLong(String value, String field) {
        try { return Long.valueOf(value); }
        catch (Exception e) { throw new IllegalArgumentException("Campo " + field + " inválido: '" + value + "'"); }
    }

    private static BigDecimal parseBigDecimal(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Campo " + field + " vacío");
        }
        //pasamos bigdecimal a string y luego de nuevo a bigdecimal
        //esto es por el formato de las columnas en excel, para evitar porblemas siempre va a ser con .
        String bigdecimalString = value.replace(",", ".");
        try {
            return new BigDecimal(bigdecimalString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Campo " + field + " inválido: '" + value + "'");
        }
    }

    private static TransactionType parseType(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Campo type vacío (se espera 0 o 1)");
        }
        if (!value.equals("0") && !value.equals("1")) {
            throw new IllegalArgumentException("Campo type inválido: '" + value + "'. Sólo se acepta 0 o 1");
        }
        return value.equals("0") ? TransactionType.DEBIT : TransactionType.CREDIT;
    }

    private static Instant parseTimestamp(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo timestamp es obligatorio");
        }

        // Validaciones para controlar que cumple con el formato permitido de Instant
        // Remplaza los espacios por T
        String s = value.trim().replace(' ', 'T');

        // Si no contiene zona le agrego 'Z' (UTC)
        if (!s.endsWith("Z") && !s.contains("+") && !s.contains("-")) {
            s = s + "Z";
        }

        try {
            return Instant.parse(s);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Formato de timestamp inválido. Ejemplo válido: 2025-10-02T12:00:00Z"
            );
        }
    }


    private static String getTransactionId(String line) {
        try {
            //En la primera columna obtenemos el transactionId para el mensaje
            String[] cols = line.split(CSV_SPLIT_REGEX, -1);
            return cols[0].trim();
        } catch (Exception e) {
            return null;
        }
    }
}