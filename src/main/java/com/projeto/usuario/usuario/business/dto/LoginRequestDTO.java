package com.projeto.usuario.usuario.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um formato válido de e-mail")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha
){}