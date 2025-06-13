package com.e_boutique.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API E-Boutique")
                        .version("1.0")
                        .description("Documentation de l’API de la boutique en ligne")
                        .contact(new Contact()
                                .name("TonNom")
                                .email("tonemail@example.com")
                        )
                );
    }
}
