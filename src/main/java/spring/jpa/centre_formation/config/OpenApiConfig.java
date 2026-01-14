package spring.jpa.centre_formation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        Server devServer = new Server()
                .url("http://localhost:8080")
                .description("Development server");

        Contact contact = new Contact()
                .name("Centre de Formation")
                .email("contact@formation.tn");

        License license = new License()
                .name("Educational Use Only")
                .url("https://www.formation.tn");

        Info info = new Info()
                .title("API Centre de Formation")
                .version("1.0.0")
                .description("""
                        REST API for managing a training center.
                        This API allows managing students, trainers, courses,
                        enrollments, grades, and sessions.
                        """)
                .contact(contact)
                .license(license);

        // 🔐 Security Scheme (JWT Bearer)
        SecurityScheme bearerAuth = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                // Apply JWT globally
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerAuth));
    }

    /**
     * Ignore Hibernate lazy-loading properties in Swagger models
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}
