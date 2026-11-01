package com.projeto.usuario.usuario.business.converter;

import com.projeto.usuario.usuario.business.dto.endereco.EnderecoRequestDTO;
import com.projeto.usuario.usuario.business.dto.endereco.EnderecoResponseDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneResponseDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioRequestDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioResponseDTO;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "enderecos", ignore = true)
    @Mapping(target = "telefones", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateUsuario(UsuarioRequestDTO request, @MappingTarget Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Endereco toEntity(EnderecoRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Telefone toEntity(TelefoneRequestDTO request);

    UsuarioResponseDTO toResponse(Usuario usuario);
    EnderecoResponseDTO toResponse(Endereco endereco);
    TelefoneResponseDTO toResponse(Telefone telefone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    void updateEndereco(EnderecoRequestDTO request, @MappingTarget Endereco endereco);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    void updateTelefone(TelefoneRequestDTO request, @MappingTarget Telefone telefone);
}
