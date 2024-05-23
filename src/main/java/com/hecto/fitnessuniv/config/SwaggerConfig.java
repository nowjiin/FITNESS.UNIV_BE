package com.hecto.fitnessuniv.config;

// http://localhost:8080/swagger-ui/index.html

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    /*
     * OpenAPI bean 구성
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("FITNESS UNIV API")
                                .version("1.0")
                                .description("API documentation")); // Refresh token URL 설정
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }
}
