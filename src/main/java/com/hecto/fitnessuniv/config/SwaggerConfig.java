package com.hecto.fitnessuniv.config;

// http://localhost:8080/swagger-ui/index.html

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

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
                                .description(
                                        "API 테스트용 JWT : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMTc2Nzk1MTI5NzY3MjgzMjM3NzIiLCJzdWIiOiLtmantmITsp4QiLCJpYXQiOjE3MTgwNzkzOTQsImV4cCI6MTcxODA4Mjk5NH0.kSeh6mp1vpUUUMWKgb9cv8v60-4LNUxWIZgA68cTHUs"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT"))); // Refresh token URL 설정
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }
}
