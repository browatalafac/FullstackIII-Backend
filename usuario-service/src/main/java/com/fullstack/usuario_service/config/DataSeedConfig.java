package com.fullstack.usuario_service.config;

import com.fullstack.usuario_service.enums.Roles;
import com.fullstack.usuario_service.model.Usuario;
import com.fullstack.usuario_service.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class DataSeedConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Se pobla la base de datos si está vacía
            if (usuarioRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String passwordComun = encoder.encode("innovatech123");

                //Crear Administrador
                Usuario admin = new Usuario();
                admin.setEmail("admin@innovatech.cl");
                admin.setPassword(passwordComun);
                admin.setRol(Roles.ADMINISTRADOR_SISTEMA);

                //Crear Funcionario Municipal
                Usuario funcionario = new Usuario();
                funcionario.setEmail("muni@innovatech.cl");
                funcionario.setPassword(passwordComun);
                funcionario.setRol(Roles.FUNCIONARIO_MUNICIPAL);

                //Crear Brigadista
                Usuario brigadista = new Usuario();
                brigadista.setEmail("uli.torres@duocuc.cl");
                brigadista.setPassword(passwordComun);
                brigadista.setRol(Roles.BRIGADISTA);

                // Guardar todos de una vez
                usuarioRepository.saveAll(Arrays.asList(admin, funcionario, brigadista));

                System.out.println("Usuarios de prueba (Admin, Funcionario, Brigadista) creados con éxito.");
            }
        };
    }
}