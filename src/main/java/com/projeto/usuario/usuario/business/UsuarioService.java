package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.LoginDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioResponseDTO;
import com.projeto.usuario.usuario.exception.ConflictException;
import com.projeto.usuario.usuario.exception.ResourceNotFound;
import com.projeto.usuario.usuario.exception.UnauthorizedException;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import com.projeto.usuario.usuario.infraestructure.repository.EnderecoRepository;
import com.projeto.usuario.usuario.infraestructure.repository.TelefoneRepository;
import com.projeto.usuario.usuario.infraestructure.repository.UsuarioRepository;
import com.projeto.usuario.usuario.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private static final String USUARIO_NOT_FOUND_MESSAGE = "Usuário não encontrado";

    // Métodos auxiliares de busca
    private Usuario getUsuarioById(Long id){
        return usuarioRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound(USUARIO_NOT_FOUND_MESSAGE));
    }

    private Usuario getUsuarioByEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFound(USUARIO_NOT_FOUND_MESSAGE)
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

    private Usuario getUsuarioAutenticado(UserDetails authenticatedUser){
        return getUsuarioByEmail(authenticatedUser.getUsername());
    }

    public UsuarioResponseDTO salvarUsuario(UsuarioDTO usuarioDTO){
        if(usuarioRepository.existsByEmail(usuarioDTO.email())) {
            throw new ConflictException("Já existe um usuário cadastrado com o e-mail informado");
        }

        // Instanciando novo dto record para fazer a codificação de senha
        UsuarioDTO novoUsuarioDto = new UsuarioDTO(
                usuarioDTO.nome(),
                usuarioDTO.email(),
                passwordEncoder.encode(usuarioDTO.senha()),
                usuarioDTO.enderecos(), usuarioDTO.telefones()
        );

        Usuario usuarioSalvar = usuarioRepository.save(usuarioConverter.dtoParaUsuario(novoUsuarioDto));
        return usuarioConverter.usuarioParaUsuarioDto(usuarioSalvar);
    }

    public UsuarioResponseDTO buscarUsuario(Long id, UserDetails authenticatedUser){
        Usuario usuario = getUsuarioById(id);
        verificarProprietario(usuario, authenticatedUser);
        return usuarioConverter.usuarioParaUsuarioDto(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorEmail(String email, UserDetails authenticatedUser){
        if(!email.equals(authenticatedUser.getUsername())){
            throw new ResourceNotFound(USUARIO_NOT_FOUND_MESSAGE);
        }
        return usuarioConverter.usuarioParaUsuarioDto(getUsuarioByEmail(email));
    }

    public void deletarUsuario(Long id, UserDetails authenticatedUser){
        verificarProprietario(getUsuarioById(id), authenticatedUser);
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO atualizarDadosUsuario(UsuarioDTO dto, Long id, UserDetails authenticatedUser){
        Usuario usuarioEntity = getUsuarioById(id);
        verificarProprietario(usuarioEntity, authenticatedUser);
        String senhaEncriptada = null;
        if(dto.senha() != null){
            senhaEncriptada = passwordEncoder.encode(dto.senha());
        }

        Usuario usuarioAtualizado = usuarioConverter.updateUsuario(usuarioEntity, dto, senhaEncriptada);
        return usuarioConverter.usuarioParaUsuarioDto(usuarioRepository.save(usuarioAtualizado));
    }

    public EnderecoDTO atualizarDadosEndereco(
            Long idEndereco,
            EnderecoDTO enderecoDTO,
            UserDetails authenticatedUser
    ){
        Endereco endereco = getEnderecoById(idEndereco);
        verificarProprietario(endereco.getUsuario(), authenticatedUser, "Endereço não encontrado.");
        Endereco enderecoAtualizado = usuarioConverter.updateEndereco(endereco, enderecoDTO);
        return usuarioConverter.enderecoParaEnderecoDto(enderecoRepository.save(enderecoAtualizado));
    }

    public TelefoneDTO atualizarDadosTelefone(
            Long idTelefone,
            TelefoneDTO telefoneDTO,
            UserDetails authenticatedUser
    ){
        Telefone telefone = getTelefoneById(idTelefone);
        verificarProprietario(telefone.getUsuario(), authenticatedUser, "Telefone não encontrado.");
        Telefone telefoneAtualizado = usuarioConverter.updateTelefone(telefone, telefoneDTO);
        return usuarioConverter.telefoneParaTelefoneDto(telefoneRepository.save(telefoneAtualizado));
    }

    public EnderecoDTO cadastrarEndereco(EnderecoDTO enderecoDTO, UserDetails authenticatedUser){
        Endereco enderecoEntity = usuarioConverter.enderecoDtoParaEndereco(
                enderecoDTO, getUsuarioAutenticado(authenticatedUser));
        Endereco enderecoCadastrado = enderecoRepository.save(enderecoEntity);
        return usuarioConverter.enderecoParaEnderecoDto(enderecoCadastrado);
    }

    public TelefoneDTO cadastrarTelefone(TelefoneDTO telefoneDTO, UserDetails authenticatedUser){
        Telefone telefoneEntity = usuarioConverter.telefoneDtoParaTelefone(
                telefoneDTO, getUsuarioAutenticado(authenticatedUser));
        Telefone telefoneCadastrado = telefoneRepository.save(telefoneEntity);
        return usuarioConverter.telefoneParaTelefoneDto(telefoneCadastrado);
    }

    private void verificarProprietario(Usuario usuario, UserDetails authenticatedUser){
        verificarProprietario(usuario, authenticatedUser, USUARIO_NOT_FOUND_MESSAGE);
    }

    private void verificarProprietario(Usuario usuario, UserDetails authenticatedUser, String notFoundMessage){
        if(usuario == null || !authenticatedUser.getUsername().equals(usuario.getEmail())){
            throw new ResourceNotFound(notFoundMessage);
        }
    }

    public String autenticarUsuario(LoginDTO loginDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(), loginDTO.senha()
                    )
            );

            log.info("Usuário: {} autenticado com sucesso", loginDTO.email());
            return jwtUtil.generateToken(authentication.getName());

        } catch(BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e){
            log.error("Falha na autenticação do usuário {}. Erro: {}", loginDTO.email(), e.getMessage());
            throw new UnauthorizedException("Usuário ou senha inválido", e.getCause());
        }
    }
}