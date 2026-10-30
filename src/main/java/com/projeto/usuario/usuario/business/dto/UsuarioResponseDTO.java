package com.projeto.usuario.usuario.business.dto;

import java.util.List;

public record UsuarioResponseDTO(
        String nome,
        String email,
        List<EnderecoDTO> enderecos,
        List<TelefoneDTO> telefones
) {}
