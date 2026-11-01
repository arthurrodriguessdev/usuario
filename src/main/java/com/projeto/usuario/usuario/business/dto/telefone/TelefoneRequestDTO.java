package com.projeto.usuario.usuario.business.dto.telefone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TelefoneRequestDTO(
        @NotBlank(message = "O número do telefone é obrigatório")
        @Size(max = 10, message = "O número do telefone deve ter no máximo 10 caracteres")
        String numero,

        @NotBlank(message = "O DDD é obrigatório")
        @Size(max = 3, message = "O DDD deve ter no máximo 3 caracteres")
        String ddd
){}
