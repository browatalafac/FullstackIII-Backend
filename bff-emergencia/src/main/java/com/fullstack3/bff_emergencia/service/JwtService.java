package com.fullstack3.bff_emergencia.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final String SECRET_KEY = "EstaEsUnaClaveSuperSecretaParaBomberosInnovatechQueDebeSerLarga";

    // 1 día en milisegundos
    private final long EXPIRATION_TIME = 86400000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generarToken(String email, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol); // Guardamos el rol dentro del token para saber sus permisos

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
