package co.com.pragma.model.common.exception;

import java.math.BigDecimal;

/**
 * Excepción específica para salarios fuera del rango permitido
 * Se lanza cuando el salario no está en el rango válido para créditos
 */
public class SalaryOutOfRangeException extends BusinessRuleException {
    
    private final BigDecimal salary;
    private final BigDecimal minSalary;
    private final BigDecimal maxSalary;
    
    public SalaryOutOfRangeException(BigDecimal salary, BigDecimal minSalary, BigDecimal maxSalary) {
        super(String.format("El salario base %s debe estar entre %s y %s para ser elegible para créditos", 
              salary, minSalary, maxSalary));
        this.salary = salary;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public BigDecimal getMinSalary() {
        return minSalary;
    }
    
    public BigDecimal getMaxSalary() {
        return maxSalary;
    }
}
