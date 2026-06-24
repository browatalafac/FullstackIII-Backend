package com.fullstack3.brigadas_recursos_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "brigadas")
public class Brigada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipoEquipo;
    private String estado;
}
