package com.andriiyan.springlearning.springboot.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String JWT_SUBJECT = "UserDetails";
    private static final String JWT_CLAIM_USERNAME = "username";

    @Value("${jwt.issuer}")
    private String jwtIssuer;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(@NonNull String username) {
        return JWT.create()
                .withSubject(JWT_SUBJECT)
                .withClaim(JWT_CLAIM_USERNAME, username)
                .withIssuedAt(new Date())
                .withIssuer(jwtIssuer)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateTokenAndRertrieveSubject(@Nullable String token) {
        if (token == null) throw new JWTVerificationException("token is null");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject(JWT_SUBJECT)
                .withIssuer(jwtIssuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(JWT_CLAIM_USERNAME).asString();
    }


}
