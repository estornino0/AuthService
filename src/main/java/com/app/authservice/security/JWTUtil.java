package com.app.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt-secret}")
    private String secret;

    public String generateToken(String email) throws IllegalArgumentException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer("APP")
                .sign(Algorithm.HMAC256(secret));
    }

    // Method to verify the JWT and then decode and extract the user email stored in the payload of the token
    public String validateTokenAndRetrieveEmail(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("APP")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
