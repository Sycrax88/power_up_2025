package co.com.pragma.r2dbc.exception;

/**
 * Excepción específica para errores de mapeo entre dominio y persistencia
 * 
 * Se lanza cuando ocurren errores durante la conversión bidireccional
 * entre objetos de dominio y entidades de persistencia.
 */
public class MappingException extends RuntimeException {
    
    private final String sourceType;
    private final String targetType;
    
    public MappingException(String message) {
        super(message);
        this.sourceType = "UNKNOWN";
        this.targetType = "UNKNOWN";
    }
    
    public MappingException(String message, Throwable cause) {
        super(message, cause);
        this.sourceType = "UNKNOWN";
        this.targetType = "UNKNOWN";
    }
    
    public MappingException(String message, String sourceType, String targetType) {
        super(message);
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
    
    public MappingException(String message, Throwable cause, String sourceType, String targetType) {
        super(message, cause);
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    /**
     * Obtiene un mensaje detallado incluyendo tipos de origen y destino
     */
    public String getDetailedMessage() {
        return String.format("Error mapeando %s → %s: %s", sourceType, targetType, getMessage());
    }
}
