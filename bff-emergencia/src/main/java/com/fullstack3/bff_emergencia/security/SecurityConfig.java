package com.fullstack3.bff_emergencia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF (Cross-Site Request Forgery) porque usaremos tokens, no cookies de sesión
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // Definimos las reglas de autorización de nuestras rutas
                .authorizeHttpRequests(auth -> auth
                        // 🟢 RUTAS PÚBLICAS (No piden token)
                        .requestMatchers("/api/bff/emergencias/reportar").permitAll()
                        .requestMatchers("/api/bff/emergencias/login").permitAll()
                        .requestMatchers("/api/bff/emergencias/seguimiento/**").permitAll() // Por si hiciste el endpoint de seguimiento

                        // 🔴 RUTAS PROTEGIDAS (Cualquier otra ruta pide token)
                        .anyRequest().authenticated()
                )

                // Indicamos que nuestra API es "Stateless" (Sin estado), no guarda sesiones en memoria
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Insertamos nuestro guardia (el filtro JWT) justo antes del filtro de autenticación por defecto de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite tu puerto de React (Agrega el 3000 si usas Create React App en vez de Vite)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
