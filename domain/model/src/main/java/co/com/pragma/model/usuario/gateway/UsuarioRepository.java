package co.com.pragma.model.usuario.gateway;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida (Gateway) para la persistencia de usuarios
 * 
 * Define el contrato requerido por HU1:
 * - Guardar usuario 
 * - Verificar unicidad de email
 * 
 * Siguiendo el principio de inversión de dependencias de SOLID,
 * el dominio define esta interfaz y la infraestructura la implementa.
 */
public interface UsuarioRepository {
    
    /**
     * Guarda un usuario en el sistema de persistencia
     * 
     * @param usuario Usuario a guardar
     * @return Mono con el usuario guardado (incluyendo ID si es nuevo)
     */
    Mono<Usuario> guardar(Usuario usuario);
    
    /**
     * Verifica si existe un usuario con el correo electrónico dado
     * 
     * @param correoElectronico Email a verificar
     * @return Mono con true si existe, false si no existe
     */
    Mono<Boolean> existePorCorreoElectronico(String correoElectronico);
}
