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
            //se pobla la base de datos si llega a estar vacía
            if (usuarioRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String passwordComun = encoder.encode("innovatech123");

                Usuario admin = new Usuario();
                admin.setEmail("admin@innovatech.cl");
                admin.setPassword(passwordComun);
                admin.setRol(Roles.ADMINISTRADOR_SISTEMA);

                Usuario funcionario = new Usuario();
                funcionario.setEmail("muni@innovatech.cl");
                funcionario.setPassword(passwordComun);
                funcionario.setRol(Roles.FUNCIONARIO_MUNICIPAL);

                Usuario brigadista = new Usuario();
                brigadista.setEmail("brigada@innovatech.cl");
                brigadista.setPassword(passwordComun);
                brigadista.setRol(Roles.BRIGADISTA);

                usuarioRepository.saveAll(Arrays.asList(admin, funcionario, brigadista));

                System.out.println("Usuarios de prueba (Admin, Funcionario, Brigadista) creados con éxito.");
            }
        };
    }
}