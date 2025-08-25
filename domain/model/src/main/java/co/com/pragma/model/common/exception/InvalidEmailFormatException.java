package co.com.pragma.model.common.exception;

/**
 * Excepción específica para errores de formato de correo electrónico
 * Se lanza cuando el formato del email no es válido
 */
public class InvalidEmailFormatException extends ValidationException {
    
    private final String email;
    
    public InvalidEmailFormatException(String email, String reason) {
        super(String.format("Formato de correo electrónico inválido '%s': %s", email, reason));
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
