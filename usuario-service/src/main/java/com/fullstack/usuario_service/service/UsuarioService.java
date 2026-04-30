package com.fullstack.usuario_service.service;

import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.model.Usuario;
import com.fullstack.usuario_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Devuelve una lista de los usuarios dto
    public List<UsuarioResponseDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Transforma la lista de Entidades a una lista de DTOs usando Streams
        return usuarios.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapearAResponseDTO(usuario);
    }

    // Crear un usuario (Recibe RequestDTO, Retorna ResponseDTO)
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO requestDTO) {

        if (requestDTO.getRun() == null || requestDTO.getRun().isBlank()) {
            throw new RuntimeException("El run es obligatorio");
        }

        if (requestDTO.getRol() == null) {
            throw new RuntimeException("El rol es obligatorio");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setRun(requestDTO.getRun());
        nuevoUsuario.setRol(requestDTO.getRol());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return mapearAResponseDTO(usuarioGuardado);
    }

    // Método auxiliar privado para centralizar el mapeo de Entidad -> DTO
    private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(usuario.getId());
        responseDTO.setRun(usuario.getRun());
        responseDTO.setRol(usuario.getRol());
        return responseDTO;
    }
}
