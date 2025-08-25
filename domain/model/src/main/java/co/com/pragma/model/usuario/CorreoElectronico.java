package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import java.util.Objects;

/**
 * Value Object que representa un correo electrónico.
 * Solo validaciones de dominio básicas, las técnicas van en infraestructura.
 */
public final class CorreoElectronico {
    
    private static final int LONGITUD_MAXIMA = 150;
    private static final int LONGITUD_MINIMA = 5;
    
    private final String valor;
    
    private CorreoElectronico(String valor) {
        String valorLimpio = validarYLimpiar(valor);
        validarReglasDeNegocio(valorLimpio);
        this.valor = valorLimpio;
    }
    
    public static CorreoElectronico de(String valor) {
        return new CorreoElectronico(valor);
    }
    
    private String validarYLimpiar(String valor) {
        if (valor == null) {
            throw new RequiredFieldException("correoElectronico", "El correo electrónico no puede ser nulo");
        }
        
        String valorLimpio = valor.trim().toLowerCase();
        if (valorLimpio.isEmpty()) {
            throw new RequiredFieldException("correoElectronico", "El correo electrónico no puede estar vacío");
        }
        
        return valorLimpio;
    }
    
    private void validarReglasDeNegocio(String valor) {
        if (valor.length() < LONGITUD_MINIMA) {
            throw new ValidationException("El correo electrónico es demasiado corto");
        }
        
        if (valor.length() > LONGITUD_MAXIMA) {
            throw new ValidationException("El correo electrónico no puede exceder " + LONGITUD_MAXIMA + " caracteres");
        }
        
        // Validación básica de dominio: debe contener @
        if (!valor.contains("@")) {
            throw new InvalidEmailFormatException(valor, "debe contener @");
        }
        
        // Validación básica: no puede empezar o terminar con @
        if (valor.startsWith("@") || valor.endsWith("@")) {
            throw new InvalidEmailFormatException(valor, "no puede empezar o terminar con @");
        }
    }
    
    public String valor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CorreoElectronico that = (CorreoElectronico) obj;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
