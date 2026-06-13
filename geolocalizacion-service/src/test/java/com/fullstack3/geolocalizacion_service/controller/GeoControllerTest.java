package com.fullstack3.geolocalizacion_service.controller;

import com.fullstack3.geolocalizacion_service.dto.CoordenadasDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GeoControllerTest {

    private GeoController geoController;

    @BeforeEach
    void setUp() {
        geoController = new GeoController();
    }

    @Test
    void validarCoordenadas_CoordenadasValidas_RetornaTrueY200Ok() {
        // ARRANGE
        // Coordenadas válidas dentro de Chile (Ejemplo: Melipilla / Alhué)
        CoordenadasDTO requestValido = new CoordenadasDTO(-33.6891, -71.2146);

        // ACT
        ResponseEntity<Boolean> response = geoController.validarCoordenadas(requestValido);

        // ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "El estado HTTP debe ser 200 OK");
        assertEquals(Boolean.TRUE, response.getBody(), "El cuerpo de la respuesta debe ser 'true'");
    }

    @Test
    void validarCoordenadas_LatitudInvalida_RetornaFalseY400BadRequest() {
        // ARRANGE
        // Latitud fuera del rango de Chile (Ejemplo: -10.0, que caería en Perú/Brasil)
        CoordenadasDTO requestInvalido = new CoordenadasDTO(-10.0, -70.0);

        // ACT
        ResponseEntity<Boolean> response = geoController.validarCoordenadas(requestInvalido);

        // ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "El estado HTTP debe ser 400 Bad Request");
        assertFalse(response.getBody(), "La latitud norteña debe ser rechazada");
    }

    @Test
    void validarCoordenadas_LongitudInvalida_RetornaFalseY400BadRequest() {
        // ARRANGE
        // Longitud fuera del rango de Chile (Ej: -60.0, que caería en Argentina)
        CoordenadasDTO requestInvalido = new CoordenadasDTO(-33.0, -60.0);

        // ACT
        ResponseEntity<Boolean> response = geoController.validarCoordenadas(requestInvalido);

        // ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "El estado HTTP debe ser 400 Bad Request");
        assertFalse(response.getBody(), "La longitud oriental debe ser rechazada");
    }

    @Test
    void validarCoordenadas_ValoresNulos_RetornaFalseY400BadRequest() {
        // ARRANGE
        // El frontend manda un DTO vacío o con datos corruptos
        CoordenadasDTO requestNulo = new CoordenadasDTO(null, null);

        // ACT
        ResponseEntity<Boolean> response = geoController.validarCoordenadas(requestNulo);

        // ASSERT
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "El estado HTTP debe ser 400 Bad Request");
        assertFalse(response.getBody(), "No debe procesar coordenadas nulas");
    }
}