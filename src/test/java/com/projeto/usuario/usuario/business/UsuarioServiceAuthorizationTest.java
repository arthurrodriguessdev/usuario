package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.exception.ResourceNotFound;
import com.projeto.usuario.usuario.infraestructure.entity.Endereco;
import com.projeto.usuario.usuario.infraestructure.entity.Telefone;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
import com.projeto.usuario.usuario.infraestructure.repository.EnderecoRepository;
import com.projeto.usuario.usuario.infraestructure.repository.TelefoneRepository;
import com.projeto.usuario.usuario.infraestructure.repository.UsuarioRepository;
import com.projeto.usuario.usuario.infraestructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioServiceAuthorizationTest {
    private UsuarioRepository usuarioRepository;
    private EnderecoRepository enderecoRepository;
    private TelefoneRepository telefoneRepository;
    private UsuarioService usuarioService;
    private final UserDetails authenticatedUser = User.withUsername("ana@example.com")
            .password("unused")
            .authorities("USER")
            .build();

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        enderecoRepository = mock(EnderecoRepository.class);
        telefoneRepository = mock(TelefoneRepository.class);
        usuarioService = new UsuarioService(
                usuarioRepository,
                mock(UsuarioConverter.class),
                mock(PasswordEncoder.class),
                enderecoRepository,
                telefoneRepository,
                mock(JwtUtil.class),
                mock(AuthenticationManager.class)
        );
    }

    @Test
    void rejectsAccessToAnotherUser() {
        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(Usuario.builder().email("bruno@example.com").build()));

        assertThrows(ResourceNotFound.class, () -> usuarioService.buscarUsuario(2L, authenticatedUser));
    }

    @Test
    void rejectsUpdatesToAnotherUsersAddress() {
        Usuario owner = Usuario.builder().email("bruno@example.com").build();
        when(enderecoRepository.findById(4L))
                .thenReturn(Optional.of(Endereco.builder().usuario(owner).build()));

        assertThrows(ResourceNotFound.class,
                () -> usuarioService.atualizarDadosEndereco(4L, new EnderecoDTO(null, null, null, null, null),
                        authenticatedUser));
    }

    @Test
    void rejectsUpdatesToAnotherUsersPhone() {
        Usuario owner = Usuario.builder().email("bruno@example.com").build();
        when(telefoneRepository.findById(5L))
                .thenReturn(Optional.of(Telefone.builder().usuario(owner).build()));

        assertThrows(ResourceNotFound.class,
                () -> usuarioService.atualizarDadosTelefone(5L, null, authenticatedUser));
    }
}
