package com.fullstack3.brigadas_recursos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrigadaDTO {
    private Long id;
    private String nombre;
    private String tipoEquipo;
    private String estado;
}
