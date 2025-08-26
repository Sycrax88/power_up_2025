package co.com.pragma.api.usuario.mapper;

import co.com.pragma.api.dto.RegistrarUsuarioRequest;
import co.com.pragma.api.dto.UsuarioResponseDto;
import co.com.pragma.model.usuario.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversiones entre DTOs de API y entidades de dominio
 * 
 * Responsabilidad única: transformar datos entre capas de presentación y dominio
 * siguiendo el principio de separación de responsabilidades.
 */
@Component
public class UsuarioMapper {
    
    /**
     * Convierte DTO de request a entidad de dominio Usuario
     * 
     * @param request DTO con datos de entrada
     * @return Usuario entidad de dominio
     */
    public Usuario toEntity(RegistrarUsuarioRequest request) {
        return Usuario.crearNuevo(
                request.getNombres(),
                request.getApellidos(),
                request.getFechaNacimiento(),
                request.getDireccion(),
                request.getTelefono(),
                request.getCorreoElectronico(),
                request.getSalarioBase()
        );
    }
    
    /**
     * Convierte entidad de dominio Usuario a DTO de respuesta
     * 
     * @param usuario Entidad de dominio
     * @return DTO para respuesta API
     */
    public UsuarioResponseDto toDto(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId().valor())
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
    }
}
