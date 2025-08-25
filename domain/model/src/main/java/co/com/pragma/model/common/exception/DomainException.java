package co.com.pragma.model.common.exception;

/**
 * Excepción base abstracta para errores de dominio
 * Sin dependencias externas, solo Java puro
 * No debe instanciarse directamente, usar excepciones específicas
 */
public abstract class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
