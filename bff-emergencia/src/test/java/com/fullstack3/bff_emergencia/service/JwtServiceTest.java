package com.fullstack3.bff_emergencia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    // Aquí usamos @InjectMocks directo porque JwtService no depende de repositorios externos
    @InjectMocks
    private JwtService jwtService;

    @Test
    void generarYExtraerDatos_FuncionaCorrectamente() {
        // ARRANGE
        String emailPrueba = "brigadista@innovatech.cl";
        String rolPrueba = "BRIGADISTA";

        // ACT
        // Generamos un token real
        String tokenGenerado = jwtService.generarToken(emailPrueba, rolPrueba);

        // Extraemos los datos de ese token
        String emailExtraido = jwtService.extraerEmail(tokenGenerado);
        String rolExtraido = jwtService.extraerRol(tokenGenerado);
        boolean esValido = jwtService.validarToken(tokenGenerado);

        // ASSERT
        assertNotNull(tokenGenerado, "El token no debería ser nulo");
        assertTrue(esValido, "El token debería ser válido");
        assertEquals(emailPrueba, emailExtraido, "El email extraído debe coincidir");
        assertEquals(rolPrueba, rolExtraido, "El rol extraído debe coincidir");
    }
}
