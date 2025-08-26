package co.com.pragma.model.common.exception;

/**
 * Excepción para violaciones de reglas de negocio del dominio
 * Se lanza cuando se violan invariantes o reglas específicas del negocio
 */
public class BusinessRuleException extends DomainException {
    
    public BusinessRuleException(String message) {
        super(message);
    }
    
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
