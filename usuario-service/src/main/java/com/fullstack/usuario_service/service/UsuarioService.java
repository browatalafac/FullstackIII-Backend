package com.fullstack.usuario_service.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.model.Usuario;
import com.fullstack.usuario_service.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // 1. MÉTODO NUEVO Y CRÍTICO: Buscar por email para el proceso de Login
    // Retornamos la Entidad completa (incluyendo la contraseña encriptada)
    // SOLO para que el BFF (o el controlador de autenticación) pueda verificar el inicio de sesión.
    public Usuario buscarPorEmailParaLogin(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
        // Usamos un mensaje genérico por seguridad, no decimos "Email no existe"
    }

    // 2. OPCIONAL: Obtener todos los funcionarios (Solo si tienes un panel de admin)
    public List<UsuarioResponseDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. OPCIONAL: Obtener un funcionario específico
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        return mapearAResponseDTO(usuario);
    }

    // ELIMINAMOS crearUsuario:
    // Como los funcionarios ya están en la base de datos, no necesitamos un endpoint
    // público para registrarlos. Si necesitas crear nuevos en el futuro,
    // harías un método exclusivo para administradores.

    // Método auxiliar privado (Nota que el DTO ya no tiene RUN, tiene Email)
    private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(usuario.getId());
        responseDTO.setEmail(usuario.getEmail());
        responseDTO.setRol(usuario.getRol());
        return responseDTO; // ¡La contraseña no viaja en el DTO!
    }

    public UsuarioResponseDTO validarCredenciales(UsuarioRequestDTO requestDTO) {
        // 1. Buscar al usuario por el email
        Usuario usuario = usuarioRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // 2. Comparar la contraseña enviada con la contraseña encriptada en la BD
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(requestDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 3. Si todo está correcto, devolvemos el DTO (sin la contraseña)
        return mapearAResponseDTO(usuario);
    }

}