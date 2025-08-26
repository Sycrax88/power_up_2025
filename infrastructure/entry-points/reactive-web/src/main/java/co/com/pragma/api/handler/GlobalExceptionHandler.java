package co.com.pragma.api.handler;

import co.com.pragma.api.dto.ErrorResponseDto;
import co.com.pragma.model.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manejador global de excepciones para la capa de entrada (API REST)
 * 
 * Implementa mejores prácticas para Spring WebFlux:
 * - Manejo específico por tipo de excepción del dominio
 * - Trazabilidad con trace IDs
 * - Logging estructurado para observabilidad
 * - Respuestas estandardizadas (RFC 7807 adaptado)
 * - Ordenamiento de handlers por especificidad
 */
@Slf4j
@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {
    
    // Constantes para logging y trazabilidad
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String UNKNOWN_ERROR_MESSAGE = "Ha ocurrido un error interno en el servidor";
    
    /**
     * Maneja errores de validación específicos del dominio
     */
    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleValidationException(
            ValidationException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Error de validación en {}: {}", traceId, path, ex.getMessage());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(ex.getMessage())
                .code("VALIDATION_ERROR")
                .path(path)
                .traceId(traceId)
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.BAD_REQUEST, traceId);
    }
    
    /**
     * Maneja errores de campos obligatorios específicamente
     */
    @ExceptionHandler(RequiredFieldException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleRequiredFieldException(
            RequiredFieldException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Campo obligatorio faltante en {}: campo={}, mensaje={}", 
            traceId, path, ex.getFieldName(), ex.getMessage());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Required Field Missing")
                .message(ex.getMessage())
                .code("REQUIRED_FIELD_MISSING")
                .path(path)
                .traceId(traceId)
                .details(Map.of("field", ex.getFieldName()))
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.BAD_REQUEST, traceId);
    }
    
    /**
     * Maneja errores de formato de email específicamente
     */
    @ExceptionHandler(InvalidEmailFormatException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleInvalidEmailFormatException(
            InvalidEmailFormatException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Email con formato inválido en {}: email={}", 
            traceId, path, ex.getEmail());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Email Format")
                .message(ex.getMessage())
                .code("INVALID_EMAIL_FORMAT")
                .path(path)
                .traceId(traceId)
                .details(Map.of("email", ex.getEmail()))
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.BAD_REQUEST, traceId);
    }
    
    /**
     * Maneja errores de reglas de negocio
     */
    @ExceptionHandler(BusinessRuleException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleBusinessRuleException(
            BusinessRuleException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Violación de regla de negocio en {}: {}", 
            traceId, path, ex.getMessage());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .code("BUSINESS_RULE_VIOLATION")
                .path(path)
                .traceId(traceId)
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.UNPROCESSABLE_ENTITY, traceId);
    }
    
    /**
     * Maneja errores de salario fuera de rango específicamente
     */
    @ExceptionHandler(SalaryOutOfRangeException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleSalaryOutOfRangeException(
            SalaryOutOfRangeException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Salario fuera de rango en {}: salario={}, min={}, max={}", 
            traceId, path, ex.getSalary(), ex.getMinSalary(), ex.getMaxSalary());
        
        Map<String, String> details = new HashMap<>();
        details.put("salary", ex.getSalary().toString());
        details.put("minSalary", ex.getMinSalary().toString());
        details.put("maxSalary", ex.getMaxSalary().toString());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Salary Out Of Range")
                .message(ex.getMessage())
                .code("SALARY_OUT_OF_RANGE")
                .path(path)
                .traceId(traceId)
                .details(details)
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.UNPROCESSABLE_ENTITY, traceId);
    }
    
    /**
     * Maneja errores de fecha de nacimiento futura específicamente
     */
    @ExceptionHandler(FutureBirthDateException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleFutureBirthDateException(
            FutureBirthDateException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Fecha de nacimiento futura en {}: fecha={}", 
            traceId, path, ex.getBirthDate());
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Future Birth Date")
                .message(ex.getMessage())
                .code("FUTURE_BIRTH_DATE")
                .path(path)
                .traceId(traceId)
                .details(Map.of("birthDate", ex.getBirthDate().toString()))
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.UNPROCESSABLE_ENTITY, traceId);
    }
    
    /**
     * Maneja errores de validación de Bean Validation (JSR-303)
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleWebExchangeBindException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.warn("[{}] Errores de validación Bean Validation en {}: {}", 
            traceId, path, ex.getMessage());
        
        // Recopilar todos los errores de validación
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                fieldErrors.put("global", error.getDefaultMessage());
            }
        });
        
        String mainErrorMessage = fieldErrors.isEmpty() ? 
            "Error de validación en los datos de entrada" :
            fieldErrors.values().iterator().next();
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bean Validation Failed")
                .message(mainErrorMessage)
                .code("BEAN_VALIDATION_FAILED")
                .path(path)
                .traceId(traceId)
                .details(fieldErrors)
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.BAD_REQUEST, traceId);
    }
    
    /**
     * Maneja errores genéricos de dominio (fallback)
     */
    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleDomainException(
            DomainException ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.error("[{}] Error de dominio no específico en {}: {}", 
            traceId, path, ex.getMessage(), ex);
        
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Domain Error")
                .message(ex.getMessage())
                .code("DOMAIN_ERROR")
                .path(path)
                .traceId(traceId)
                .build();
        
        return createResponseWithTraceId(error, HttpStatus.BAD_REQUEST, traceId);
    }
    
    /**
     * Maneja cualquier excepción no controlada (último resort)
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponseDto>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {
        
        String traceId = generateTraceId();
        String path = exchange.getRequest().getPath().value();
        
        log.error("[{}] Error interno no controlado en {}: {}", 
            traceId, path, ex.getMessage(), ex);
        
        ErrorResponseDto error = ErrorResponseDto.internalError(traceId, path);
        
        return createResponseWithTraceId(error, HttpStatus.INTERNAL_SERVER_ERROR, traceId);
    }
    
    // Métodos de utilidad
    
    /**
     * Genera un trace ID único para trazabilidad
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Crea una respuesta con el trace ID en el header
     */
    private Mono<ResponseEntity<ErrorResponseDto>> createResponseWithTraceId(
            ErrorResponseDto error, HttpStatus status, String traceId) {
        
        return Mono.just(ResponseEntity
                .status(status)
                .header(TRACE_ID_HEADER, traceId)
                .body(error));
    }
}
