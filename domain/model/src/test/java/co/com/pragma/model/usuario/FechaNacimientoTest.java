package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FechaNacimiento - Tests de Value Object")
class FechaNacimientoTest {
    
    // ✅ CONSTANTES PARA TESTS
    private static final LocalDate FECHA_VALIDA = LocalDate.of(1990, 5, 15);
    private static final LocalDate FECHA_RECIENTE = LocalDate.of(2000, 1, 1);

    private static final LocalDate FECHA_MUY_ANTIGUA = LocalDate.of(1900, 1, 1); // Más de 120 años
    
    // ========================================
    // TESTS DE CREACIÓN EXITOSA
    // ========================================
    
    @Test
    @DisplayName("Debería crear FechaNacimiento válida con fecha correcta")
    void deberiaCrearFechaNacimientoValidaConFechaCorrecta() {
        // Arrange
        LocalDate fecha = FECHA_VALIDA;
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.de(fecha);
        
        // Assert
        assertAll("FechaNacimiento creada correctamente",
            () -> assertNotNull(fechaNacimiento, "La FechaNacimiento no debe ser null"),
            () -> assertEquals(fecha, fechaNacimiento.valor(), "El valor debe coincidir"),
            () -> assertNotNull(fechaNacimiento.toString(), "ToString debe funcionar")
        );
    }
    
    @Test
    @DisplayName("Debería crear FechaNacimiento opcional cuando fecha es null")
    void deberiaCrearFechaNacimientoOpcionalCuandoFechaEsNull() {
        // Arrange
        LocalDate fecha = null;
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.opcional(fecha);
        
        // Assert
        assertNull(fechaNacimiento, 
            "FechaNacimiento opcional debe retornar null cuando la fecha es null");
    }
    
    @Test
    @DisplayName("Debería crear FechaNacimiento opcional cuando fecha es válida")
    void deberiaCrearFechaNacimientoOpcionalCuandoFechaEsValida() {
        // Arrange
        LocalDate fecha = FECHA_VALIDA;
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.opcional(fecha);
        
        // Assert
        assertAll("FechaNacimiento opcional creada correctamente",
            () -> assertNotNull(fechaNacimiento, "La FechaNacimiento no debe ser null"),
            () -> assertEquals(fecha, fechaNacimiento.valor(), "El valor debe coincidir")
        );
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN - REGLAS DE NEGOCIO
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando fecha de nacimiento es futura")
    void deberiaFallarCuandoFechaNacimientoEsFutura() {
        // Arrange
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        
        // Act & Assert
        FutureBirthDateException exception = assertThrows(FutureBirthDateException.class, () -> {
            FechaNacimiento.de(fechaFutura);
        });
        
        assertEquals(fechaFutura, exception.getBirthDate(), 
            "Debe capturar la fecha que causó el error");
    }
    
    @Test
    @DisplayName("Debería fallar cuando fecha indica edad mayor a 120 años")
    void deberiaFallarCuandoFechaIndicaEdadMayorA120Anos() {
        // Arrange
        LocalDate fechaMuyAntigua = FECHA_MUY_ANTIGUA;
        
        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            FechaNacimiento.de(fechaMuyAntigua);
        });
        
