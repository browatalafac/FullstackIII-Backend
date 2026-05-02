package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.UsuarioRequestDTO;
import com.fullstack3.bff_emergencia.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "usuario-service", url = "http://localhost:8080/api/v1/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioResponseDTO obtenerPorId(@PathVariable("id") Long id);

    @PostMapping
    UsuarioResponseDTO crearUsuario(@RequestBody UsuarioRequestDTO request);

    @GetMapping
    List<UsuarioResponseDTO> getAllUsuarios();

}
