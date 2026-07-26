package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.exception.ConflictException;
import com.projeto.usuario.usuario.exception.ResourceNotFound;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import com.projeto.usuario.usuario.infraestructure.repository.EnderecoRepository;
import com.projeto.usuario.usuario.infraestructure.repository.TelefoneRepository;
import com.projeto.usuario.usuario.infraestructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder; // Classe do security usada para codificar ps
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuarioSalvar = usuarioRepository.save(usuarioConverter.dtoParaUsuario(usuarioDTO));
        return usuarioConverter.usuarioParaUsuarioDto(usuarioSalvar);
    }

    public Usuario getUsuario(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("Usuário não encontrado"));
    };

    public UsuarioDTO buscarUsuario(Long id){
        Usuario usuarioBuscar = getUsuario(id);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioBuscar);
    }

    public void deletarUsuario(Long id){
        getUsuario(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO atualizarDadosUsuario(UsuarioDTO dto, Long id){
        Usuario usuarioEntity = getUsuario(id);
        String senhaEncriptada = null;
        if(dto != null && dto.getSenha() != null){
            senhaEncriptada = passwordEncoder.encode(dto.getSenha());
        }

        Usuario usuarioAtualizado = usuarioConverter.updateUsuario(usuarioEntity, dto, senhaEncriptada);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioRepository.save(usuarioAtualizado));
    }

    public EnderecoDTO atualizarDadosEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(
                () -> new ResourceNotFound("Endereço não encontrado.")
        );

        Endereco enderecoAtualizado = usuarioConverter.updateEndereco(enderecoEntity, enderecoDTO);
        return usuarioConverter.enderecoParaEnderecoDto(enderecoRepository.save(enderecoAtualizado));
    }

    public TelefoneDTO atualizarDadosTelefone(Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFound("Telefone não encontrado.")
        );

        Telefone telefoneAtualizado = usuarioConverter.updateTelefone(telefoneEntity, telefoneDTO);
        return usuarioConverter.telefoneParaTelefoneDto(telefoneRepository.save(telefoneAtualizado));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("O e-mail já existe.");
            }

        } catch (ConflictException e) {
            throw new ConflictException("O e-mail já existe", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}