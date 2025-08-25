package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SalarioBase - Tests de Value Object")
class SalarioBaseTest {
    
    // ✅ CONSTANTES PARA TESTS
    private static final BigDecimal SALARIO_VALIDO = new BigDecimal("2500000");
    private static final BigDecimal SALARIO_MINIMO = SalarioBase.SALARIO_MINIMO_CREDITO;
    private static final BigDecimal SALARIO_MAXIMO = SalarioBase.SALARIO_MAXIMO_CREDITO;
    private static final BigDecimal SALARIO_NEGATIVO = new BigDecimal("-1000");

    
    // ========================================
    // TESTS DE CREACIÓN EXITOSA
    // ========================================
    
    @Test
    @DisplayName("Debería crear SalarioBase válido desde BigDecimal")
    void deberiaCrearSalarioBaseValidoDesdeBigDecimal() {
        // Arrange
        BigDecimal valor = SALARIO_VALIDO;
        
        // Act
        SalarioBase salario = SalarioBase.de(valor);
        
        // Assert
        assertAll("SalarioBase creado correctamente",
            () -> assertNotNull(salario, "El SalarioBase no debe ser null"),
            () -> assertEquals(valor, salario.valor(), "El valor debe coincidir"),
            () -> assertTrue(salario.estaEnRangoValidoParaCredito(), "Debe estar en rango válido"),
            () -> assertNotNull(salario.toString(), "ToString debe funcionar")
        );
    }
    
    @Test
    @DisplayName("Debería crear SalarioBase válido desde double")
    void deberiaCrearSalarioBaseValidoDesdeDouble() {
        // Arrange
        double valor = 2500000.0;
        
        // Act
        SalarioBase salario = SalarioBase.de(valor);
        
        // Assert
        assertAll("SalarioBase desde double creado correctamente",
            () -> assertNotNull(salario, "El SalarioBase no debe ser null"),
            () -> assertEquals(BigDecimal.valueOf(valor), salario.valor(), "El valor debe coincidir"),
            () -> assertTrue(salario.estaEnRangoValidoParaCredito(), "Debe estar en rango válido")
        );
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando salario es null")
    void deberiaFallarCuandoSalarioEsNull() {
        // Arrange
        BigDecimal salario = null;
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            SalarioBase.de(salario);
        });
        
        assertTrue(exception.getMessage().contains("nulo"), 
            "El mensaje debe indicar que el salario no puede ser nulo");
    }
    
    @Test
    @DisplayName("Debería fallar cuando salario es negativo")
    void deberiaFallarCuandoSalarioEsNegativo() {
        // Arrange
        BigDecimal salario = SALARIO_NEGATIVO;
        
        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            SalarioBase.de(salario);
        });
        
        assertTrue(exception.getMessage().contains("negativo"), 
            "El mensaje debe indicar que el salario no puede ser negativo");
    }
    
    // ========================================
    // TESTS DE REGLAS DE NEGOCIO
    // ========================================
    
    @Test
    @DisplayName("Debería validar salario en límite mínimo como válido para crédito")
    void deberiaValidarSalarioEnLimiteMinimoComoValidoParaCredito() {
        // Arrange
        BigDecimal salarioMinimo = SALARIO_MINIMO;
        
        // Act
        SalarioBase salario = SalarioBase.de(salarioMinimo);
        
        // Assert
        assertTrue(salario.estaEnRangoValidoParaCredito(), 
            "El salario mínimo debe ser válido para crédito");
    }
    
    @Test
    @DisplayName("Debería validar salario en límite máximo como válido para crédito")
    void deberiaValidarSalarioEnLimiteMaximoComoValidoParaCredito() {
        // Arrange
        BigDecimal salarioMaximo = SALARIO_MAXIMO;
        
        // Act
        SalarioBase salario = SalarioBase.de(salarioMaximo);
        
        // Assert
        assertTrue(salario.estaEnRangoValidoParaCredito(), 
            "El salario máximo debe ser válido para crédito");
    }
    
    @Test
    @DisplayName("Debería validar salario dentro del rango como válido para crédito")
    void deberiaValidarSalarioDentroDelRangoComoValidoParaCredito() {
        // Arrange
        BigDecimal salarioMedio = new BigDecimal("7500000"); // Mitad del rango
        
        // Act
        SalarioBase salario = SalarioBase.de(salarioMedio);
        
        // Assert
        assertTrue(salario.estaEnRangoValidoParaCredito(), 
            "Un salario dentro del rango debe ser válido para crédito");
    }
    
    // ========================================
    // TESTS DE IGUALDAD Y HASH CODE
    // ========================================
    
    @Test
    @DisplayName("Debería considerar iguales SalarioBase con mismo valor")
    void deberiaConsiderarIgualesSalarioBaseConMismoValor() {
        // Arrange
        BigDecimal valor = SALARIO_VALIDO;
        SalarioBase salario1 = SalarioBase.de(valor);
        SalarioBase salario2 = SalarioBase.de(valor);
        
        // Act & Assert
        assertAll("Igualdad basada en valor",
            () -> assertEquals(salario1, salario2, "SalarioBase con mismo valor deben ser iguales"),
            () -> assertEquals(salario1.hashCode(), salario2.hashCode(), 
                "Hash codes deben ser iguales para objetos iguales"),
            () -> assertEquals(salario1.toString(), salario2.toString(), 
                "ToString debe ser igual para objetos iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar diferentes SalarioBase con valores diferentes")
    void deberiaConsiderarDiferentesSalarioBaseConValoresDiferentes() {
        // Arrange
        SalarioBase salario1 = SalarioBase.de(new BigDecimal("2000000"));
        SalarioBase salario2 = SalarioBase.de(new BigDecimal("3000000"));
        
        // Act & Assert
        assertAll("Diferencia basada en valor",
            () -> assertNotEquals(salario1, salario2, "SalarioBase con valores diferentes deben ser diferentes"),
            () -> assertNotEquals(salario1.valor(), salario2.valor(), "Los valores deben ser diferentes")
        );
    }
    
    // ========================================
    // TESTS DE CASOS EDGE
    // ========================================
    
    @Test
    @DisplayName("Debería manejar correctamente salario cero")
    void deberiaManejarCorrectamenteSalarioCero() {
        // Arrange
        BigDecimal salarioCero = BigDecimal.ZERO;
        
        // Act
        SalarioBase salario = SalarioBase.de(salarioCero);
        
        // Assert
        assertAll("Salario cero válido",
            () -> assertEquals(salarioCero, salario.valor(), "Debe aceptar salario cero"),
            () -> assertTrue(salario.estaEnRangoValidoParaCredito(), "Salario cero debe ser válido para crédito")
        );
    }
    
    @Test
    @DisplayName("Debería manejar correctamente decimales")
    void deberiaManejarCorrectamenteDecimales() {
        // Arrange
        BigDecimal salarioConDecimales = new BigDecimal("2500000.50");
        
        // Act
        SalarioBase salario = SalarioBase.de(salarioConDecimales);
        
        // Assert
        assertAll("Salario con decimales",
            () -> assertEquals(salarioConDecimales, salario.valor(), "Debe preservar decimales"),
            () -> assertTrue(salario.estaEnRangoValidoParaCredito(), "Debe ser válido para crédito")
        );
    }
}
