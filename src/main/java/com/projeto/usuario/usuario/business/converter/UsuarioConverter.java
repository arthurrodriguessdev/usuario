package com.projeto.usuario.usuario.business.converter;

import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public List<Endereco> listaEnderecosDtoParaEndereco(List<EnderecoDTO> enderecosDto){
        List<Endereco> listaEnderecos = new ArrayList<>();
        enderecosDto.forEach(endereco ->{
            listaEnderecos.add(this.enderecoDtoParaEndereco(endereco));
        });

        return listaEnderecos;
    }

    // Conversão de telefone
    public Telefone telefoneDtoParaTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .ddd(telefoneDTO.getDdd())
                .numero(telefoneDTO.getNumero())
                .build();
    }

    public List<Telefone> listaTelefonesDtoParaTelefone(List<TelefoneDTO> telefonesDto){
        // stream transforma em um fluxo para ser percorrido
        // o map pega cada telefoneDto e faz a chamada do método passando ele como parâmetro
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
                .ddd(telefone.getDdd())
                .numero(telefone.getNumero())
                .build();
    }

    public List<TelefoneDTO> listaTelefoneParaTelefonesDto(List<Telefone> telefones){
        return telefones.stream().map(this::telefoneParaTelefoneDto).toList();
    }
}