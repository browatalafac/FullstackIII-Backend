package com.fullstack.usuario_service.controller;

import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.enums.Roles;
import com.fullstack.usuario_service.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    UsuarioController usuarioController;

    @Mock
    UsuarioService usuarioService;

    @Test
    void obtenerUsuario_retorna_ok() {

        //ARRANGE
        UsuarioResponseDTO mockResponse = new UsuarioResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setEmail("poyo@gmail.com");
        mockResponse.setRol(Roles.BRIGADISTA);
        when(usuarioService.obtenerPorId(1L)).thenReturn(mockResponse);

        //ACT
        UsuarioResponseDTO response = usuarioController.obtenerPorId(1L);

        //ASSERT
        assertNotNull(response);
        assertEquals("poyo@gmail.com", response.getEmail());
        assertEquals(Roles.BRIGADISTA, response.getRol());
        verify(usuarioService, times(1)).obtenerPorId(1L);
    }
    @Test
    void login_credencialesCorrectas_retornaUsuarioDTO() {
        UsuarioRequestDTO requestBody = new UsuarioRequestDTO();
        requestBody.setEmail("brigadista@innovatech.cl");
        requestBody.setPassword("admin123");

        UsuarioResponseDTO mockResponse = new UsuarioResponseDTO();
        mockResponse.setId(2L);
        mockResponse.setEmail("brigadista@innovatech.cl");
        mockResponse.setRol(Roles.BRIGADISTA);

        when(usuarioService.validarCredenciales(any(UsuarioRequestDTO.class))).thenReturn(mockResponse);

        //ACT
        UsuarioResponseDTO response = usuarioController.login(requestBody);

        //ASSERT
        assertNotNull(response);
        assertEquals("brigadista@innovatech.cl", response.getEmail());
        assertEquals(Roles.BRIGADISTA, response.getRol());
        verify(usuarioService, times(1)).validarCredenciales(requestBody);
    }
    @Test
    void obtenerUsuario_idNoExiste_lanzaExcepcion() {
        //ARRANGE
        Long idInexistente = 99L;

        when(usuarioService.obtenerPorId(idInexistente))
                .thenThrow(new RuntimeException("Funcionario no encontrado"));

        //ACT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.obtenerPorId(idInexistente);
        });

        //ASSERT
        assertEquals("Funcionario no encontrado", exception.getMessage());
        verify(usuarioService, times(1)).obtenerPorId(idInexistente);
    }

}