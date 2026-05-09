package com.fullstack.usuario_service.controller;

import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/usuarios")
@RestController
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioRequestDTO requestDTO) {
        return usuarioService.validarCredenciales(requestDTO);
    }

    @GetMapping
    public List<UsuarioResponseDTO> getAllUsuarios(){
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }



    @PostMapping("/admin/crear")
    public UsuarioResponseDTO crearFuncionario(@RequestBody UsuarioRequestDTO requestDTO) {
        return usuarioService.crearFuncionario(requestDTO);
    }




}