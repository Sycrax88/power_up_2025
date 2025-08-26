package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.ValidationException;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa la identidad única de un Usuario.
 */
public final class UsuarioId {
    
    private final String valor;
    
    private UsuarioId(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidationException("El ID del usuario no puede ser nulo o vacío");
        }
        this.valor = valor.trim();
    }
    
    public static UsuarioId de(String valor) {
        return new UsuarioId(valor);
    }
    
    public static UsuarioId generar() {
        return new UsuarioId(UUID.randomUUID().toString());
    }
    
    public String valor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UsuarioId usuarioId = (UsuarioId) obj;
        return Objects.equals(valor, usuarioId.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return "UsuarioId{" + valor + '}';
    }
}
