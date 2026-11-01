package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.converter.UsuarioMapper;
import com.projeto.usuario.usuario.business.dto.endereco.EnderecoRequestDTO;
import com.projeto.usuario.usuario.business.dto.endereco.EnderecoResponseDTO;
import com.projeto.usuario.usuario.business.dto.LoginRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneResponseDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioRequestDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioResponseDTO;
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
    private final UsuarioMapper usuarioMapper;
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

    public UsuarioResponseDTO salvarUsuario(UsuarioRequestDTO usuarioRequestDTO){
        if(usuarioRepository.existsByEmail(usuarioRequestDTO.email())) {
            throw new ConflictException("Já existe um usuário cadastrado com o e-mail informado");
        }

        Usuario usuarioSalvar = usuarioMapper.toEntity(usuarioRequestDTO);
        usuarioSalvar.setSenha(passwordEncoder.encode(usuarioRequestDTO.senha()));
        associarDependentes(usuarioSalvar);
        return usuarioMapper.toResponse(usuarioRepository.save(usuarioSalvar));
    }

    public UsuarioResponseDTO buscarUsuario(Long id, UserDetails authenticatedUser){
        Usuario usuario = getUsuarioById(id);
        verificarProprietario(usuario, authenticatedUser);
        return usuarioMapper.toResponse(usuario);
    }

    public UsuarioResponseDTO buscarUsuarioPorEmail(String email, UserDetails authenticatedUser){
        if(!email.equals(authenticatedUser.getUsername())){
            throw new ResourceNotFound(USUARIO_NOT_FOUND_MESSAGE);
        }
        return usuarioMapper.toResponse(getUsuarioByEmail(email));
    }

    public void deletarUsuario(Long id, UserDetails authenticatedUser){
        verificarProprietario(getUsuarioById(id), authenticatedUser);
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponseDTO atualizarDadosUsuario(
            UsuarioRequestDTO usuarioRequestDTO,
            Long id,
            UserDetails authenticatedUser
    ){
        Usuario usuarioEntity = getUsuarioById(id);
        verificarProprietario(usuarioEntity, authenticatedUser);
        if(usuarioRequestDTO.senha() != null){
            usuarioEntity.setSenha(passwordEncoder.encode(usuarioRequestDTO.senha()));
        }

        usuarioMapper.updateUsuario(usuarioRequestDTO, usuarioEntity);
        return usuarioMapper.toResponse(usuarioRepository.save(usuarioEntity));
    }

    public EnderecoResponseDTO atualizarDadosEndereco(
            Long idEndereco,
            EnderecoRequestDTO enderecoRequestDTO,
            UserDetails authenticatedUser
    ){
        Endereco endereco = getEnderecoById(idEndereco);
        verificarProprietario(endereco.getUsuario(), authenticatedUser, "Endereço não encontrado.");
        usuarioMapper.updateEndereco(enderecoRequestDTO, endereco);
        return usuarioMapper.toResponse(enderecoRepository.save(endereco));
    }

    public TelefoneResponseDTO atualizarDadosTelefone(
            Long idTelefone,
            TelefoneRequestDTO telefoneRequestDTO,
            UserDetails authenticatedUser
    ){
        Telefone telefone = getTelefoneById(idTelefone);
        verificarProprietario(telefone.getUsuario(), authenticatedUser, "Telefone não encontrado.");
        usuarioMapper.updateTelefone(telefoneRequestDTO, telefone);
        return usuarioMapper.toResponse(telefoneRepository.save(telefone));
    }

    public EnderecoResponseDTO cadastrarEndereco(
            EnderecoRequestDTO enderecoRequestDTO,
            UserDetails authenticatedUser
    ){
        Endereco enderecoEntity = usuarioMapper.toEntity(enderecoRequestDTO);
        enderecoEntity.setUsuario(getUsuarioAutenticado(authenticatedUser));
        Endereco enderecoCadastrado = enderecoRepository.save(enderecoEntity);
        return usuarioMapper.toResponse(enderecoCadastrado);
    }

    public TelefoneResponseDTO cadastrarTelefone(TelefoneRequestDTO telefoneRequestDTO, UserDetails authenticatedUser){
        Telefone telefoneEntity = usuarioMapper.toEntity(telefoneRequestDTO);
        telefoneEntity.setUsuario(getUsuarioAutenticado(authenticatedUser));
        Telefone telefoneCadastrado = telefoneRepository.save(telefoneEntity);
        return usuarioMapper.toResponse(telefoneCadastrado);
    }

    private void associarDependentes(Usuario usuario) {
        if (usuario.getEnderecos() != null) {
            usuario.getEnderecos().forEach(endereco -> endereco.setUsuario(usuario));
        }
        if (usuario.getTelefones() != null) {
            usuario.getTelefones().forEach(telefone -> telefone.setUsuario(usuario));
        }
    }

    private void verificarProprietario(Usuario usuario, UserDetails authenticatedUser){
        verificarProprietario(usuario, authenticatedUser, USUARIO_NOT_FOUND_MESSAGE);
    }

    private void verificarProprietario(Usuario usuario, UserDetails authenticatedUser, String notFoundMessage){
        if(usuario == null || !authenticatedUser.getUsername().equals(usuario.getEmail())){
            throw new ResourceNotFound(notFoundMessage);
        }
    }

    public String autenticarUsuario(LoginRequestDTO loginRequestDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.email(), loginRequestDTO.senha()
                    )
            );

            log.info("Usuário: {} autenticado com sucesso", loginRequestDTO.email());
            return jwtUtil.generateToken(authentication.getName());

        } catch(BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException e){
            log.error("Falha na autenticação do usuário {}. Erro: {}", loginRequestDTO.email(), e.getMessage());
            throw new UnauthorizedException("Usuário ou senha inválido", e.getCause());
        }
    }
}