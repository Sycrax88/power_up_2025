package co.com.pragma.api.usuario.service;

import co.com.pragma.model.usuario.gateway.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Servicio de validaciones específicas para usuarios
 * 
 * Responsabilidad única: validar reglas de negocio relacionadas con usuarios
 * en la capa de entrada (API REST).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioValidationService {
    
    private final UsuarioRepository usuarioRepository;
    
    /**
     * Valida que el email no esté registrado por otro usuario
     * 
     * @param correoElectronico Email a validar
     * @param traceId ID de trazabilidad
     * @return Mono vacío si es válido, error si ya existe
     */
    public Mono<Void> validarEmailUnico(String correoElectronico, String traceId) {
        return usuarioRepository.existePorCorreoElectronico(correoElectronico)
                .flatMap(existe -> {
                    if (existe) {
                        log.warn("[{}] Intento de registro con email duplicado: {}", 
                            traceId, correoElectronico);
                        return Mono.error(new RuntimeException(
                            "Ya existe un usuario registrado con el correo electrónico: " + 
                            correoElectronico
                        ));
                    }
                    return Mono.empty();
                });
    }
}