        assertTrue(exception.getMessage().contains("120"), 
            "El mensaje debe mencionar el límite de 120 años");
    }
    
    @Test
    @DisplayName("Debería aceptar fecha de hoy como válida")
    void deberiaAceptarFechaDeHoyComoValida() {
        // Arrange
        LocalDate fechaHoy = LocalDate.now();
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.de(fechaHoy);
        
        // Assert
        assertEquals(fechaHoy, fechaNacimiento.valor(), 
            "Debe aceptar la fecha de hoy como válida");
    }
    
    @Test
    @DisplayName("Debería aceptar fecha de ayer como válida")
    void deberiaAceptarFechaDeAyerComoValida() {
        // Arrange
        LocalDate fechaAyer = LocalDate.now().minusDays(1);
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.de(fechaAyer);
        
        // Assert
        assertEquals(fechaAyer, fechaNacimiento.valor(), 
            "Debe aceptar la fecha de ayer como válida");
    }
    
    @Test
    @DisplayName("Debería aceptar fecha antigua pero dentro del límite de 120 años")
    void deberiaAceptarFechaAntiguaPeroDetroDeLimite120Anos() {
        // Arrange
        LocalDate fechaLimite = LocalDate.now().minusYears(119); // Justo dentro del límite
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.de(fechaLimite);
        
        // Assert
        assertEquals(fechaLimite, fechaNacimiento.valor(), 
            "Debe aceptar fecha dentro del límite de 120 años");
    }
    
    // ========================================
    // TESTS DE IGUALDAD Y HASH CODE
    // ========================================
    
    @Test
    @DisplayName("Debería considerar iguales FechaNacimiento con mismo valor")
    void deberiaConsiderarIgualesFechaNacimientoConMismoValor() {
        // Arrange
        LocalDate fecha = FECHA_VALIDA;
        FechaNacimiento fecha1 = FechaNacimiento.de(fecha);
        FechaNacimiento fecha2 = FechaNacimiento.de(fecha);
        
        // Act & Assert
        assertAll("Igualdad basada en valor",
            () -> assertEquals(fecha1, fecha2, "FechaNacimiento con mismo valor deben ser iguales"),
            () -> assertEquals(fecha1.hashCode(), fecha2.hashCode(), 
                "Hash codes deben ser iguales para objetos iguales"),
            () -> assertEquals(fecha1.toString(), fecha2.toString(), 
                "ToString debe ser igual para objetos iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar diferentes FechaNacimiento con valores diferentes")
    void deberiaConsiderarDiferentesFechaNacimientoConValoresDiferentes() {
        // Arrange
        FechaNacimiento fecha1 = FechaNacimiento.de(FECHA_VALIDA);
        FechaNacimiento fecha2 = FechaNacimiento.de(FECHA_RECIENTE);
        
        // Act & Assert
        assertAll("Diferencia basada en valor",
            () -> assertNotEquals(fecha1, fecha2, "FechaNacimiento con valores diferentes deben ser diferentes"),
            () -> assertNotEquals(fecha1.valor(), fecha2.valor(), "Los valores deben ser diferentes")
        );
    }
    
    // ========================================
    // TESTS DE CASOS EDGE
    // ========================================
    
    @Test
    @DisplayName("Debería manejar correctamente años bisiestos")
    void deberiaManejarCorrectamenteAnosBisiestos() {
        // Arrange
        LocalDate fechaBisiesto = LocalDate.of(2000, 2, 29); // 29 de febrero en año bisiesto
        
        // Act
        FechaNacimiento fechaNacimiento = FechaNacimiento.de(fechaBisiesto);
        
        // Assert
        assertEquals(fechaBisiesto, fechaNacimiento.valor(), 
            "Debe manejar correctamente fechas en años bisiestos");
    }
    
    @Test
    @DisplayName("Debería manejar correctamente fechas en límites de meses")
    void deberiaManejarCorrectamenteFechasEnLimitesDeMeses() {
        // Arrange
        LocalDate primerDiaAno = LocalDate.of(1990, 1, 1);
        LocalDate ultimoDiaAno = LocalDate.of(1990, 12, 31);
        
        // Act
        FechaNacimiento fecha1 = FechaNacimiento.de(primerDiaAno);
        FechaNacimiento fecha2 = FechaNacimiento.de(ultimoDiaAno);
        
        // Assert
        assertAll("Fechas en límites de año",
            () -> assertEquals(primerDiaAno, fecha1.valor(), "Debe aceptar primer día del año"),
            () -> assertEquals(ultimoDiaAno, fecha2.valor(), "Debe aceptar último día del año")
        );
    }
    
    @Test
    @DisplayName("Debería manejar correctamente toString para fecha null")
    void deberiaManejarCorrectamenteToStringParaFechaNull() {
        // Arrange
        FechaNacimiento fechaNacimiento = FechaNacimiento.opcional(null);
        
        // Act & Assert
        assertNull(fechaNacimiento, 
            "FechaNacimiento opcional con null debe ser null");
    }
}
