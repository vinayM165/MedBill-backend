package com.medbill.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI medBillOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MedBill API")
                        .description("Medical Store ERP Backend API Documentation")
                        .version("v1.0.0"));
    }
}
