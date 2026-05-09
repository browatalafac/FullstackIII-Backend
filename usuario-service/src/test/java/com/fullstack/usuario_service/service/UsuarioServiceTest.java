package com.fullstack.usuario_service.service;

import com.fullstack.usuario_service.dto.UsuarioRequestDTO;
import com.fullstack.usuario_service.dto.UsuarioResponseDTO;
import com.fullstack.usuario_service.enums.Roles;
import com.fullstack.usuario_service.model.Usuario;
import com.fullstack.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    // 1. MOCK: Simulamos la base de datos
    @Mock
    private UsuarioRepository usuarioRepository;

    // 2. INJECT: Inyectamos el mock en el servicio que vamos a probar
    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void validarCredenciales_Exito_RetornaUsuario() {
        // ARRANGE, preparamos los datos
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("admin@innovatech.cl");
        request.setPassword("admin123");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail("admin@innovatech.cl");
        // La contraseña en la base de datos estaría encriptada
        usuarioMock.setPassword(new BCryptPasswordEncoder().encode("admin123"));

        // Le decimos a nuestro Mock: "Cuando te busquen por este correo, devuelve este usuario"
        when(usuarioRepository.findByEmail("admin@innovatech.cl")).thenReturn(Optional.of(usuarioMock));

        // ACT, Ejecutar el método real
        UsuarioResponseDTO response = usuarioService.validarCredenciales(request);

        // ASSERT, Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals("admin@innovatech.cl", response.getEmail());

        // Verificamos que el repositorio fue llamado exactamente 1 vez, en este caso
        verify(usuarioRepository, times(1)).findByEmail("admin@innovatech.cl");
    }

    @Test
    void validarCredenciales_PasswordIncorrecta_LanzaExcepcion() {
        // ARRANGE
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("admin@innovatech.cl");
        request.setPassword("claveMala"); // Contraseña errónea

        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail("admin@innovatech.cl");
        usuarioMock.setPassword(new BCryptPasswordEncoder().encode("admin123"));

        when(usuarioRepository.findByEmail("admin@innovatech.cl")).thenReturn(Optional.of(usuarioMock));

        // ACT y ASSERT
        // Verificamos que al poner la clave mala, el sistema caiga o explote con el error correcto
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.validarCredenciales(request);
        });

        assertEquals("Credenciales incorrectas", exception.getMessage());
    }

    @Test
    void obtenerTodos_RetornaListaDeUsuarios() {
        // ARRANGE: Preparamos una lista falsa de la base de datos
        Usuario admin = new Usuario();
        admin.setId(1L);
        admin.setEmail("admin@innovatech.cl");
        admin.setRol(Roles.ADMINISTRADOR_SISTEMA);

        Usuario brigadista = new Usuario();
        brigadista.setId(2L);
        brigadista.setEmail("brigadista@innovatech.cl");
        brigadista.setRol(Roles.BRIGADISTA);

        when(usuarioRepository.findAll()).thenReturn(List.of(admin, brigadista));

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        // ASSERT: Verificamos que traiga 2 y que los haya convertido bien a DTO, sin la contraseña
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("admin@innovatech.cl", resultado.get(0).getEmail());
        assertEquals(Roles.BRIGADISTA, resultado.get(1).getRol());

        verify(usuarioRepository, times(1)).findAll();
    }


    @Test
    void obtenerPorId_Exito_RetornaUsuarioDTO() {
        // ARRANGE
        Long idBuscado = 1L;
        Usuario mockUsuario = new Usuario();
        mockUsuario.setId(idBuscado);
        mockUsuario.setEmail("municipal@innovatech.cl");
        mockUsuario.setRol(Roles.FUNCIONARIO_MUNICIPAL);

        // Simulamos que la base de datos sí encuentra el ID 1
        when(usuarioRepository.findById(idBuscado)).thenReturn(Optional.of(mockUsuario));

        // ACT
        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(idBuscado);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId());
        assertEquals("municipal@innovatech.cl", resultado.getEmail());

        verify(usuarioRepository, times(1)).findById(idBuscado);
    }

    @Test
    void obtenerPorId_NoEncontrado_LanzaExcepcion() {
        // ARRANGE
        Long idInexistente = 99L;
        // Simulamos que la BD devuelve vacío (no encontró nada)
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT y ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(idInexistente);
        });

        // Verificamos que el mensaje de error sea el mismo programado
        assertEquals("Funcionario no encontrado", exception.getMessage());

        verify(usuarioRepository, times(1)).findById(idInexistente);
    }

}
