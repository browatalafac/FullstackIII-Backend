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
import java.util.UUID;

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

    @Column(unique = true, nullable = false, updatable = false)
    private String codigoSeguimiento;

    @CreatedDate
    private LocalDateTime fechaReporte;

    private Double latitud;

    private Double longitud;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoIncendio tipoIncendio;

    @Enumerated(EnumType.STRING)
    private EstadoReporte estado;

    @Enumerated(EnumType.STRING)
    private NivelPrioridad nivelPrioridad;

    private Integer radioImpacto; // En metros

    @Enumerated(EnumType.STRING)
    private EquipoAsignado equipoAsignado;

    // Se ejecuta automáticamente antes del primer save()
    @PrePersist
    public void generarCodigoSeguimiento() {
        if (this.codigoSeguimiento == null) {
            // Genera un código amigable para el ciudadano, ej: "REP-4F3A8B12"
            String uuidCoroto = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            this.codigoSeguimiento = "REP-" + uuidCoroto;
        }
    }

}
