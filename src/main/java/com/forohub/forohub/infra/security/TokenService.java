package com.forohub.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.forohub.forohub.domain.usuario.Usuario;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationInMillis;

    public String generarToken(Usuario usuario) {
        var algoritmo = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("forohub-api")
                .withSubject(usuario.getLogin())
                .withExpiresAt(Instant.now().plusMillis(expirationInMillis))
                .sign(algoritmo);
    }

    public String getSubject(String token) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("forohub-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT invalido o expirado", exception);
        }
    }
}
