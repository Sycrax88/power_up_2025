package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO estandardizado para respuestas de error
 * 
 * Implementa RFC 7807 (Problem Details for HTTP APIs) adaptado
 * y mejores prácticas para manejo de errores en APIs REST
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
    name = "ErrorResponse",
    description = "Respuesta estandardizada para errores de la API, incluyendo información de trazabilidad y contexto",
    example = """
        {
          "timestamp": "2024-08-24T18:30:00",
          "status": 400,
          "error": "Validation Error",
          "message": "Los nombres son obligatorios",
          "code": "VALIDATION_ERROR",
          "path": "/api/v1/usuarios",
          "traceId": "abc123-def456"
        }
        """
)
public class ErrorResponseDto {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
        description = "Timestamp del momento en que ocurrió el error",
        example = "2024-08-24T18:30:00",
        required = true,
        format = "date-time"
    )
    @JsonPropertyDescription("Fecha y hora del error")
    private LocalDateTime timestamp;
    
    @Schema(
        description = "Código de estado HTTP del error",
        example = "400",
        required = true,
        minimum = "100",
        maximum = "599"
    )
    @JsonPropertyDescription("Código de estado HTTP")
    private int status;
    
    @Schema(
        description = "Tipo de error en formato legible",
        example = "Validation Error",
        required = true
    )
    @JsonPropertyDescription("Tipo de error")
    private String error;
    
    @Schema(
        description = "Mensaje descriptivo del error para el usuario",
        example = "Los nombres son obligatorios",
        required = true
    )
    @JsonPropertyDescription("Mensaje del error")
    private String message;
    
    @Schema(
        description = "Código específico del error para programas cliente",
        example = "VALIDATION_ERROR"
    )
    @JsonPropertyDescription("Código específico del error")
    private String code;
    
    @Schema(
        description = "Ruta del endpoint donde ocurrió el error",
        example = "/api/v1/usuarios"
    )
    @JsonPropertyDescription("Ruta del endpoint")
    private String path;
    
    @Schema(
        description = "ID de trazabilidad para seguimiento en logs",
        example = "abc123-def456"
    )
    @JsonPropertyDescription("ID de trazabilidad")
    private String traceId;
    
    @Schema(
        description = "Detalles adicionales del error (errores de validación específicos, etc.)",
        example = """
            {
              "nombres": "Los nombres son obligatorios",
              "correoElectronico": "El formato del email no es válido"
            }
            """
    )
    @JsonPropertyDescription("Detalles adicionales del error")
    private Map<String, String> details;
    
    // Factory methods para creación rápida
    
    /**
     * Crea una respuesta de error de validación estándar
     */
    public static ErrorResponseDto validationError(String message, String traceId, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Validation Error")
                .message(message)
                .code("VALIDATION_ERROR")
                .path(path)
                .traceId(traceId)
                .build();
    }
    
    /**
     * Crea una respuesta de error de negocio estándar
     */
    public static ErrorResponseDto businessError(String message, String code, String traceId, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(422)
                .error("Business Rule Violation")
                .message(message)
                .code(code)
                .path(path)
                .traceId(traceId)
                .build();
    }
    
    /**
     * Crea una respuesta de error de conflicto (409)
     */
    public static ErrorResponseDto conflictError(String message, String code, String traceId, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(409)
                .error("Conflict")
                .message(message)
                .code(code)
                .path(path)
                .traceId(traceId)
                .build();
    }
    
    /**
     * Crea una respuesta de error interno del servidor
     */
    public static ErrorResponseDto internalError(String traceId, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("Ha ocurrido un error interno en el servidor")
                .code("INTERNAL_ERROR")
                .path(path)
                .traceId(traceId)
                .build();
    }
}
