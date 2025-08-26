package co.com.pragma.api.usuario;

import co.com.pragma.api.dto.RegistrarUsuarioRequest;
import co.com.pragma.api.dto.UsuarioResponseDto;
import co.com.pragma.api.dto.ErrorResponseDto;
import co.com.pragma.api.usuario.mapper.UsuarioMapper;
import co.com.pragma.api.usuario.service.UsuarioValidationService;
import co.com.pragma.model.usuario.gateway.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Controlador REST para la gestión de usuarios en el sistema CrediYa
 * 
 * Implementa el puerto de entrada (driving adapter) de la arquitectura hexagonal
 * siguiendo las mejores prácticas de Spring WebFlux y OpenAPI 3.0
 * 
 * NOTA: Por ahora usa directamente el repositorio. En la siguiente fase
 * se integrará con la capa de casos de uso (Application Layer).
 * 
 * @author Sebastian Ospina Garcia
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(
    name = "Gestión de Usuarios", 
    description = "API REST para operaciones CRUD de usuarios del sistema CrediYa. " +
                  "Permite registrar nuevos usuarios con validaciones de negocio completas."
)
public class UsuarioController {
    
    // Constantes para logging y trazabilidad
    private static final String OPERATION_REGISTER = "REGISTER_USER";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    
    // Servicios especializados siguiendo principio de responsabilidad única
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioValidationService validationService;
    
    /**
     * Endpoint para registrar un nuevo usuario en el sistema
     * 
     * @param request Datos del usuario a registrar
     * @param exchange Contexto de la petición web
     * @return Usuario registrado con su ID generado
     */
    @PostMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
        summary = "Registrar nuevo usuario",
        description = """
            Registra un nuevo usuario en el sistema CrediYa con validaciones completas:
            
            **Validaciones aplicadas:**
            - Campos obligatorios: nombres, apellidos, correoElectronico, salarioBase
            - Formato de email válido (RFC 5322)
            - Unicidad del correo electrónico
            - Rango de salario: 0 - 15,000,000 COP
            - Fecha de nacimiento no futura y edad máxima 120 años
            
            **Proceso:**
            1. Validación de formato en capa de entrada
            2. Validación de reglas de negocio en dominio
            3. Verificación de unicidad de email
            4. Persistencia transaccional
            5. Logging de auditoría
            """,
        operationId = "registrarUsuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UsuarioResponseDto.class),
                examples = @ExampleObject(
                    name = "Usuario creado",
                    summary = "Ejemplo de respuesta exitosa",
                    value = """
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "nombres": "Juan Carlos",
                          "apellidos": "Pérez García",
                          "fechaNacimiento": "1990-05-15",
                          "direccion": "Calle 123 #45-67",
                          "telefono": "+57 300 123 4567",
                          "correoElectronico": "juan.perez@email.com",
                          "salarioBase": 2500000,
                          "fechaCreacion": "2024-08-24T18:30:00"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos - Errores de validación",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = {
                    @ExampleObject(
                        name = "Error de validación",
                        summary = "Campos obligatorios faltantes",
                        value = """
                            {
                              "timestamp": "2024-08-24T18:30:00",
                              "status": 400,
                              "error": "Validation Error",
                              "message": "Los nombres son obligatorios",
                              "code": "VALIDATION_ERROR",
                              "path": "/api/v1/usuarios",
                              "traceId": "abc123-def456"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Error de formato email",
                        summary = "Email con formato inválido",
                        value = """
                            {
                              "timestamp": "2024-08-24T18:30:00",
                              "status": 400,
                              "error": "Invalid Email Format",
                              "message": "El email 'invalid-email' tiene formato inválido: debe contener @",
                              "code": "INVALID_EMAIL_FORMAT",
                              "path": "/api/v1/usuarios",
                              "traceId": "abc123-def456"
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto - Correo electrónico ya registrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Email duplicado",
                    summary = "Usuario con email ya existe",
                    value = """
                        {
                          "timestamp": "2024-08-24T18:30:00",
                          "status": 409,
                          "error": "Duplicate Email",
                          "message": "Ya existe un usuario registrado con el correo electrónico: juan@email.com",
                          "code": "DUPLICATE_EMAIL",
                          "path": "/api/v1/usuarios",
                          "traceId": "abc123-def456"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Entidad no procesable - Error en reglas de negocio",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Regla de negocio violada",
                    summary = "Salario fuera de rango",
                    value = """
                        {
                          "timestamp": "2024-08-24T18:30:00",
                          "status": 422,
                          "error": "Business Rule Violation",
                          "message": "El salario base debe estar entre 0 y 15000000, pero se recibió: 20000000",
                          "code": "SALARY_OUT_OF_RANGE",
                          "path": "/api/v1/usuarios",
                          "traceId": "abc123-def456"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponseDto.class),
                examples = @ExampleObject(
                    name = "Error interno",
                    summary = "Error inesperado del servidor",
                    value = """
                        {
                          "timestamp": "2024-08-24T18:30:00",
                          "status": 500,
                          "error": "Internal Server Error",
                          "message": "Ha ocurrido un error interno en el servidor",
                          "code": "INTERNAL_ERROR",
                          "path": "/api/v1/usuarios",
                          "traceId": "abc123-def456"
                        }
                        """
                )
            )
        )
    })
    public Mono<ResponseEntity<UsuarioResponseDto>> registrarUsuario(
            @Parameter(
                description = "Datos del usuario a registrar",
                required = true,
                schema = @Schema(implementation = RegistrarUsuarioRequest.class)
            )
            @Valid @RequestBody RegistrarUsuarioRequest request,
            ServerWebExchange exchange) {
        
        // Generación de trace ID para trazabilidad
        String traceId = java.util.UUID.randomUUID().toString().substring(0, 8);
        String path = exchange.getRequest().getPath().value();
        
        return Mono.just(request)
                .doOnNext(req -> log.info(
                    "[{}] {} iniciado para correo: {}", 
                    traceId, OPERATION_REGISTER, req.getCorreoElectronico()
                ))
                // Normalizar datos de entrada
                .map(RegistrarUsuarioRequest::normalizar)
                // Validar que email no exista
                .flatMap(req -> validationService.validarEmailUnico(req.getCorreoElectronico(), traceId)
                    .then(Mono.just(req)))
                // Crear usuario usando factory method del dominio
                .map(usuarioMapper::toEntity)
                // Guardar en repositorio
                .flatMap(usuarioRepository::guardar)
                // Mapear a DTO de respuesta
                .map(usuarioMapper::toDto)
                // Crear ResponseEntity con headers
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED)
                    .header(TRACE_ID_HEADER, traceId)
                    .body(dto))
                .doOnSuccess(response -> log.info(
                    "[{}] {} completado exitosamente. ID: {}, Email: {}", 
                    traceId, OPERATION_REGISTER, 
                    response.getBody().getId(), 
                    response.getBody().getCorreoElectronico()
                ))
                .doOnError(error -> log.error(
                    "[{}] {} falló para correo: {}. Error: {}", 
                    traceId, OPERATION_REGISTER, 
                    request.getCorreoElectronico(), 
                    error.getMessage(), error
                ));
    }
    

}
