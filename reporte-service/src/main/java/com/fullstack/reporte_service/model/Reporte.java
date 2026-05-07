package com.fullstack.reporte_service.model;

import com.fullstack.reporte_service.enums.EquipoAsignado;
import com.fullstack.reporte_service.enums.EstadoReporte;
import com.fullstack.reporte_service.enums.NivelPrioridad;
import com.fullstack.reporte_service.enums.TipoIncendio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reportes")
@EntityListeners(AuditingEntityListener.class) // Para que @CreatedDate funcione
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime fechaReporte;

    private Double latitud;

    private Double longitud;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoIncendio tipoIncendio;

    @Enumerated(EnumType.STRING)
    private EstadoReporte estado;

    private Long usuarioId;
    private String runCiudadano;
    private Boolean anonimo;

    @Enumerated(EnumType.STRING)
    private NivelPrioridad nivelPrioridad;

    private Integer radioImpacto; //Esta en metros

    @Enumerated(EnumType.STRING)
    private EquipoAsignado equipoAsignado;

}
