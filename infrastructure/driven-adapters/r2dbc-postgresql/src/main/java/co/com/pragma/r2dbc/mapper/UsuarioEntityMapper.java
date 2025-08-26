package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entity.UsuarioEntity;
import co.com.pragma.r2dbc.exception.MappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper bidireccional entre Usuario (dominio) y UsuarioEntity (persistencia)
 * 
 * Implementa conversiones seguras con validaciones y manejo de errores específicos
 * para mantener la integridad entre las capas de dominio e infraestructura.
 */
@Slf4j
@Component
public class UsuarioEntityMapper {
    
    /**
     * Convierte de Usuario (dominio) a UsuarioEntity (persistencia)
     * 
     * @param usuario Usuario del dominio
     * @return UsuarioEntity para persistencia
     * @throws MappingException si hay errores en la conversión
     */
    public UsuarioEntity toEntity(Usuario usuario) {
        try {
            Objects.requireNonNull(usuario, "Usuario no puede ser nulo para mapear a entidad");
            
            log.debug("Mapeando Usuario a UsuarioEntity: {}", usuario.getId() != null ? usuario.getId().valor() : "nuevo");
            
            UsuarioEntity entity = UsuarioEntity.builder()
                    .id(usuario.getId() != null ? usuario.getId().valor() : null)
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .fechaNacimiento(usuario.getFechaNacimiento() != null ? 
                        usuario.getFechaNacimiento().valor() : null)
                    .direccion(usuario.getDireccion())
                    .telefono(usuario.getTelefono())
                    .correoElectronico(usuario.getCorreoElectronico().valor())
                    .salarioBase(usuario.getSalarioBase().valor())
                    .fechaCreacion(usuario.getFechaCreacion())
                    .build();
            
            // Validación post-mapeo
            if (!entity.esValidaParaPersistencia()) {
                throw new MappingException("Entidad resultante no es válida para persistencia: " + 
                    entity.getResumenParaLog());
            }
            
            log.debug("Mapeo exitoso a UsuarioEntity: {}", entity.getResumenParaLog());
            return entity;
            
        } catch (Exception e) {
            log.error("Error mapeando Usuario a UsuarioEntity: {}", e.getMessage(), e);
            throw new MappingException("Error en mapeo de Usuario a UsuarioEntity", e);
        }
    }
    
    /**
     * Convierte de UsuarioEntity (persistencia) a Usuario (dominio)
     * 
     * @param entity UsuarioEntity de persistencia
     * @return Usuario del dominio
     * @throws MappingException si hay errores en la conversión
     */
    public Usuario toDomain(UsuarioEntity entity) {
        try {
            Objects.requireNonNull(entity, "UsuarioEntity no puede ser nulo para mapear a dominio");
            Objects.requireNonNull(entity.getId(), "ID de UsuarioEntity no puede ser nulo");
            
            log.debug("Mapeando UsuarioEntity a Usuario: {}", entity.getResumenParaLog());
            
            Usuario usuario = Usuario.reconstruir(
                    entity.getId(),
                    entity.getNombres(),
                    entity.getApellidos(),
                    entity.getFechaNacimiento(),
                    entity.getDireccion(),
                    entity.getTelefono(),
                    entity.getCorreoElectronico(),
                    entity.getSalarioBase(),
                    entity.getFechaCreacion(),
                    null // fechaActualizacion no requerida por HU1
            );
            
            log.debug("Mapeo exitoso a Usuario: {}", usuario.getId().valor());
            return usuario;
            
        } catch (Exception e) {
            log.error("Error mapeando UsuarioEntity a Usuario: {}", e.getMessage(), e);
            throw new MappingException("Error en mapeo de UsuarioEntity a Usuario", e);
        }
    }
    

}
