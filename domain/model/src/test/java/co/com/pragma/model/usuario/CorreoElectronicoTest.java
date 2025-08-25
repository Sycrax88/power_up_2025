package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CorreoElectronico - Tests de Value Object")
class CorreoElectronicoTest {
    
    // ✅ CONSTANTES PARA TESTS
    private static final String EMAIL_VALIDO = "juan.perez@email.com";
    private static final String EMAIL_MAYUSCULAS = "JUAN.PEREZ@EMAIL.COM";
    private static final String EMAIL_SIN_ARROBA = "juan.perez.email.com";
    private static final String EMAIL_EMPIEZA_ARROBA = "@juan.perez.email.com";
    private static final String EMAIL_TERMINA_ARROBA = "juan.perez.email.com@";
    private static final String EMAIL_CORTO = "a@b.c";
    private static final String EMAIL_LARGO = "a".repeat(150) + "@email.com"; // Excede límite
    
    // ========================================
    // TESTS DE CREACIÓN EXITOSA
    // ========================================
    
    @Test
    @DisplayName("Debería crear CorreoElectronico válido con formato correcto")
    void deberiaCrearCorreoElectronicoValidoConFormatoCorrecto() {
        // Arrange
        String email = EMAIL_VALIDO;
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(email);
        
        // Assert
        assertAll("CorreoElectronico creado correctamente",
            () -> assertNotNull(correo, "El CorreoElectronico no debe ser null"),
            () -> assertEquals(email, correo.valor(), "El valor debe coincidir"),
            () -> assertNotNull(correo.toString(), "ToString debe funcionar")
        );
    }
    
    @Test
    @DisplayName("Debería normalizar email a minúsculas")
    void deberiaNormalizarEmailAMinusculas() {
        // Arrange
        String emailMayusculas = EMAIL_MAYUSCULAS;
        String emailEsperado = emailMayusculas.toLowerCase();
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(emailMayusculas);
        
        // Assert
        assertEquals(emailEsperado, correo.valor(), 
            "Debe normalizar el email a minúsculas");
    }
    
    @Test
    @DisplayName("Debería eliminar espacios al inicio y final")
    void deberiaEliminarEspaciosAlInicioYFinal() {
        // Arrange
        String emailConEspacios = "  " + EMAIL_VALIDO + "  ";
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(emailConEspacios);
        
        // Assert
        assertEquals(EMAIL_VALIDO, correo.valor(), 
            "Debe eliminar espacios al inicio y final");
    }
    
    @Test
    @DisplayName("Debería aceptar email en límite mínimo de longitud")
    void deberiaAceptarEmailEnLimiteMinimoLongitud() {
        // Arrange
        String emailCorto = EMAIL_CORTO; // 5 caracteres: a@b.c
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(emailCorto);
        
        // Assert
        assertEquals(emailCorto, correo.valor(), 
            "Debe aceptar email en límite mínimo de longitud");
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN - CAMPOS OBLIGATORIOS
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando email es null")
    void deberiaFallarCuandoEmailEsNull() {
        // Arrange
        String email = null;
        
        // Act & Assert
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> {
            CorreoElectronico.de(email);
        });
        
        assertEquals("correoElectronico", exception.getFieldName(), 
            "Debe indicar que el campo correoElectronico es el problema");
    }
    
    @Test
    @DisplayName("Debería fallar cuando email está vacío")
    void deberiaFallarCuandoEmailEstaVacio() {
        // Arrange
        String email = "";
        
        // Act & Assert
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> {
            CorreoElectronico.de(email);
        });
        
