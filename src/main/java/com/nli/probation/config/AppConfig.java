package com.nli.probation.config;

import com.nli.probation.resolver.RequestPaginationResolver;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

/**
 * Application configuration
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Add pagination resolver
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestPaginationResolver());
    }

    /**
     * Config api information in swagger
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Team work management API",
                "API for TWM Back end",
                "1.0",
                "Terms of service",
                new Contact("TMW", "www.TMW.com", "minhtuanqn320@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    /**
     * Config for swagger
     * @return Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * config for model mapper
     * @return Model mapper
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        return modelMapper;
    }

}
