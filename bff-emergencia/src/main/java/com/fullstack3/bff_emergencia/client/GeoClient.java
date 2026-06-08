package com.fullstack3.bff_emergencia.client;

import com.fullstack3.bff_emergencia.dto.CoordenadaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "geo-service", url = "http://localhost:8081/api/v1/geolocalizacion")
public interface GeoClient {
    @PostMapping("/api/v1/geolocalizacion/validar")
    Boolean validarCoordenadas(@RequestBody CoordenadaDTO request);

}
