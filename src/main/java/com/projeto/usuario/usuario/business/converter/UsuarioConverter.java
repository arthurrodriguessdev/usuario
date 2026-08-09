package com.projeto.usuario.usuario.business.converter;

import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UsuarioConverter {
    public Usuario dtoParaUsuario(UsuarioDTO usuarioDTO){
        List<Endereco> enderecos = this.listaEnderecosDtoParaEndereco(usuarioDTO.enderecos());
        List<Telefone> telefones = this.listaTelefonesDtoParaTelefone(usuarioDTO.telefones());
        return Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .senha(usuarioDTO.senha())
                .enderecos((enderecos.isEmpty()) ? null : enderecos)
                .telefones((telefones.isEmpty()) ? null : telefones)
                .build();
    }

    // Conversão de endereço
    public Endereco enderecoDtoParaEndereco(EnderecoDTO enderecoDto){
        return Endereco.builder()
                .rua(enderecoDto.rua())
                .cep(enderecoDto.cep())
                .cidade(enderecoDto.cidade())
                .bairro(enderecoDto.bairro())
                .build();
    }

    public Endereco enderecoDtoParaEndereco(EnderecoDTO enderecoDto, Usuario usuario){
        return Endereco.builder()
                .rua(enderecoDto.rua())
                .cep(enderecoDto.cep())
                .cidade(enderecoDto.cidade())
                .bairro(enderecoDto.bairro())
                .usuario(usuario)
                .build();
    }

    public List<Endereco> listaEnderecosDtoParaEndereco(List<EnderecoDTO> enderecosDto){
        if(enderecosDto == null){
            return Collections.emptyList();
        }

        return enderecosDto.stream().map(this::enderecoDtoParaEndereco).toList();
    }

    // Conversão de telefone
    public Telefone telefoneDtoParaTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .ddd(telefoneDTO.ddd())
                .numero(telefoneDTO.numero())
                .build();
    }

    public Telefone telefoneDtoParaTelefone(TelefoneDTO telefoneDTO, Usuario usuario){
        return Telefone.builder()
                .ddd(telefoneDTO.ddd())
                .numero(telefoneDTO.numero())
                .usuario(usuario)
                .build();
    }

    public List<Telefone> listaTelefonesDtoParaTelefone(List<TelefoneDTO> telefonesDto){
        if(telefonesDto == null){
            return Collections.emptyList();
        }

        return telefonesDto.stream().map(this::telefoneDtoParaTelefone).toList();
    }

    public UsuarioDTO usuarioParaUsuarioDto(Usuario usuario){
        return new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                this.listaEnderecoParaEnderecosDto(usuario.getEnderecos()),
                this.listaTelefoneParaTelefonesDto(usuario.getTelefones()));
    }

    // Conversão de endereço
    public EnderecoDTO enderecoParaEnderecoDto(Endereco endereco){
        return new EnderecoDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getCep(),
                endereco.getCidade(),
                endereco.getBairro());
    }

    public List<EnderecoDTO> listaEnderecoParaEnderecosDto(List<Endereco> enderecos){
        if(enderecos == null){
            return Collections.emptyList();
        }

        return enderecos.stream().map(this::enderecoParaEnderecoDto).toList();
    }

    // Conversão de telefone
    public TelefoneDTO telefoneParaTelefoneDto(Telefone telefone){
        return new TelefoneDTO(
                telefone.getId(),
                telefone.getDdd(),
                telefone.getNumero());
    }

    public List<TelefoneDTO> listaTelefoneParaTelefonesDto(List<Telefone> telefones){
        return telefones.stream().map(this::telefoneParaTelefoneDto).toList();
    }

    /*
    * Esses métodos são responsáveis pela atualização das entidades
    * */
    public Usuario updateUsuario(Usuario entity, UsuarioDTO usuarioDto, String senhaEncriptada) {
        entity.setNome((usuarioDto.nome() != null) ? usuarioDto.nome() : entity.getNome());
        entity.setEmail((usuarioDto.email() != null) ? usuarioDto.email() : entity.getEmail());
        entity.setSenha((senhaEncriptada != null) ? senhaEncriptada : entity.getSenha());
        return entity;
    }

    public Endereco updateEndereco(Endereco entity, EnderecoDTO enderecoDTO) {
        entity.setBairro((enderecoDTO.bairro() != null) ? enderecoDTO.bairro() : entity.getBairro());
        entity.setCep((enderecoDTO.cep() != null) ? enderecoDTO.cep() : entity.getCep());
        entity.setRua((enderecoDTO.rua() != null) ? enderecoDTO.rua() : entity.getRua());
        entity.setCidade((enderecoDTO.cidade() != null) ? enderecoDTO.cidade() : entity.getCidade());
        return entity;
    }

    public Telefone updateTelefone(Telefone entity, TelefoneDTO telefoneDTO) {
        entity.setNumero((telefoneDTO.numero() != null) ? telefoneDTO.numero() : entity.getNumero());
        entity.setDdd((telefoneDTO.ddd() != null) ? telefoneDTO.ddd() : entity.getDdd());
        return entity;
    }
}