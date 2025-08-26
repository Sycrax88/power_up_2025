package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioId - Tests de Value Object")
class UsuarioIdTest {
    
    // ✅ CONSTANTES PARA TESTS
    private static final String ID_VALIDO = "123e4567-e89b-12d3-a456-426614174000";
    
    // ========================================
    // TESTS DE CREACIÓN EXITOSA
    // ========================================
    
    @Test
    @DisplayName("Debería crear UsuarioId válido desde string")
    void deberiaCrearUsuarioIdValidoDesdeString() {
        // Arrange
        String id = ID_VALIDO;
        
        // Act
        UsuarioId usuarioId = UsuarioId.de(id);
        
        // Assert
        assertAll("UsuarioId creado correctamente",
            () -> assertNotNull(usuarioId, "El UsuarioId no debe ser null"),
            () -> assertEquals(id, usuarioId.valor(), "El valor debe coincidir"),
            () -> assertNotNull(usuarioId.toString(), "ToString debe funcionar")
        );
    }
    
    @Test
    @DisplayName("Debería generar UsuarioId único automáticamente")
    void deberiaGenerarUsuarioIdUnicoAutomaticamente() {
        // Arrange & Act
        UsuarioId id1 = UsuarioId.generar();
        UsuarioId id2 = UsuarioId.generar();
        
        // Assert
        assertAll("IDs únicos generados",
            () -> assertNotNull(id1, "El primer ID no debe ser null"),
            () -> assertNotNull(id2, "El segundo ID no debe ser null"),
            () -> assertNotNull(id1.valor(), "El valor del primer ID no debe ser null"),
            () -> assertNotNull(id2.valor(), "El valor del segundo ID no debe ser null"),
            () -> assertNotEquals(id1.valor(), id2.valor(), "Los IDs deben ser únicos"),
            () -> assertNotEquals(id1, id2, "Los objetos deben ser diferentes")
        );
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando ID es null")
    void deberiaFallarCuandoIdEsNull() {
        // Arrange
        String id = null;
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UsuarioId.de(id);
        });
        
        assertTrue(exception.getMessage().contains("nulo"), 
            "El mensaje debe indicar que el ID no puede ser nulo");
    }
    
    @Test
    @DisplayName("Debería fallar cuando ID está vacío")
    void deberiaFallarCuandoIdEstaVacio() {
        // Arrange
        String id = "";
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UsuarioId.de(id);
        });
        
        assertTrue(exception.getMessage().contains("vacío"), 
            "El mensaje debe indicar que el ID no puede estar vacío");
    }
    
    @Test
    @DisplayName("Debería fallar cuando ID contiene solo espacios")
    void deberiaFallarCuandoIdContieneSoloEspacios() {
        // Arrange
        String id = "   ";
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UsuarioId.de(id);
        });
        
        assertTrue(exception.getMessage().contains("vacío"), 
            "El mensaje debe indicar que el ID no puede estar vacío");
    }
    
    // ========================================
    // TESTS DE IGUALDAD Y HASH CODE
    // ========================================
    
    @Test
    @DisplayName("Debería considerar iguales UsuarioIds con mismo valor")
    void deberiaConsiderarIgualesUsuarioIdsConMismoValor() {
        // Arrange
        String valor = ID_VALIDO;
        UsuarioId id1 = UsuarioId.de(valor);
        UsuarioId id2 = UsuarioId.de(valor);
        
        // Act & Assert
        assertAll("Igualdad basada en valor",
            () -> assertEquals(id1, id2, "UsuarioIds con mismo valor deben ser iguales"),
            () -> assertEquals(id1.hashCode(), id2.hashCode(), 
                "Hash codes deben ser iguales para objetos iguales"),
            () -> assertEquals(id1.toString(), id2.toString(), 
                "ToString debe ser igual para objetos iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar diferentes UsuarioIds con valores diferentes")
    void deberiaConsiderarDiferentesUsuarioIdsConValoresDiferentes() {
        // Arrange
        UsuarioId id1 = UsuarioId.de("id-1");
        UsuarioId id2 = UsuarioId.de("id-2");
        
        // Act & Assert
        assertAll("Diferencia basada en valor",
            () -> assertNotEquals(id1, id2, "UsuarioIds con valores diferentes deben ser diferentes"),
            () -> assertNotEquals(id1.valor(), id2.valor(), "Los valores deben ser diferentes"),
            () -> assertNotEquals(id1.toString(), id2.toString(), "ToString debe ser diferente")
        );
    }
    
    // ========================================
    // TESTS DE NORMALIZACIÓN
    // ========================================
    
    @Test
    @DisplayName("Debería normalizar ID eliminando espacios")
    void deberiaNormalizarIdEliminandoEspacios() {
        // Arrange
        String idConEspacios = "  " + ID_VALIDO + "  ";
        
        // Act
        UsuarioId usuarioId = UsuarioId.de(idConEspacios);
        
        // Assert
        assertEquals(ID_VALIDO, usuarioId.valor(), 
            "Debe eliminar espacios al inicio y final");
    }
}
