package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para solicitudes de registro de usuario
 * 
 * Implementa validaciones en la capa de entrada siguiendo Bean Validation (JSR-303)
 * y está completamente documentado para OpenAPI 3.0
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RegistrarUsuarioRequest",
    description = "Datos requeridos para registrar un nuevo usuario en el sistema CrediYa",
    example = """
        {
          "nombres": "Juan Carlos",
          "apellidos": "Pérez García",
          "fechaNacimiento": "1990-05-15",
          "direccion": "Calle 123 #45-67",
          "telefono": "+57 300 123 4567",
          "correoElectronico": "juan.perez@email.com",
          "salarioBase": 2500000
        }
        """
)
public class RegistrarUsuarioRequest {
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", 
        message = "Los nombres solo pueden contener letras y espacios"
    )
    @Schema(
        description = "Nombres del usuario. Solo se permiten letras y espacios.",
        example = "Juan Carlos",
        required = true,
        minLength = 2,
        maxLength = 100,
        pattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"
    )
    @JsonPropertyDescription("Nombres del usuario")
    private String nombres;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", 
        message = "Los apellidos solo pueden contener letras y espacios"
    )
    @Schema(
        description = "Apellidos del usuario. Solo se permiten letras y espacios.",
        example = "Pérez García",
        required = true,
        minLength = 2,
        maxLength = 100,
        pattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"
    )
    @JsonPropertyDescription("Apellidos del usuario")
    private String apellidos;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "La fecha de nacimiento no puede ser futura")
    @Schema(
        description = "Fecha de nacimiento del usuario. No puede ser futura y la edad máxima permitida es 120 años.",
        example = "1990-05-15",
        format = "date",
        pattern = "yyyy-MM-dd"
    )
    @JsonPropertyDescription("Fecha de nacimiento (formato: yyyy-MM-dd)")
    private LocalDate fechaNacimiento;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Schema(
        description = "Dirección de residencia del usuario (opcional).",
        example = "Calle 123 #45-67",
        maxLength = 200
    )
    @JsonPropertyDescription("Dirección de residencia")
    private String direccion;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Pattern(
        regexp = "^[+]?[0-9\\s\\-()]*$", 
        message = "El teléfono tiene formato inválido. Solo se permiten números, espacios, guiones, paréntesis y el símbolo +"
    )
    @Schema(
        description = "Número de teléfono del usuario (opcional). Formato libre pero solo números, espacios, guiones, paréntesis y +.",
        example = "+57 300 123 4567",
        maxLength = 20,
        pattern = "^[+]?[0-9\\s\\-()]*$"
    )
    @JsonPropertyDescription("Número de teléfono")
    private String telefono;
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(
        message = "El formato del correo electrónico no es válido",
        regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    )
    @Size(min = 5, max = 150, message = "El correo electrónico debe tener entre 5 y 150 caracteres")
    @Schema(
        description = "Correo electrónico único del usuario. Debe cumplir con el formato RFC 5322 y será validado por unicidad en el sistema.",
        example = "juan.perez@email.com",
        required = true,
        format = "email",
        minLength = 5,
        maxLength = 150
    )
    @JsonPropertyDescription("Correo electrónico único")
    private String correoElectronico;
    
    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0", inclusive = true, message = "El salario base no puede ser negativo")
    @DecimalMax(value = "15000000", inclusive = true, message = "El salario base no puede exceder 15,000,000")
    @Digits(integer = 8, fraction = 2, message = "El salario base debe tener máximo 8 dígitos enteros y 2 decimales")
    @Schema(
        description = "Salario base del usuario en pesos colombianos (COP). Debe estar en el rango de 0 a 15,000,000.",
        example = "2500000",
        required = true,
        minimum = "0",
        maximum = "15000000",
        type = "number",
        format = "decimal"
    )
    @JsonPropertyDescription("Salario base en COP")
    private BigDecimal salarioBase;
    
    // Métodos de utilidad para validación y logging
    
    /**
     * Valida si todos los campos obligatorios están presentes
     * @return true si todos los campos obligatorios están presentes
     */
    public boolean tieneRequerimientos() {
        return nombres != null && !nombres.trim().isEmpty() &&
               apellidos != null && !apellidos.trim().isEmpty() &&
               correoElectronico != null && !correoElectronico.trim().isEmpty() &&
               salarioBase != null;
    }
    
    /**
     * Obtiene un resumen del request para logging (sin datos sensibles)
     * @return String con resumen para logs
     */
    public String getResumenParaLog() {
        return String.format("Usuario[email=%s, nombres=%s, apellidos=%s, salario=%s]", 
            correoElectronico != null ? correoElectronico.replaceAll("(.{2}).*(@.*)", "$1***$2") : "null",
            nombres != null ? nombres.substring(0, Math.min(nombres.length(), 10)) + "..." : "null",
            apellidos != null ? apellidos.substring(0, Math.min(apellidos.length(), 10)) + "..." : "null",
            salarioBase != null ? "****" : "null"
        );
    }
    
    /**
     * Normaliza los datos de entrada (trim, lowercase email, etc.)
     * @return nueva instancia con datos normalizados
     */
    public RegistrarUsuarioRequest normalizar() {
        return this.toBuilder()
                .nombres(nombres != null ? nombres.trim() : null)
                .apellidos(apellidos != null ? apellidos.trim() : null)
                .direccion(direccion != null && !direccion.trim().isEmpty() ? direccion.trim() : null)
                .telefono(telefono != null && !telefono.trim().isEmpty() ? telefono.trim() : null)
                .correoElectronico(correoElectronico != null ? correoElectronico.trim().toLowerCase() : null)
                .build();
    }
}
