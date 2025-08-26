package co.com.pragma.model.common.exception;

import java.time.LocalDate;

/**
 * Excepción específica para fechas de nacimiento futuras
 * Se lanza cuando la fecha de nacimiento es posterior a la fecha actual
 */
public class FutureBirthDateException extends BusinessRuleException {
    
    private final LocalDate birthDate;
    
    public FutureBirthDateException(LocalDate birthDate) {
        super(String.format("La fecha de nacimiento %s no puede ser futura", birthDate));
        this.birthDate = birthDate;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
}