        assertEquals("correoElectronico", exception.getFieldName(), 
            "Debe indicar que el campo correoElectronico es el problema");
    }
    
    @Test
    @DisplayName("Debería fallar cuando email contiene solo espacios")
    void deberiaFallarCuandoEmailContieneSoloEspacios() {
        // Arrange
        String email = "   ";
        
        // Act & Assert
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> {
            CorreoElectronico.de(email);
        });
        
        assertEquals("correoElectronico", exception.getFieldName(), 
            "Debe indicar que el campo correoElectronico es el problema");
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN - FORMATO
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando email es demasiado corto")
    void deberiaFallarCuandoEmailEsDemasisadoCorto() {
        // Arrange
        String emailCorto = "a@b"; // 3 caracteres, menos que el mínimo (5)
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            CorreoElectronico.de(emailCorto);
        });
        
        assertTrue(exception.getMessage().contains("corto"), 
            "El mensaje debe indicar que el email es demasiado corto");
    }
    
    @Test
    @DisplayName("Debería fallar cuando email es demasiado largo")
    void deberiaFallarCuandoEmailEsDemasisadoLargo() {
        // Arrange
        String emailLargo = EMAIL_LARGO;
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            CorreoElectronico.de(emailLargo);
        });
        
        assertTrue(exception.getMessage().contains("exceder"), 
            "El mensaje debe indicar que el email excede la longitud máxima");
    }
    
    @Test
    @DisplayName("Debería fallar cuando email no contiene @")
    void deberiaFallarCuandoEmailNoContieneArroba() {
        // Arrange
        String emailSinArroba = EMAIL_SIN_ARROBA;
        
        // Act & Assert
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            CorreoElectronico.de(emailSinArroba);
        });
        
        assertAll("Información del error de formato",
            () -> assertEquals(emailSinArroba, exception.getEmail(), "Debe capturar el email problemático"),
            () -> assertTrue(exception.getMessage().contains("@"), "Debe mencionar el símbolo @")
        );
    }
    
    @Test
    @DisplayName("Debería fallar cuando email empieza con @")
    void deberiaFallarCuandoEmailEmpiezaConArroba() {
        // Arrange
        String emailEmpiezaArroba = EMAIL_EMPIEZA_ARROBA;
        
        // Act & Assert
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            CorreoElectronico.de(emailEmpiezaArroba);
        });
        
        assertAll("Información del error de formato",
            () -> assertEquals(emailEmpiezaArroba, exception.getEmail(), "Debe capturar el email problemático"),
            () -> assertTrue(exception.getMessage().contains("empezar"), "Debe mencionar que no puede empezar con @")
        );
    }
    
    @Test
    @DisplayName("Debería fallar cuando email termina con @")
    void deberiaFallarCuandoEmailTerminaConArroba() {
        // Arrange
        String emailTerminaArroba = EMAIL_TERMINA_ARROBA;
        
        // Act & Assert
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            CorreoElectronico.de(emailTerminaArroba);
        });
        
        assertAll("Información del error de formato",
            () -> assertEquals(emailTerminaArroba, exception.getEmail(), "Debe capturar el email problemático"),
            () -> assertTrue(exception.getMessage().contains("terminar"), "Debe mencionar que no puede terminar con @")
        );
    }
    
    // ========================================
    // TESTS DE IGUALDAD Y HASH CODE
    // ========================================
    
    @Test
    @DisplayName("Debería considerar iguales CorreoElectronico con mismo valor")
    void deberiaConsiderarIgualesCorreoElectronicoConMismoValor() {
        // Arrange
        String email = EMAIL_VALIDO;
        CorreoElectronico correo1 = CorreoElectronico.de(email);
        CorreoElectronico correo2 = CorreoElectronico.de(email);
        
        // Act & Assert
        assertAll("Igualdad basada en valor",
            () -> assertEquals(correo1, correo2, "CorreoElectronico con mismo valor deben ser iguales"),
            () -> assertEquals(correo1.hashCode(), correo2.hashCode(), 
                "Hash codes deben ser iguales para objetos iguales"),
            () -> assertEquals(correo1.toString(), correo2.toString(), 
                "ToString debe ser igual para objetos iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar iguales emails normalizados")
    void deberiaConsiderarIgualesEmailsNormalizados() {
        // Arrange
        CorreoElectronico correo1 = CorreoElectronico.de("JUAN@EMAIL.COM");
        CorreoElectronico correo2 = CorreoElectronico.de("juan@email.com");
        CorreoElectronico correo3 = CorreoElectronico.de("  Juan@Email.Com  ");
        
        // Act & Assert
        assertAll("Igualdad después de normalización",
            () -> assertEquals(correo1, correo2, "Emails con diferentes casos deben ser iguales después de normalización"),
            () -> assertEquals(correo1, correo3, "Emails con espacios deben ser iguales después de normalización"),
            () -> assertEquals(correo2, correo3, "Todos los emails normalizados deben ser iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar diferentes CorreoElectronico con valores diferentes")
    void deberiaConsiderarDiferentesCorreoElectronicoConValoresDiferentes() {
        // Arrange
        CorreoElectronico correo1 = CorreoElectronico.de("juan@email.com");
        CorreoElectronico correo2 = CorreoElectronico.de("pedro@email.com");
        
        // Act & Assert
        assertAll("Diferencia basada en valor",
            () -> assertNotEquals(correo1, correo2, "CorreoElectronico con valores diferentes deben ser diferentes"),
            () -> assertNotEquals(correo1.valor(), correo2.valor(), "Los valores deben ser diferentes")
        );
    }
    
    // ========================================
    // TESTS DE CASOS EDGE
    // ========================================
    
    @Test
    @DisplayName("Debería manejar correctamente emails con caracteres especiales")
    void deberiaManejarCorrectamenteEmailsConCaracteresEspeciales() {
        // Arrange
        String emailEspecial = "juan.perez+test@email-domain.co.uk";
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(emailEspecial);
        
        // Assert
        assertEquals(emailEspecial, correo.valor(), 
            "Debe aceptar emails con caracteres especiales válidos");
    }
    
    @Test
    @DisplayName("Debería manejar correctamente emails con números")
    void deberiaManejarCorrectamenteEmailsConNumeros() {
        // Arrange
        String emailConNumeros = "usuario123@dominio456.com";
        
        // Act
        CorreoElectronico correo = CorreoElectronico.de(emailConNumeros);
        
        // Assert
        assertEquals(emailConNumeros, correo.valor(), 
            "Debe aceptar emails con números");
    }
}
