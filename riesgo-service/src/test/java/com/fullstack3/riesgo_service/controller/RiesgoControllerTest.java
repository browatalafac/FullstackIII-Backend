package com.fullstack3.riesgo_service.controller;

import com.fullstack3.riesgo_service.dto.CoordenadaDTO;
import com.fullstack3.riesgo_service.dto.RutaDTO;
import com.fullstack3.riesgo_service.dto.ZonaRiesgoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RiesgoControllerTest {

    // Instanciamos el controlador real, sin Mocks
    private RiesgoController riesgoController;

    @BeforeEach
    void setUp() {
        // Inicializamos un controlador limpio antes de cada test
        riesgoController = new RiesgoController();
    }

    @Test
    void obtenerZonaEvacuacion_GeneraPoligonoCorrectamente() {
        // ARRANGE
        Long reporteId = 15L;
        double lat = -33.4500;
        double lng = -70.6500;

        // ACT
        ZonaRiesgoDTO resultado = riesgoController.obtenerZonaEvacuacion(reporteId, lat, lng);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(15L, resultado.getReporteId());

        // Usamos "getPerimetro()" basado en ZonaRiesgoDTO
        List<CoordenadaDTO> poligono = resultado.getPerimetro();
        assertEquals(4, poligono.size());

        // Validamos la exactitud matemática del primer punto
        assertEquals(-33.4530, poligono.get(0).getLatitud(), 0.0001);
        assertEquals(-70.6530, poligono.get(0).getLongitud(), 0.0001);

        // Validamos el tercer punto
        assertEquals(-33.4470, poligono.get(2).getLatitud(), 0.0001);
        assertEquals(-70.6470, poligono.get(2).getLongitud(), 0.0001);
    }

    @Test
    void obtenerRutaSegura_GeneraCaminoCorrectamente() {
        // ARRANGE
        Long reporteId = 42L;
        double lat = -33.4500;
        double lng = -70.6500;

        // ACT
        RutaDTO resultado = riesgoController.obtenerRutaSegura(reporteId, lat, lng);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(42L, resultado.getReporteId());

        // Usamos "getPuntosRuta()" basado en RutaDTO
        List<CoordenadaDTO> camino = resultado.getPuntosRuta();
        assertEquals(3, camino.size());

        // El primer punto debe ser exactamente la ubicación original del incendio
        assertEquals(lat, camino.get(0).getLatitud(), 0.0001);
        assertEquals(lng, camino.get(0).getLongitud(), 0.0001);

        // El último punto debe estar desplazado matemáticamente en +0.010
        assertEquals(-33.4400, camino.get(2).getLatitud(), 0.0001);
        assertEquals(-70.6400, camino.get(2).getLongitud(), 0.0001);
    }

}
