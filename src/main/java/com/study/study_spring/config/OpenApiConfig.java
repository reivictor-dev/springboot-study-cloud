package com.study.study_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    
    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
            .info(new Info()
                .title("App study - SpringBoot, Kubernetes and Docker")
                .version("v1")
                .description("App study - SpringBoot, Kubernetes and Docker")
                .termsOfService("My Terms")
                .license(new License()
                    .name("APACHE 2.0")
                    .url("www.licensepage.com")));
    }
}
