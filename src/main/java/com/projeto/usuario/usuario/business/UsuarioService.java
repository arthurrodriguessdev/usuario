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
import com.projeto.usuario.usuario.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final JwtUtil jwtUtil;

    // Métodos auxiliares de busca
    private Usuario getUsuarioById(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("Usuário não encontrado."));
    }

    private Usuario getUsuarioByEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFound("Usuário não encontrado.")
        );
    }

    private Endereco getEnderecoById(Long id){
        return enderecoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Endereço não encontrado."));
    }

    private Telefone getTelefoneById(Long id){
        return telefoneRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Telefone não encontrado."));
    }

    private Usuario getUsuarioAutenticadoByToken(String token){
        token = token.substring(7); // Retirando o "Bearer"
        String emailUsuario = jwtUtil.extractUsername(token);
        Usuario usuarioRequisitante = usuarioRepository.findByEmail(emailUsuario).orElseThrow(
                () -> new ResourceNotFound("Usuário não encontrado.")
        );

        return usuarioRequisitante;
    }

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha())); // Codificando password
        Usuario usuarioSalvar = usuarioRepository.save(usuarioConverter.dtoParaUsuario(usuarioDTO));
        return usuarioConverter.usuarioParaUsuarioDto(usuarioSalvar);
    }

    public UsuarioDTO buscarUsuario(Long id){
        Usuario usuarioBuscar = getUsuarioById(id);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioBuscar);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        Usuario usuarioBuacar = getUsuarioByEmail(email);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioBuacar);
    }

    public void deletarUsuario(Long id){
        getUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO atualizarDadosUsuario(UsuarioDTO dto, Long id){
        Usuario usuarioEntity = getUsuarioById(id);
        String senhaEncriptada = null;
        if(dto.getSenha() != null){
            senhaEncriptada = passwordEncoder.encode(dto.getSenha());
        }

        Usuario usuarioAtualizado = usuarioConverter.updateUsuario(usuarioEntity, dto, senhaEncriptada);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioRepository.save(usuarioAtualizado));
    }

    public EnderecoDTO atualizarDadosEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco enderecoAtualizado = usuarioConverter.updateEndereco(getEnderecoById(idEndereco), enderecoDTO);
        return usuarioConverter.enderecoParaEnderecoDto(enderecoRepository.save(enderecoAtualizado));
    }

    public TelefoneDTO atualizarDadosTelefone(Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone telefoneAtualizado = usuarioConverter.updateTelefone(getTelefoneById(idTelefone), telefoneDTO);
        return usuarioConverter.telefoneParaTelefoneDto(telefoneRepository.save(telefoneAtualizado));
    }

    public EnderecoDTO cadastrarEndereco(EnderecoDTO enderecoDTO, String token){
        Endereco enderecoEntity = usuarioConverter.enderecoDtoParaEndereco(
                enderecoDTO, getUsuarioAutenticadoByToken(token));
        Endereco enderecoCadastrado = enderecoRepository.save(enderecoEntity);
        return usuarioConverter.enderecoParaEnderecoDto(enderecoCadastrado);
    }

    public TelefoneDTO cadastrarTelefone(TelefoneDTO telefoneDTO, String token){
        Telefone telefoneEntity = usuarioConverter.telefoneDtoParaTelefone(
                telefoneDTO, getUsuarioAutenticadoByToken(token));
        Telefone telefoneCadastrado = telefoneRepository.save(telefoneEntity);
        return usuarioConverter.telefoneParaTelefoneDto(telefoneCadastrado);
    }

    public void emailExiste(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictException("O e-mail já existe.");
        }
    }
}