package com.projeto.usuario.usuario.business.dto.usuario;

import com.projeto.usuario.usuario.business.dto.endereco.EnderecoResponseDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneResponseDTO;

import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        List<EnderecoResponseDTO> enderecos,
        List<TelefoneResponseDTO> telefones
) {}
