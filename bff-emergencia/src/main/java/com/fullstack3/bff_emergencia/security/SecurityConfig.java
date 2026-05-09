package com.fullstack3.bff_emergencia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                // Definimos las reglas de autorización de las rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (No piden token)
                        .requestMatchers("/api/bff/emergencias/reportar").permitAll()
                        .requestMatchers("/api/bff/emergencias/login").permitAll()
                        .requestMatchers("/api/bff/emergencias/seguimiento/**").permitAll()

                        // Rutas protegidas por rol
                        // Cualquier usuario autenticado (Admin, Muni, Brigadista) puede hacer GET para ver la lista
                        .requestMatchers(HttpMethod.GET, "/api/bff/emergencias/reportes").authenticated()

                        // Solo el brigaditas puede hacer un PUT para editar un reporte, solo el estado por ahora
                        .requestMatchers(HttpMethod.PUT, "/api/bff/emergencias/reportes/**").hasAuthority("BRIGADISTA")

                        // Desspues se podria crear un endpoint para crear funcionarios, solo el Admin podría
                        // .requestMatchers(HttpMethod.POST, "/api/bff/emergencias/funcionarios").hasAuthority("ADMINISTRADOR_SISTEMA")
                        //Esto aun que no se implemente aun.


                        // Cualquier otra ruta que no esté especificada, por defecto pedirá token
                        .anyRequest().authenticated()
                )

                // Indicamos que nuestra API es "Stateless" (Sin estado), no guarda sesiones en memoria
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Insertamos el filtro JWT justo antes del filtro de autenticación por defecto de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite el puerto de React
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
