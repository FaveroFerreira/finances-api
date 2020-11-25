package com.deliver.finances.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.deliver.finances"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("finances-api")
                        .version("1.0.0")
                        .contact(new Contact("Guilherme Favero Ferreira",
                                "https://github.com/FaveroFerreira/finances-api",
                                ""))
                        .build())
                .tags(new Tag("bills", "Criação e listagem de contas."))
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"));
    }

}
