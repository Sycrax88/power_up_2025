package co.com.pragma.r2dbc.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

/**
 * Configuración de R2DBC para PostgreSQL con optimizaciones de rendimiento
 * 
 * Configura pool de conexiones, transacciones y auditoría automática
 * siguiendo las mejores prácticas para aplicaciones reactivas.
 */
@Slf4j
@Configuration
@EnableR2dbcRepositories(basePackages = "co.com.pragma.r2dbc.repository")
@EnableTransactionManagement
public class R2dbcConfig extends AbstractR2dbcConfiguration {
    
    @Value("${spring.r2dbc.url:r2dbc:postgresql://localhost:5432/crediya}")
    private String databaseUrl;
    
    @Value("${spring.r2dbc.username:crediya_user}")
    private String username;
    
    @Value("${spring.r2dbc.password:crediya_pass}")
    private String password;
    
    @Value("${spring.r2dbc.pool.initial-size:10}")
    private int initialSize;
    
    @Value("${spring.r2dbc.pool.max-size:20}")
    private int maxSize;
    
    @Value("${spring.r2dbc.pool.max-idle-time:30m}")
    private Duration maxIdleTime;
    
    @Value("${spring.r2dbc.pool.validation-query:SELECT 1}")
    private String validationQuery;
    
    @Override
    @Bean
    public ConnectionPool connectionFactory() {
        log.info("Configurando R2DBC ConnectionPool para PostgreSQL");
        log.info("Database URL: {}", databaseUrl.replaceAll("password=[^&]*", "password=***"));
        
        PostgresqlConnectionConfiguration pgConfig = PostgresqlConnectionConfiguration.builder()
                .host(extractHost(databaseUrl))
                .port(extractPort(databaseUrl))
                .database(extractDatabase(databaseUrl))
                .username(username)
                .password(password)
                .schema("public")
                .build();
        
        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(pgConfig);
        
        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(initialSize)
                .maxSize(maxSize)
                .maxIdleTime(maxIdleTime)
                .validationQuery(validationQuery)
                .name("crediya-r2dbc-pool")
                .build();
        
        ConnectionPool pool = new ConnectionPool(poolConfig);
        
        log.info("R2DBC ConnectionPool configurado: initialSize={}, maxSize={}, maxIdleTime={}", 
            initialSize, maxSize, maxIdleTime);
        
        return pool;
    }
    
    private String extractHost(String url) {
        String[] parts = url.split("//")[1].split("/")[0].split(":");
        return parts[0];
    }
    
    private int extractPort(String url) {
        String[] parts = url.split("//")[1].split("/")[0].split(":");
        return parts.length > 1 ? Integer.parseInt(parts[1]) : 5432;
    }
    
    private String extractDatabase(String url) {
        String[] parts = url.split("//")[1].split("/");
        return parts.length > 1 ? parts[1].split("\\?")[0] : "postgres";
    }
}
