package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.converter.UsuarioConverter;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.exception.ConflictException;
import com.projeto.usuario.usuario.exception.ResourceNotFound;
import com.projeto.usuario.usuario.infraestructure.entity.Usuario;
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

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuarioSalvar = usuarioRepository.save(usuarioConverter.dtoParaUsuario(usuarioDTO));
        return usuarioConverter.usuarioParaUsuarioDto(usuarioSalvar);
    }

    public UsuarioDTO buscarUsuario(Long id){
        Usuario usuarioBuscar = usuarioRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("Usuário não encontrado"));

        return usuarioConverter.usuarioParaUsuarioDto(usuarioBuscar);
    }

    public void deletarUsuario(Long id){
        usuarioRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Usuário não encontrado"));

        usuarioRepository.deleteById(id);
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