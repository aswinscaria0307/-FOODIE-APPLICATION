package com.demo.apiGateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class ApiGateway {
    @Bean
    public RouteLocator routeUrl (RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(p->p.path("/api/v1/auth/**")
                        .uri("http://localhost:9099"))
                .route(p->p.path("/api/v1/userService/**")
                        .uri("http://localhost:8090"))
                .route(p->p.path("/api/v1/order/**")
                        .uri("http://localhost:8070"))
                .route(p->p.path("/api/v1/restaurant/**")
                        .uri("http://localhost:25500"))
                .route(p->p.path("/api/v1/cuisine/**")
                        .uri("http://localhost:25500"))
                .route(p->p.path("/api/v1/notification/**")
                        .uri("http://localhost:7171"))
                .route(p->p.path("/api/v1/payment/**")
                        .uri("http://localhost:1122"))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
