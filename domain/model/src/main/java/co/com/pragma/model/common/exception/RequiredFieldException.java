package co.com.pragma.model.common.exception;

/**
 * Excepción específica para campos obligatorios faltantes
 * Se lanza cuando un campo requerido es nulo o vacío
 */
public class RequiredFieldException extends ValidationException {
    
    private final String fieldName;
    
    public RequiredFieldException(String fieldName) {
        super(String.format("El campo '%s' es obligatorio", fieldName));
        this.fieldName = fieldName;
    }
    
    public RequiredFieldException(String fieldName, String customMessage) {
        super(customMessage);
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
}
