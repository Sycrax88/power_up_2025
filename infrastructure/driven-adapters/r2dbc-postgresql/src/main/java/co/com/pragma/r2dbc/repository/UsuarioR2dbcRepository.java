package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.UsuarioEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC para UsuarioEntity
 * 
 * Proporciona operaciones reactivas necesarias para HU1:
 * - Guardar usuario
 * - Verificar unicidad de email
 */
@Repository
public interface UsuarioR2dbcRepository extends ReactiveCrudRepository<UsuarioEntity, String> {
    
    /**
     * Verifica si existe un usuario con el email dado (case-insensitive)
     * 
     * @param correoElectronico Email a verificar
     * @return Mono con true si existe, false si no
     */
    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(correo_electronico) = LOWER(:correoElectronico)")
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
