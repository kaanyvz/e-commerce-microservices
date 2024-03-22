package com.ky.apigateway.config;

import com.ky.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ApiGatewayConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route("userService", r -> r.path("/v1/auth/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://userService"))
                .route("userService", r -> r.path("/v1/user/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://userService"))
                .route("productService", r ->r.path("/v1/products/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://productService"))
                .route("productService", r ->r.path("/v1/categories/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://productService"))
                .route("productService", r ->r.path("/v1/comments/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://productService"))
                .route("orderService", r ->r.path("/v1/orders/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://oderService"))
                .build();
    }
}
