package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Value Object que representa la fecha de nacimiento de un Usuario.
 */
public final class FechaNacimiento {
    
    private final LocalDate valor;
    
    private FechaNacimiento(LocalDate valor) {
        validarReglasDeNegocio(valor);
        this.valor = valor;
    }
    
    public static FechaNacimiento de(LocalDate valor) {
        return new FechaNacimiento(valor);
    }
    
    public static FechaNacimiento opcional(LocalDate valor) {
        return valor != null ? new FechaNacimiento(valor) : null;
    }
    
    private void validarReglasDeNegocio(LocalDate valor) {
        if (valor != null) {
            if (valor.isAfter(LocalDate.now())) {
                throw new FutureBirthDateException(valor);
            }
            
            int edad = calcularEdad(valor);
            if (edad > 120) {
                throw new BusinessRuleException("La fecha de nacimiento no puede indicar una edad mayor a 120 años");
            }
        }
    }
    
    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    public LocalDate valor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FechaNacimiento that = (FechaNacimiento) obj;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return valor != null ? valor.toString() : "No especificada";
    }
}
