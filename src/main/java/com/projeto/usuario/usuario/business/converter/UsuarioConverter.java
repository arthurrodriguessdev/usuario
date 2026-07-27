package com.projeto.usuario.usuario.business.converter;

import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.springframework.stereotype.Component;
import java.util.List;

@Component // Define que é uma classe gerenciada pelo spring, MAS não é service, controller, entity
public class UsuarioConverter {
    public Usuario dtoParaUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(this.listaEnderecosDtoParaEndereco(usuarioDTO.getEnderecos()))
                .telefones(this.listaTelefonesDtoParaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    // Conversão de endereço
    public Endereco enderecoDtoParaEndereco(EnderecoDTO enderecoDto){
        return Endereco.builder()
                .rua(enderecoDto.getRua())
                .cep(enderecoDto.getCep())
                .cidade(enderecoDto.getCidade())
                .bairro(enderecoDto.getBairro())
                .build();
    }

    public Endereco enderecoDtoParaEndereco(EnderecoDTO enderecoDto, Usuario usuario){
        return Endereco.builder()
                .rua(enderecoDto.getRua())
                .cep(enderecoDto.getCep())
                .cidade(enderecoDto.getCidade())
                .bairro(enderecoDto.getBairro())
                .usuario(usuario)
                .build();
    }

    public List<Endereco> listaEnderecosDtoParaEndereco(List<EnderecoDTO> enderecosDto){
        return enderecosDto.stream().map(this::enderecoDtoParaEndereco).toList();
    }

    // Conversão de telefone
    public Telefone telefoneDtoParaTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build();
    }

    public Telefone telefoneDtoParaTelefone(TelefoneDTO telefoneDTO, Usuario usuario){
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .usuario(usuario)
                .build();
    }

    public List<Telefone> listaTelefonesDtoParaTelefone(List<TelefoneDTO> telefonesDto){
        return telefonesDto.stream().map(this::telefoneDtoParaTelefone).toList();
    }

    public UsuarioDTO usuarioParaUsuarioDto(Usuario usuario){
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(this.listaEnderecoParaEnderecosDto(usuario.getEnderecos()))
                .telefones(this.listaTelefoneParaTelefonesDto(usuario.getTelefones()))
                .build();
    }

    // Conversão de endereço
    public EnderecoDTO enderecoParaEnderecoDto(Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .bairro(endereco.getBairro())
                .build();
    }

    public List<EnderecoDTO> listaEnderecoParaEnderecosDto(List<Endereco> enderecos){
        return enderecos.stream().map(this::enderecoParaEnderecoDto).toList();
    }

    // Conversão de telefone
    public TelefoneDTO telefoneParaTelefoneDto(Telefone telefone){
        return TelefoneDTO.builder()
                .id(telefone.getId())
                .ddd(telefone.getDdd())
                .numero(telefone.getNumero())
                .build();
    }

    public List<TelefoneDTO> listaTelefoneParaTelefonesDto(List<Telefone> telefones){
        return telefones.stream().map(this::telefoneParaTelefoneDto).toList();
    }

    /*
    * Esses métodos são responsáveis pela atualização das entidades
    * */
    public Usuario updateUsuario(Usuario entity, UsuarioDTO usuarioDto, String senhaEncriptada) {
        entity.setNome((usuarioDto.getNome() != null) ? usuarioDto.getNome() : entity.getNome());
        entity.setEmail((usuarioDto.getEmail() != null) ? usuarioDto.getEmail() : entity.getEmail());
        entity.setSenha((senhaEncriptada != null) ? senhaEncriptada : entity.getSenha());
        return entity;
    }

    public Endereco updateEndereco(Endereco entity, EnderecoDTO enderecoDTO) {
        entity.setBairro((enderecoDTO.getBairro() != null) ? enderecoDTO.getBairro() : entity.getBairro());
        entity.setCep((enderecoDTO.getCep() != null) ? enderecoDTO.getCep() : entity.getCep());
        entity.setRua((enderecoDTO.getRua() != null) ? enderecoDTO.getRua() : entity.getRua());
        entity.setCidade((enderecoDTO.getCidade() != null) ? enderecoDTO.getCidade() : entity.getCidade());
        return entity;
    }

    public Telefone updateTelefone(Telefone entity, TelefoneDTO telefoneDTO) {
        entity.setNumero((telefoneDTO.getNumero() != null) ? telefoneDTO.getNumero() : entity.getNumero());
        entity.setDdd((telefoneDTO.getDdd() != null) ? telefoneDTO.getDdd() : entity.getDdd());
        return entity;
    }
}