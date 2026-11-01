package com.projeto.usuario.usuario.business.dto.endereco;

public record EnderecoResponseDTO(
        Long id,
        String rua,
        String bairro,
        String cep,
        String cidade
){}
