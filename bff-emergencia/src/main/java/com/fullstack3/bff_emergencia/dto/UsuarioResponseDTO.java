package com.fullstack3.bff_emergencia.dto;

import com.fullstack3.bff_emergencia.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private Roles rol;
}
