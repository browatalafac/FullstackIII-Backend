package com.fullstack3.bff_emergencia.security;

import com.fullstack3.bff_emergencia.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Si no hay header o no empieza con "Bearer ", lo dejamos pasar.
        // Si es una ruta protegida, Spring Security lo bloqueará más adelante por no tener contexto.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token (quitando la palabra "Bearer ")
        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extraerEmail(jwt);

            // 3. Si extrajimos el email y el usuario aún no está autenticado en este hilo
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Verificamos si el token es válido matemáticamente y no ha expirado
                if (jwtService.validarToken(jwt)) {
                    // Creamos el objeto de autenticación de Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            new ArrayList<>() // Aquí podríamos pasar los roles si los extraemos del token
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Guardamos la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si el token es malicioso o expirado, fallará en silencio y no establecerá la autenticación
        }

        // 4. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
