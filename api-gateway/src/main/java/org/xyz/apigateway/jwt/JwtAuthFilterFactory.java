package org.xyz.apigateway.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@Component
public class JwtAuthFilterFactory {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public HandlerFilterFunction<ServerResponse, ServerResponse> jwtGatewayAuthFilter() {
        return (request, next) -> {
            var authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            System.out.println("Raw header: '" + authHeader + "'");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }

            var token = authHeader.substring(7);
            System.out.println("Token parts: " + token.split("\\.").length);
            System.out.println("Token: '" + token + "'");
            System.out.println("Token1: '" + secretKey + "'");

            try {
                var claims = Jwts
                        .parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                System.out.println("claims: " + claims.getSubject());
                var mutatedRequest = ServerRequest.from(request)
                        .header("X-Customer-Email", claims.getSubject())
                        .header("X-Customer-Roles", String.join(",", claims.get("roles", List.class)))
                        .build();

                System.out.println("mutatedRequest: " + mutatedRequest);
                return next.handle(mutatedRequest);
            } catch (ExpiredJwtException e) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body("token expired");
            } catch (JwtException e) {
                System.out.println("exception: " + e.getLocalizedMessage());
                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                        .body("invalid token");
            }

        };
    }


}
