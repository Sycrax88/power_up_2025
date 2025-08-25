package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.RequiredFieldException;
import co.com.pragma.model.common.exception.BusinessRuleException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa el salario base de un Usuario.
 * Contiene las reglas de negocio para elegibilidad de créditos.
 */
public final class SalarioBase {
    
    public static final BigDecimal SALARIO_MINIMO_CREDITO = BigDecimal.ZERO;
    public static final BigDecimal SALARIO_MAXIMO_CREDITO = new BigDecimal("15000000");
    
    private final BigDecimal valor;
    
    private SalarioBase(BigDecimal valor) {
        validarReglasDeNegocio(valor);
        this.valor = valor;
    }
    
    public static SalarioBase de(BigDecimal valor) {
        return new SalarioBase(valor);
    }
    
    public static SalarioBase de(double valor) {
        return new SalarioBase(BigDecimal.valueOf(valor));
    }
    
    private void validarReglasDeNegocio(BigDecimal valor) {
        if (valor == null) {
            throw new RequiredFieldException("salarioBase", "El salario base no puede ser nulo");
        }
        
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("El salario base no puede ser negativo");
        }
    }
    
    public boolean estaEnRangoValidoParaCredito() {
        return valor.compareTo(SALARIO_MINIMO_CREDITO) >= 0 && 
               valor.compareTo(SALARIO_MAXIMO_CREDITO) <= 0;
    }
    
    public BigDecimal valor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SalarioBase that = (SalarioBase) obj;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return valor.toString();
    }
}
