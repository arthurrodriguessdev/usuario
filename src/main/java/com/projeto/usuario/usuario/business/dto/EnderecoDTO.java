package com.projeto.usuario.usuario.business.dto;

public record EnderecoDTO(
        Long id,
        String rua,
        String bairro,
        String cep,
        String cidade
){}
