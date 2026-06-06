package com.fullstack3.notificaciones_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaDTO {
    private Long reporteId;
    private String mensaje;
    private String prioridad; // ALTA, MEDIA, BAJA
    private String destinatarioRol; // FUNCIONARIO, COMUNIDAD, BRIGADA
}
