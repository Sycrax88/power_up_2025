package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de persistencia para Usuario en R2DBC
 * 
 * Mapea la tabla 'usuarios' en PostgreSQL con metadatos de auditoría automáticos
 * y optimizaciones específicas para operaciones reactivas.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("usuarios")
public class UsuarioEntity {
    
    @Id
    @Column("id")
    private String id;
    
    @Column("nombres")
    private String nombres;
    
    @Column("apellidos")
    private String apellidos;
    
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column("direccion")
    private String direccion;
    
    @Column("telefono")
    private String telefono;
    
    @Column("correo_electronico")
    private String correoElectronico;
    
    @Column("salario_base")
    private BigDecimal salarioBase;
    
    @CreatedDate
    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    // Métodos de utilidad para debugging
    
    /**
     * Obtiene un resumen de la entidad para logging
     */
    public String getResumenParaLog() {
        return String.format("UsuarioEntity[id=%s, email=%s]", 
            id, correoElectronico);
    }
    
    /**
     * Valida que la entidad tenga datos mínimos para persistencia
     */
    public boolean esValidaParaPersistencia() {
        return nombres != null && !nombres.trim().isEmpty() &&
               apellidos != null && !apellidos.trim().isEmpty() &&
               correoElectronico != null && !correoElectronico.trim().isEmpty() &&
               salarioBase != null;
    }
}
