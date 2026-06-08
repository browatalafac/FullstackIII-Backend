package com.fullstack3.bff_emergencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brigada-service", url = "http://localhost:1011/api/v1/brigadas")
public interface BrigadaClient {
    @PostMapping("/asignar")
    String asignarBrigada(@RequestParam("reporteId") Long reporteId, @RequestParam("tipoEquipo") String tipoEquipo);
}
