package com.fullstack.usuario_service.service;
import com.fullstack.usuario_service.enums.Roles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.model.Usuario;
import com.fullstack.usuario_service.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Solo para que el BFF pueda verificar el inicio de sesión.
    public Usuario buscarPorEmailParaLogin(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
    }

    // Opcional, es para obtener todos los funcionarios, aun no se implementa
    public List<UsuarioResponseDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    // Opcional, obtener un funcionario específico
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario no encontrado"));
        return mapearAResponseDTO(usuario);
    }


    public UsuarioResponseDTO crearFuncionario(UsuarioRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        // Verificamos que el correo no esté en uso
        Optional<Usuario> existente = usuarioRepository.findByEmail(requestDTO.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario nuevoFuncionario = new Usuario();
        nuevoFuncionario.setEmail(requestDTO.getEmail());

        // Encriptamos la contraseña antes de guardarla
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        nuevoFuncionario.setPassword(encoder.encode(requestDTO.getPassword()));

        nuevoFuncionario.setRol(Roles.ADMINISTRADOR_SISTEMA);

        Usuario guardado = usuarioRepository.save(nuevoFuncionario);

        return mapearAResponseDTO(guardado);
    }

    private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(usuario.getId());
        responseDTO.setEmail(usuario.getEmail());
        responseDTO.setRol(usuario.getRol());
        return responseDTO; // ¡La contraseña no viaja en el DTO!
    }

    public UsuarioResponseDTO validarCredenciales(UsuarioRequestDTO requestDTO) {

        Usuario usuario = usuarioRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // Compara la contraseña enviada con la contraseña encriptada en la base de datos
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(requestDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // Si todo esta correcto, se devuelve el usuario, pero sin la contraseña
        //Usuario es en realidad un funcionario
        return mapearAResponseDTO(usuario);
    }

}