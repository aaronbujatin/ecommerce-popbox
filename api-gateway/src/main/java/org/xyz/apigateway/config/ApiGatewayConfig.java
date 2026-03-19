package org.xyz.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.xyz.apigateway.jwt.JwtAuthFilterFactory;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.host;
import static org.springframework.web.servlet.function.RequestPredicates.GET;

@RequiredArgsConstructor
@Configuration
public class ApiGatewayConfig {

    private final JwtAuthFilterFactory jwtAuthFilterFactory;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("product-svc")
               .path("/api/v1/products/**", builder -> builder
                        .GET("", http())
                        .POST("", http())
                        .PUT("/{id}", http())
                        .DELETE("/{id}", http())
                   )
                .filter(jwtAuthFilterFactory.jwtGatewayAuthFilter())
                .before(uri("http://localhost:8081")).build()
               .and(
                    route("auth-svc")
                    .path("/api/v1/auth/**", builder -> builder
                            .GET("", http())
                            .POST("", http())
                            .PUT("/{id}", http())
                            .DELETE("/{id}", http())
                    ).before(uri("http://localhost:8085")).build()
                );
    }

}
