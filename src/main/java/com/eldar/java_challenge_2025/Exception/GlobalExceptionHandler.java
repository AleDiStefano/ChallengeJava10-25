package com.eldar.java_challenge_2025.Exception;
import com.eldar.java_challenge_2025.Application.DTO.Response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: entidad de negocio no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.create(404, "Not Found", ex.getMessage(), req.getRequestURI()));
    }

    // 404: operación de datos sin filas afectadas (delete con id erroneo)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleEmptyResult(EmptyResultDataAccessException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.create(404, "Not Found", "Recurso no encontrado", req.getRequestURI()));
    }

    // 400: reglas de negocio inválidas (monto < 0, ids iguales, body nulo)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.create(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
    }

    // 400: JSON mal escrito
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.create(400, "Malformed JSON", "Cuerpo de la solicitud inválido", req.getRequestURI()));
    }

    // 400: validación de DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> details = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            details.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                ErrorResponse.create(400, "Validation Error", "Datos inválidos", req.getRequestURI(), details)
        );
    }

    // 400: validación de parámetros PathVariable/RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, Object> details = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            details.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return ResponseEntity.badRequest().body(
                ErrorResponse.create(400, "Constraint Violation", "Parámetros inválidos", req.getRequestURI(), details)
        );
    }

    // 400: tipos incompatibles en argumentos
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = "El parámetro '%s' debe ser de tipo %s".formatted(
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido"
        );
        return ResponseEntity.badRequest()
                .body(ErrorResponse.create(400, "Type Mismatch", msg, req.getRequestURI()));
    }

    // 400: faltan parametros
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String msg = "Falta el parámetro requerido: " + ex.getParameterName();
        return ResponseEntity.badRequest()
                .body(ErrorResponse.create(400, "Missing Parameter", msg, req.getRequestURI()));
    }

    // 409: violaciones de integridad
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.create(409, "Conflict", "Violación de integridad de datos", req.getRequestURI()));
    }

    // 409: conflictos de concurrencia
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.create(409, "Conflict", "Conflicto de concurrencia", req.getRequestURI()));
    }

    // 400/422: cuando algo falla al hacer commit dentro de una transacción JPA (@Transactional)
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTxSystem(TransactionSystemException ex, HttpServletRequest req) {
        Throwable root = ex.getMostSpecificCause();
        if (root instanceof ConstraintViolationException cve) {
            Map<String, Object> details = new LinkedHashMap<>();
            for (ConstraintViolation<?> v : cve.getConstraintViolations()) {
                details.put(v.getPropertyPath().toString(), v.getMessage());
            }
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.create(400, "Validation Error", "Datos inválidos", req.getRequestURI(), details));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.create(400, "Transaction Error", "Error de validación en transacción", req.getRequestURI()));
    }

    // 500: último recurso (cualquier cosa no contemplada arriba)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.create(500, "Internal Server Error", ex.getMessage(), req.getRequestURI()));
    }
}