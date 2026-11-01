package com.projeto.usuario.usuario.business.dto.usuario;

import com.projeto.usuario.usuario.business.dto.endereco.EnderecoRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 50,
                message = "O nome deve ter de 3 a 50 caracteres"
        )
        String nome,

        @Email(message = "Informe um formato válido de e-mail")
        @NotBlank(message = "O e-mail é obrigatório")
        @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter, no mínimo, 8 caracteres")
        String senha,

        @Valid List<EnderecoRequestDTO> enderecos,
        @Valid List<TelefoneRequestDTO> telefones
){}
