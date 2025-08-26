package co.com.pragma.r2dbc.exception;

/**
 * Excepción específica para errores de persistencia en la capa de infraestructura
 * 
 * Encapsula errores relacionados con la base de datos y operaciones de persistencia,
 * proporcionando información contextual para el manejo de errores.
 */
public class PersistenceException extends RuntimeException {
    
    private final String operation;
    private final String context;
    
    public PersistenceException(String message) {
        super(message);
        this.operation = "UNKNOWN";
        this.context = "GENERAL";
    }
    
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
        this.operation = "UNKNOWN";
        this.context = "GENERAL";
    }
    
    public PersistenceException(String message, String operation, String context) {
        super(message);
        this.operation = operation;
        this.context = context;
    }
    
    public PersistenceException(String message, Throwable cause, String operation, String context) {
        super(message, cause);
        this.operation = operation;
        this.context = context;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getContext() {
        return context;
    }
    
    /**
     * Obtiene un mensaje detallado incluyendo contexto y operación
     */
    public String getDetailedMessage() {
        return String.format("[%s:%s] %s", operation, context, getMessage());
    }
}
