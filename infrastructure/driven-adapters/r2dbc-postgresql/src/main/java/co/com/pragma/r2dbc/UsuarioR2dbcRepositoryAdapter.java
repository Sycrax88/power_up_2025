package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.UsuarioId;
import co.com.pragma.model.usuario.gateway.UsuarioRepository;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.mapper.UsuarioEntityMapper;
import co.com.pragma.r2dbc.repository.UsuarioR2dbcRepository;
import co.com.pragma.r2dbc.exception.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Adaptador R2DBC para persistencia de Usuario 
 * 
 * Implementa el puerto de salida UsuarioRepository definido en el dominio,
 * proporcionando persistencia reactiva con PostgreSQL y manejo robusto de errores.
 * 
 * Siguiendo estándares de Bancolombia: nombre específico de tecnología (R2DBC)
 * 
 * @author Equipo de Desarrollo CrediYa
 * @version 1.0
 * @since 2024
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioR2dbcRepositoryAdapter implements UsuarioRepository {
    
    // Constantes para configuración y logging
    private static final String OPERATION_SAVE = "SAVE_USER";
    private static final String OPERATION_EXISTS_BY_EMAIL = "EXISTS_USER_BY_EMAIL";
    
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration RETRY_DELAY = Duration.ofMillis(100);
    
    private final UsuarioR2dbcRepository usuarioR2dbcRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;
    
    /**
     * Guarda un usuario en la base de datos con manejo transaccional
     * 
     * @param usuario Usuario del dominio a persistir
     * @return Mono con el usuario persistido
     */
    @Override
    @Transactional
    public Mono<Usuario> guardar(Usuario usuario) {
        String email = usuario.getCorreoElectronico().valor();
        String traceId = generateTraceId();
        
        return Mono.just(usuario)
                .doOnNext(u -> log.info("[{}] {} iniciado para email: {}", 
                    traceId, OPERATION_SAVE, email))
                .map(usuarioEntityMapper::toEntity)
                .flatMap(this::persistirConReintento)
                .map(usuarioEntityMapper::toDomain)
                .doOnSuccess(savedUser -> log.info(
                    "[{}] {} completado exitosamente. ID: {}, Email: {}", 
                    traceId, OPERATION_SAVE, savedUser.getId().valor(), email))
                .doOnError(error -> log.error(
                    "[{}] {} falló para email: {}. Error: {}", 
                    traceId, OPERATION_SAVE, email, error.getMessage(), error))
                .onErrorMap(this::mapearExcepcionPersistencia);
    }
    
    /**
     * Verifica si existe un usuario con el email dado
     * 
     * @param correoElectronico Email a verificar
     * @return Mono con true si existe, false si no
     */
    @Override
    public Mono<Boolean> existePorCorreoElectronico(String correoElectronico) {
        String traceId = generateTraceId();
        
        return Mono.just(correoElectronico)
                .doOnNext(email -> log.debug("[{}] {} iniciado para: {}", 
                    traceId, OPERATION_EXISTS_BY_EMAIL, email))
                .flatMap(usuarioR2dbcRepository::existsByCorreoElectronico)
                .doOnNext(exists -> log.debug("[{}] {} - Resultado: {} para email: {}", 
                    traceId, OPERATION_EXISTS_BY_EMAIL, exists, correoElectronico))
                .onErrorMap(this::mapearExcepcionConsulta);
    }
    
    // Métodos privados de soporte
    
    /**
     * Persiste la entidad con reintentos automáticos en caso de fallos transitorios
     */
    private Mono<UsuarioEntity> persistirConReintento(UsuarioEntity entity) {
        return usuarioR2dbcRepository.save(entity)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, RETRY_DELAY)
                    .filter(this::esErrorTransitorio)
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Reintentando persistencia. Intento: {}", 
                            retrySignal.totalRetries() + 1)));
    }
    
    /**
     * Determina si un error es transitorio y merece reintento
     */
    private boolean esErrorTransitorio(Throwable error) {
        if (error instanceof DataAccessException) {
            // No reintentar en caso de violación de constraints (datos duplicados)
            return !(error instanceof DuplicateKeyException);
        }
        return false;
    }
    
    /**
     * Mapea excepciones de persistencia a excepciones de dominio
     */
    private Throwable mapearExcepcionPersistencia(Throwable error) {
        if (error instanceof DuplicateKeyException) {
            log.warn("Intento de crear usuario con email duplicado: {}", error.getMessage());
            return new PersistenceException("Email ya registrado en el sistema", error);
        }
        
        if (error instanceof DataAccessException) {
            log.error("Error de acceso a datos durante persistencia: {}", error.getMessage(), error);
            return new PersistenceException("Error al acceder a la base de datos", error);
        }
        
        log.error("Error no controlado durante persistencia: {}", error.getMessage(), error);
        return new PersistenceException("Error interno de persistencia", error);
    }
    
    /**
     * Mapea excepciones de consulta a excepciones de dominio
     */
    private Throwable mapearExcepcionConsulta(Throwable error) {
        if (error instanceof DataAccessException) {
            log.error("Error de acceso a datos durante consulta: {}", error.getMessage(), error);
            return new PersistenceException("Error al consultar la base de datos", error);
        }
        
        log.error("Error no controlado durante consulta: {}", error.getMessage(), error);
        return new PersistenceException("Error interno de consulta", error);
    }
    
    /**
     * Genera un trace ID para trazabilidad
     */
    private String generateTraceId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
