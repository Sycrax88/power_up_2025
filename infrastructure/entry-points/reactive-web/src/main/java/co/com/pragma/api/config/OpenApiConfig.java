package co.com.pragma.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3.0 para documentación automática
 * 
 * Implementa mejores prácticas para documentación de APIs:
 * - Información completa del API
 * - Configuración de servidores por ambiente
 * - Tags organizacionales
 * - Contacto y licencia
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${spring.application.name:CrediYa API}")
    private String applicationName;
    
    @Value("${app.version:1.0.0}")
    private String version;
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(buildApiInfo())
                .servers(buildServers())
                .tags(buildTags());
    }
    
    private Info buildApiInfo() {
        return new Info()
                .title("CrediYa - API de Gestión de Usuarios")
                .description("""
                    **API REST para la gestión de usuarios del sistema CrediYa**
                    
                    Esta API implementa arquitectura hexagonal con Spring WebFlux,
                    proporcionando endpoints reactivos para:
                    
                    - Registro de usuarios con validaciones de negocio
                    - Validación de correos electrónicos únicos  
                    - Manejo de errores específicos y trazabilidad
                    - Logging estructurado para observabilidad
                    
                    **Tecnologías:**
                    - Spring Boot 3.x con WebFlux (Reactivo)
                    - Bean Validation (JSR-303)
                    - OpenAPI 3.0 con Swagger UI
                    - PostgreSQL con R2DBC
                    
                    **Arquitectura:**
                    - Clean Architecture / Hexagonal Architecture
                    - Domain-Driven Design (DDD)
                    - Principios SOLID aplicados
                    """)
                .version(version)
                .contact(buildContact())
                .license(buildLicense());
    }
    
    private Contact buildContact() {
        return new Contact()
                .name("Equipo de Desarrollo CrediYa")
                .email("desarrollo@crediya.com")
                .url("https://crediya.com");
    }
    
    private License buildLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }
    
    private List<Server> buildServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Servidor Local")
        );
    }
    
    private List<Tag> buildTags() {
        return List.of(
                new Tag()
                        .name("Gestión de Usuarios")
                        .description("Operaciones para el manejo de usuarios del sistema")
        );
    }
}
