package co.com.pragma.model.usuario;

import co.com.pragma.model.common.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Usuario - Tests de Dominio")
class UsuarioTest {
    
    private static final String NOMBRES_VALIDOS = "Juan Carlos";
    private static final String APELLIDOS_VALIDOS = "Pérez García";
    private static final String EMAIL_VALIDO = "juan.perez@email.com";
    private static final BigDecimal SALARIO_VALIDO = new BigDecimal("2500000");
    private static final LocalDate FECHA_NACIMIENTO_VALIDA = LocalDate.of(1990, 5, 15);
    private static final String DIRECCION_VALIDA = "Calle 123 #45-67";
    private static final String TELEFONO_VALIDO = "+57 300 123 4567";
    
    // ========================================
    // TESTS DE CREACIÓN EXITOSA
    // ========================================
    
    @Test
    @DisplayName("Debería crear usuario válido con solo campos obligatorios")
    void deberiaCrearUsuarioValidoConSoloCamposObligatorios() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act
        Usuario usuario = Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        
        // Assert
        assertAll("Usuario creado correctamente",
            () -> assertNotNull(usuario, "El usuario no debe ser null"),
            () -> assertNotNull(usuario.getId(), "El ID debe ser generado automáticamente"),
            () -> assertEquals(nombres, usuario.getNombres(), "Los nombres deben coincidir"),
            () -> assertEquals(apellidos, usuario.getApellidos(), "Los apellidos deben coincidir"),
            () -> assertEquals(correo, usuario.getCorreoElectronico().valor(), "El correo debe coincidir"),
            () -> assertEquals(salario, usuario.getSalarioBase().valor(), "El salario debe coincidir"),
            () -> assertNull(usuario.getFechaNacimiento(), "La fecha de nacimiento debe ser null"),
            () -> assertNull(usuario.getDireccion(), "La dirección debe ser null"),
            () -> assertNull(usuario.getTelefono(), "El teléfono debe ser null"),
            () -> assertNotNull(usuario.getFechaCreacion(), "La fecha de creación debe ser generada"),
            () -> assertNull(usuario.getFechaActualizacion(), "La fecha de actualización debe ser null al crear")
        );
    }
    
    @Test
    @DisplayName("Debería crear usuario válido con todos los campos")
    void deberiaCrearUsuarioValidoConTodosLosCampos() {
        // Arrange
        String nombres = "María Elena";
        String apellidos = "González López";
        LocalDate fechaNacimiento = FECHA_NACIMIENTO_VALIDA;
        String direccion = DIRECCION_VALIDA;
        String telefono = TELEFONO_VALIDO;
        String correo = "maria.gonzalez@email.com";
        BigDecimal salario = new BigDecimal("6000000");
        
        // Act
        Usuario usuario = Usuario.crearNuevo(nombres, apellidos, fechaNacimiento, direccion, telefono, correo, salario);
        
        // Assert
        assertAll("Usuario completo creado correctamente",
            () -> assertNotNull(usuario, "El usuario no debe ser null"),
            () -> assertEquals(nombres, usuario.getNombres()),
            () -> assertEquals(apellidos, usuario.getApellidos()),
            () -> assertEquals(fechaNacimiento, usuario.getFechaNacimiento().valor()),
            () -> assertEquals(direccion, usuario.getDireccion()),
            () -> assertEquals(telefono, usuario.getTelefono()),
            () -> assertEquals(correo, usuario.getCorreoElectronico().valor()),
            () -> assertEquals(salario, usuario.getSalarioBase().valor())
        );
    }
    
    @Test
    @DisplayName("Debería generar ID único para cada usuario")
    void deberiaGenerarIdUnicoParaCadaUsuario() {
        // Arrange & Act
        Usuario usuario1 = Usuario.crearNuevo(NOMBRES_VALIDOS, APELLIDOS_VALIDOS, null, null, null, EMAIL_VALIDO, SALARIO_VALIDO);
        Usuario usuario2 = Usuario.crearNuevo(NOMBRES_VALIDOS, APELLIDOS_VALIDOS, null, null, null, "otro@email.com", SALARIO_VALIDO);
        
        // Assert
        assertNotEquals(usuario1.getId().valor(), usuario2.getId().valor(), 
            "Cada usuario debe tener un ID único");
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN - CAMPOS OBLIGATORIOS
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando nombres es null")
    void deberiaFallarCuandoNombresEsNull() {
        // Arrange
        String nombres = null;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("nombres"), 
            "El mensaje debe indicar que los nombres son obligatorios");
    }
    
    @Test
    @DisplayName("Debería fallar cuando nombres está vacío")
    void deberiaFallarCuandoNombresEstaVacio() {
        // Arrange
        String nombres = "";
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertEquals("nombres", exception.getFieldName(), 
            "Debe indicar que el campo 'nombres' es el problema");
    }
    
    @Test
    @DisplayName("Debería fallar cuando apellidos es null")
    void deberiaFallarCuandoApellidosEsNull() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = null;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("apellidos"), 
            "El mensaje debe indicar que los apellidos son obligatorios");
    }
    
    @Test
    @DisplayName("Debería fallar cuando correo electrónico es null")
    void deberiaFallarCuandoCorreoElectronicoEsNull() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = null;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("correo"), 
            "El mensaje debe indicar que el correo electrónico es obligatorio");
    }
    
    @Test
    @DisplayName("Debería fallar cuando salario base es null")
    void deberiaFallarCuandoSalarioBaseEsNull() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = null;
        
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("salario"), 
            "El mensaje debe indicar que el salario base es obligatorio");
    }
    
    // ========================================
    // TESTS DE VALIDACIÓN - FORMATO
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando nombres es demasiado corto")
    void deberiaFallarCuandoNombresEsDemasisadoCorto() {
        // Arrange
        String nombres = "A"; // Solo 1 carácter
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("entre 2 y 100"), 
            "Debe indicar el rango válido de caracteres");
    }
    
    @Test
    @DisplayName("Debería fallar cuando nombres contiene caracteres inválidos")
    void deberiaFallarCuandoNombresContieneCaracteresInvalidos() {
        // Arrange
        String nombres = "Juan123"; // Contiene números
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertTrue(exception.getMessage().contains("letras y espacios"), 
            "Debe indicar que solo se permiten letras y espacios");
    }
    
    @Test
    @DisplayName("Debería fallar cuando correo electrónico no contiene @")
    void deberiaFallarCuandoCorreoElectronicoNoContieneArroba() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = "correo-sin-arroba.com";
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salario);
        });
        
        assertEquals(correo, exception.getEmail(), 
            "Debe capturar el email que causó el error");
    }
    
    @Test
    @DisplayName("Debería fallar cuando fecha de nacimiento es futura")
    void deberiaFallarCuandoFechaNacimientoEsFutura() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        
        // Act & Assert
        FutureBirthDateException exception = assertThrows(FutureBirthDateException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, fechaFutura, null, null, correo, salario);
        });
        
        assertEquals(fechaFutura, exception.getBirthDate(), 
            "Debe capturar la fecha que causó el error");
    }
    
    // ========================================
    // TESTS DE REGLAS DE NEGOCIO
    // ========================================
    
    @Test
    @DisplayName("Debería fallar cuando salario excede el rango máximo")
    void deberiaFallarCuandoSalarioExcedeRangoMaximo() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salarioAlto = new BigDecimal("20000000"); // Fuera del rango máximo
        
        // Act & Assert
        SalaryOutOfRangeException exception = assertThrows(SalaryOutOfRangeException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salarioAlto);
        });
        
        assertAll("Información del error de salario",
            () -> assertEquals(salarioAlto, exception.getSalary()),
            () -> assertEquals(SalarioBase.SALARIO_MINIMO_CREDITO, exception.getMinSalary()),
            () -> assertEquals(SalarioBase.SALARIO_MAXIMO_CREDITO, exception.getMaxSalary())
        );
    }
    
    @Test
    @DisplayName("Debería fallar cuando salario es negativo")
    void deberiaFallarCuandoSalarioEsNegativo() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salarioNegativo = new BigDecimal("-1000");
        
        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salarioNegativo);
        });
        
        assertTrue(exception.getMessage().contains("negativo"), 
            "Debe indicar que el salario no puede ser negativo");
    }
    
    @Test
    @DisplayName("Debería aceptar salario en el límite mínimo válido")
    void deberiaAceptarSalarioEnLimiteMinimoValido() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salarioMinimo = SalarioBase.SALARIO_MINIMO_CREDITO;
        
        // Act
        Usuario usuario = Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salarioMinimo);
        
        // Assert
        assertEquals(salarioMinimo, usuario.getSalarioBase().valor(), 
            "Debe aceptar el salario mínimo válido");
    }
    
    @Test
    @DisplayName("Debería aceptar salario en el límite máximo válido")
    void deberiaAceptarSalarioEnLimiteMaximoValido() {
        // Arrange
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        String correo = EMAIL_VALIDO;
        BigDecimal salarioMaximo = SalarioBase.SALARIO_MAXIMO_CREDITO;
        
        // Act
        Usuario usuario = Usuario.crearNuevo(nombres, apellidos, null, null, null, correo, salarioMaximo);
        
        // Assert
        assertEquals(salarioMaximo, usuario.getSalarioBase().valor(), 
            "Debe aceptar el salario máximo válido");
    }
    
    // ========================================
    // TESTS DE RECONSTRUCCIÓN DESDE PERSISTENCIA
    // ========================================
    
    @Test
    @DisplayName("Debería reconstruir usuario desde datos de persistencia")
    void deberiaReconstruirUsuarioDesdeDatosPersistencia() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        String nombres = NOMBRES_VALIDOS;
        String apellidos = APELLIDOS_VALIDOS;
        LocalDate fechaNacimiento = FECHA_NACIMIENTO_VALIDA;
        String direccion = DIRECCION_VALIDA;
        String telefono = TELEFONO_VALIDO;
        String correo = EMAIL_VALIDO;
        BigDecimal salario = SALARIO_VALIDO;
        LocalDateTime fechaCreacion = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaActualizacion = LocalDateTime.now();
        
        // Act
        Usuario usuario = Usuario.reconstruir(id, nombres, apellidos, fechaNacimiento, 
            direccion, telefono, correo, salario, fechaCreacion, fechaActualizacion);
        
        // Assert
        assertAll("Usuario reconstruido correctamente",
            () -> assertEquals(id, usuario.getId().valor()),
            () -> assertEquals(nombres, usuario.getNombres()),
            () -> assertEquals(apellidos, usuario.getApellidos()),
            () -> assertEquals(fechaNacimiento, usuario.getFechaNacimiento().valor()),
            () -> assertEquals(direccion, usuario.getDireccion()),
            () -> assertEquals(telefono, usuario.getTelefono()),
            () -> assertEquals(correo, usuario.getCorreoElectronico().valor()),
            () -> assertEquals(salario, usuario.getSalarioBase().valor()),
            () -> assertEquals(fechaCreacion, usuario.getFechaCreacion()),
            () -> assertEquals(fechaActualizacion, usuario.getFechaActualizacion())
        );
    }
    
    // ========================================
    // TESTS DE IGUALDAD Y HASH CODE
    // ========================================
    
    @Test
    @DisplayName("Debería considerar iguales usuarios con mismo ID")
    void deberiaConsiderarIgualesUsuariosConMismoId() {
        // Arrange
        String id = "123e4567-e89b-12d3-a456-426614174000";
        Usuario usuario1 = Usuario.reconstruir(id, NOMBRES_VALIDOS, APELLIDOS_VALIDOS, 
            null, null, null, EMAIL_VALIDO, SALARIO_VALIDO, null, null);
        Usuario usuario2 = Usuario.reconstruir(id, "Otros Nombres", "Otros Apellidos", 
            null, null, null, "otro@email.com", new BigDecimal("5000000"), null, null);
        
        // Act & Assert
        assertAll("Igualdad basada en ID",
            () -> assertEquals(usuario1, usuario2, "Usuarios con mismo ID deben ser iguales"),
            () -> assertEquals(usuario1.hashCode(), usuario2.hashCode(), 
                "Hash codes deben ser iguales para objetos iguales")
        );
    }
    
    @Test
    @DisplayName("Debería considerar diferentes usuarios con IDs diferentes")
    void deberiaConsiderarDiferentesUsuariosConIdsDiferentes() {
        // Arrange
        Usuario usuario1 = Usuario.reconstruir("id-1", NOMBRES_VALIDOS, APELLIDOS_VALIDOS, 
            null, null, null, EMAIL_VALIDO, SALARIO_VALIDO, null, null);
        Usuario usuario2 = Usuario.reconstruir("id-2", NOMBRES_VALIDOS, APELLIDOS_VALIDOS, 
            null, null, null, EMAIL_VALIDO, SALARIO_VALIDO, null, null);
        
        // Act & Assert
        assertNotEquals(usuario1, usuario2, 
            "Usuarios con IDs diferentes deben ser diferentes");
    }
    
    // ========================================
    // TESTS DE CAMPOS OPCIONALES
    // ========================================
    
    @Test
    @DisplayName("Debería manejar correctamente campos opcionales nulos")
    void deberiaManejarCorrectamenteCamposOpcionalesNulos() {
        // Arrange & Act
        Usuario usuario = Usuario.crearNuevo(NOMBRES_VALIDOS, APELLIDOS_VALIDOS, 
            null, null, null, EMAIL_VALIDO, SALARIO_VALIDO);
        
        // Assert
        assertAll("Campos opcionales nulos",
            () -> assertNull(usuario.getFechaNacimiento(), "Fecha de nacimiento puede ser null"),
            () -> assertNull(usuario.getDireccion(), "Dirección puede ser null"),
            () -> assertNull(usuario.getTelefono(), "Teléfono puede ser null")
        );
    }
    
    @Test
    @DisplayName("Debería normalizar campos opcionales vacíos a null")
    void deberiaNormalizarCamposOpcionalesVaciosANull() {
        // Arrange & Act
        Usuario usuario = Usuario.crearNuevo(NOMBRES_VALIDOS, APELLIDOS_VALIDOS, 
            null, "", "   ", EMAIL_VALIDO, SALARIO_VALIDO);
        
        // Assert
        assertAll("Campos vacíos normalizados a null",
            () -> assertNull(usuario.getDireccion(), "Dirección vacía debe ser null"),
            () -> assertNull(usuario.getTelefono(), "Teléfono con espacios debe ser null")
        );
    }
}