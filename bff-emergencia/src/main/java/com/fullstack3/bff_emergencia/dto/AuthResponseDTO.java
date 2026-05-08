package com.fullstack3.bff_emergencia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UsuarioResponseDTO usuario; // Aquí van el id, email y rol
}
