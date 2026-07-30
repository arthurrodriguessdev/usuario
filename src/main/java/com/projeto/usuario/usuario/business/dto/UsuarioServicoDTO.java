package com.projeto.usuario.usuario.business.dto;
// Refact
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioServicoDTO {
    private String email;
    private String senha;
}
