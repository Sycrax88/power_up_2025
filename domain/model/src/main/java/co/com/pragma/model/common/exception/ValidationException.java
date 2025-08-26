package co.com.pragma.model.common.exception;

/**
 * Excepción para errores de validación de formato y estructura de datos
 * Se lanza cuando los datos no cumplen con el formato esperado
 */
public class ValidationException extends DomainException {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
