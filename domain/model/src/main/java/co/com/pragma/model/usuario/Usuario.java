package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un usuario en el contexto de negocio CrediYa.
 * 
 * Aplica un enfoque híbrido: Value Objects para lógica de negocio compleja,
 * primitivos con validaciones centralizadas para campos simples.
 */
public final class Usuario {
    
    private final UsuarioId id;
    private final SalarioBase salarioBase;
    private final CorreoElectronico correoElectronico;
    private final FechaNacimiento fechaNacimiento;
    private final String nombres;
    private final String apellidos;
    private final String direccion;
    private final String telefono;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaActualizacion;
    
    private Usuario(UsuarioId id, String nombres, String apellidos,
                   FechaNacimiento fechaNacimiento, String direccion, String telefono,
                   CorreoElectronico correoElectronico, SalarioBase salarioBase,
                   LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        
        validarInvariantesDeNegocio(correoElectronico, salarioBase);
        
        this.id = id;
        this.nombres = validarNombres(nombres);
        this.apellidos = validarApellidos(apellidos);
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = validarDireccion(direccion);
        this.telefono = validarTelefono(telefono);
        this.correoElectronico = correoElectronico;
        this.salarioBase = salarioBase;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public static Usuario crearNuevo(String nombres, String apellidos,
                                   LocalDate fechaNacimiento, String direccion, String telefono,
                                   String correoElectronico, BigDecimal salarioBase) {
        
        Objects.requireNonNull(nombres, "Los nombres son obligatorios para crear un usuario");
        Objects.requireNonNull(apellidos, "Los apellidos son obligatorios para crear un usuario");
        Objects.requireNonNull(correoElectronico, "El correo electrónico es obligatorio para crear un usuario");
        Objects.requireNonNull(salarioBase, "El salario base es obligatorio para crear un usuario");
        
        return new Usuario(
            UsuarioId.generar(),
            nombres,
            apellidos,
            fechaNacimiento != null ? FechaNacimiento.de(fechaNacimiento) : null,
            direccion,
            telefono,
            CorreoElectronico.de(correoElectronico),
            SalarioBase.de(salarioBase),
            LocalDateTime.now(),
            null
        );
    }
    
    public static Usuario reconstruir(String id, String nombres, String apellidos,
                                    LocalDate fechaNacimiento, String direccion, String telefono,
                                    String correoElectronico, BigDecimal salarioBase,
                                    LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        
        Objects.requireNonNull(id, "El ID es obligatorio para reconstruir un usuario");
        
        return new Usuario(
            UsuarioId.de(id),
            nombres,
            apellidos,
            fechaNacimiento != null ? FechaNacimiento.de(fechaNacimiento) : null,
            direccion,
            telefono,
            CorreoElectronico.de(correoElectronico),
            SalarioBase.de(salarioBase),
            fechaCreacion,
            fechaActualizacion
        );
    }
    
    private void validarInvariantesDeNegocio(CorreoElectronico correoElectronico, SalarioBase salarioBase) {
        Objects.requireNonNull(correoElectronico, "Un usuario debe tener correo electrónico");
        Objects.requireNonNull(salarioBase, "Un usuario debe tener salario base definido");
        
        // Regla de negocio: Salario debe estar en rango válido para créditos
        if (!salarioBase.estaEnRangoValidoParaCredito()) {
            throw new SalaryOutOfRangeException(
                salarioBase.valor(), 
                SalarioBase.SALARIO_MINIMO_CREDITO, 
                SalarioBase.SALARIO_MAXIMO_CREDITO
            );
        }
    }
    private String validarNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new RequiredFieldException("nombres");
        }
        
        String nombresTrimmed = nombres.trim();
        if (nombresTrimmed.length() < 2 || nombresTrimmed.length() > 100) {
            throw new ValidationException("Los nombres deben tener entre 2 y 100 caracteres");
        }
        
        if (!nombresTrimmed.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new ValidationException("Los nombres solo pueden contener letras y espacios");
        }
        
        return nombresTrimmed;
    }
    
    private String validarApellidos(String apellidos) {
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new ValidationException("Los apellidos son obligatorios");
        }
        
        String apellidosTrimmed = apellidos.trim();
        if (apellidosTrimmed.length() < 2 || apellidosTrimmed.length() > 100) {
            throw new ValidationException("Los apellidos deben tener entre 2 y 100 caracteres");
        }
        
        if (!apellidosTrimmed.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new ValidationException("Los apellidos solo pueden contener letras y espacios");
        }
        
        return apellidosTrimmed;
    }
    
    private String validarDireccion(String direccion) {
        if (direccion != null) {
            String direccionTrimmed = direccion.trim();
            if (direccionTrimmed.length() > 200) {
                throw new ValidationException("La dirección no puede exceder 200 caracteres");
            }
            return direccionTrimmed.isEmpty() ? null : direccionTrimmed;
        }
        return null;
    }
    
    private String validarTelefono(String telefono) {
        if (telefono != null) {
            String telefonoTrimmed = telefono.trim();
            if (telefonoTrimmed.length() > 20) {
                throw new ValidationException("El teléfono no puede exceder 20 caracteres");
            }
            if (!telefonoTrimmed.isEmpty() && !telefonoTrimmed.matches("^[+]?[0-9\\s\\-()]+$")) {
                throw new ValidationException("El teléfono tiene formato inválido");
            }
            return telefonoTrimmed.isEmpty() ? null : telefonoTrimmed;
        }
        return null;
    }
    
    public UsuarioId getId() {
        return id;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public FechaNacimiento getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public CorreoElectronico getCorreoElectronico() {
        return correoElectronico;
    }
    
    public SalarioBase getSalarioBase() {
        return salarioBase;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(id, usuario.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo=" + correoElectronico +
                ", salario=" + salarioBase +
                '}';
    }
}
