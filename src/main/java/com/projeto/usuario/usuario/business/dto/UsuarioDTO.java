package com.projeto.usuario.usuario.business.dto;

import java.util.List;

public record UsuarioDTO(
        String nome,
        String email,
        String senha,
        List<EnderecoDTO> enderecos,
        List<TelefoneDTO> telefones
){}
