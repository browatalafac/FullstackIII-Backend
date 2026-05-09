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

        // ARRANGE
        UsuarioResponseDTO mockResponse = new UsuarioResponseDTO();
        mockResponse.setId(1L);
        mockResponse.setEmail("poyo@gmail.com");
        mockResponse.setRol(Roles.BRIGADISTA);

        // Le decimos al mock qué hacer
        when(usuarioService.obtenerPorId(1L)).thenReturn(mockResponse);

        // ACT: Llamamos al controlador
        UsuarioResponseDTO response = usuarioController.obtenerPorId(1L);

        // ASSERT: Comprobamos que llegó bien
        assertNotNull(response);
        assertEquals("poyo@gmail.com", response.getEmail());
        assertEquals(Roles.BRIGADISTA, response.getRol());

        // Se verifica que el controlador efectivamente llamó al servicio 1 vez
        verify(usuarioService, times(1)).obtenerPorId(1L);
    }
    @Test
    void login_credencialesCorrectas_retornaUsuarioDTO() {
        // ARRANGE: Preparamos la petición que simula venir del Postman o Frontend, por ejemplo
        UsuarioRequestDTO requestBody = new UsuarioRequestDTO();
        requestBody.setEmail("brigadista@innovatech.cl");
        requestBody.setPassword("admin123");

        // Preparamos la respuesta falsa del servicio
        UsuarioResponseDTO mockResponse = new UsuarioResponseDTO();
        mockResponse.setId(2L);
        mockResponse.setEmail("brigadista@innovatech.cl");
        mockResponse.setRol(Roles.BRIGADISTA);

        // Le decimos al mock que cunado el servicio llame a validarCredenciales con cualquier request, devuelve el mockResponse
        when(usuarioService.validarCredenciales(any(UsuarioRequestDTO.class))).thenReturn(mockResponse);

        // ACT
        UsuarioResponseDTO response = usuarioController.login(requestBody);

        // ASSERT: Comprobamos que el controlador nos devolvió al usuario correcto
        assertNotNull(response);
        assertEquals("brigadista@innovatech.cl", response.getEmail());
        assertEquals(Roles.BRIGADISTA, response.getRol());

        verify(usuarioService, times(1)).validarCredenciales(requestBody);
    }
    @Test
    void obtenerUsuario_idNoExiste_lanzaExcepcion() {
        // ARRANGE
        Long idInexistente = 99L;

        // Simulamos que el servicio explota al no encontrar el usuario
        when(usuarioService.obtenerPorId(idInexistente))
                .thenThrow(new RuntimeException("Funcionario no encontrado"));

        // ACT y ASSERT: Verificamos que el controlador también lance la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioController.obtenerPorId(idInexistente);
        });

        assertEquals("Funcionario no encontrado", exception.getMessage());
        verify(usuarioService, times(1)).obtenerPorId(idInexistente);
    }

}