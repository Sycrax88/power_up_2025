package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para usuario registrado
 * 
 * Implementa mejores prácticas para APIs REST:
 * - Incluye solo campos no nulos (JsonInclude)
 * - Formatos estándar para fechas
 * - Documentación completa OpenAPI 3.0
 * - Metadatos de auditoría
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
    name = "UsuarioResponse",
    description = "Información completa del usuario registrado exitosamente en el sistema CrediYa",
    example = """
        {
          "id": "123e4567-e89b-12d3-a456-426614174000",
          "nombres": "Juan Carlos",
          "apellidos": "Pérez García",
          "fechaNacimiento": "1990-05-15",
          "direccion": "Calle 123 #45-67",
          "telefono": "+57 300 123 4567",
          "correoElectronico": "juan.perez@email.com",
          "salarioBase": 2500000,
          "fechaCreacion": "2024-08-24T18:30:00"
        }
        """
)
public class UsuarioResponseDto {
    
    @Schema(
        description = "Identificador único del usuario generado automáticamente por el sistema",
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        format = "uuid"
    )
    @JsonPropertyDescription("ID único del usuario")
    private String id;
    
    @Schema(
        description = "Nombres del usuario tal como fueron registrados",
        example = "Juan Carlos",
        required = true,
        maxLength = 100
    )
    @JsonPropertyDescription("Nombres del usuario")
    private String nombres;
    
    @Schema(
        description = "Apellidos del usuario tal como fueron registrados",
        example = "Pérez García",
        required = true,
        maxLength = 100
    )
    @JsonPropertyDescription("Apellidos del usuario")
    private String apellidos;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
        description = "Fecha de nacimiento del usuario",
        example = "1990-05-15",
        format = "date",
        pattern = "yyyy-MM-dd"
    )
    @JsonPropertyDescription("Fecha de nacimiento")
    private LocalDate fechaNacimiento;
    
    @Schema(
        description = "Dirección de residencia del usuario",
        example = "Calle 123 #45-67",
        maxLength = 200
    )
    @JsonPropertyDescription("Dirección de residencia")
    private String direccion;
    
    @Schema(
        description = "Número de teléfono de contacto del usuario",
        example = "+57 300 123 4567",
        maxLength = 20
    )
    @JsonPropertyDescription("Número de teléfono")
    private String telefono;
    
    @Schema(
        description = "Correo electrónico único del usuario en el sistema",
        example = "juan.perez@email.com",
        required = true,
        format = "email",
        maxLength = 150
    )
    @JsonPropertyDescription("Correo electrónico único")
    private String correoElectronico;
    
    @Schema(
        description = "Salario base del usuario en pesos colombianos (COP)",
        example = "2500000",
        required = true,
        type = "number",
        format = "decimal",
        minimum = "0",
        maximum = "15000000"
    )
    @JsonPropertyDescription("Salario base en COP")
    private BigDecimal salarioBase;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(
        description = "Fecha y hora de creación del registro en el sistema",
        example = "2024-08-24T18:30:00",
        required = true,
        format = "date-time",
        pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @JsonPropertyDescription("Fecha de creación del registro")
    private LocalDateTime fechaCreacion;
    
    // Métodos de utilidad para respuesta
    
    /**
     * Valida que la respuesta tenga todos los datos mínimos requeridos
     * @return true si la respuesta es válida
     */
    public boolean esRespuestaValida() {
        return id != null && !id.trim().isEmpty() &&
               nombres != null && !nombres.trim().isEmpty() &&
               apellidos != null && !apellidos.trim().isEmpty() &&
               correoElectronico != null && !correoElectronico.trim().isEmpty() &&
               salarioBase != null &&
               fechaCreacion != null;
    }
    
    /**
     * Obtiene el nombre completo del usuario
     * @return nombre completo concatenado
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    /**
     * Obtiene un resumen del usuario para logging
     * @return resumen para logs
     */
    public String getResumenParaLog() {
        return String.format("Usuario[id=%s, email=%s, nombre=%s]", 
            id,
            correoElectronico,
            getNombreCompleto()
        );
    }
}
