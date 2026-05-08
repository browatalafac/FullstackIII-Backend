package com.fullstack.usuario_service.dto;

import com.fullstack.usuario_service.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private Roles rol;
}
