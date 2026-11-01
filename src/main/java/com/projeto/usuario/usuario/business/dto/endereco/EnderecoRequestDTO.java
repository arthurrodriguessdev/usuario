package com.projeto.usuario.usuario.business.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
        @NotBlank(message = "A rua é obrigatória")
        @Size(max = 100, message = "A rua deve ter no máximo 100 caracteres")
        String rua,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 60, message = "O bairro deve ter no máximo 60 caracteres")
        String bairro,

        @NotBlank(message = "O CEP é obrigatório")
        @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres")
        String cep,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 60, message = "A cidade deve ter no máximo 60 caracteres")
        String cidade
){}
