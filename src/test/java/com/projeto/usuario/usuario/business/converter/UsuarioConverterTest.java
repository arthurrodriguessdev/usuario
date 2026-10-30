package com.projeto.usuario.usuario.business.converter;

import com.projeto.usuario.usuario.business.dto.UsuarioResponseDTO;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioConverterTest {
    private final UsuarioConverter converter = new UsuarioConverter();

    @Test
    void convertsUserToResponseWithoutPassword() {
        Usuario usuario = Usuario.builder()
                .nome("Ana Silva")
                .email("ana@example.com")
                .senha("encoded-password")
                .enderecos(List.of())
                .telefones(List.of())
                .build();

        UsuarioResponseDTO response = converter.usuarioParaUsuarioDto(usuario);

        assertEquals(new UsuarioResponseDTO("Ana Silva", "ana@example.com", List.of(), List.of()), response);
    }
}
