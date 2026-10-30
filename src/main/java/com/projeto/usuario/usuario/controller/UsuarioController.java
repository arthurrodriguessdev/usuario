package com.projeto.usuario.usuario.controller;

import com.projeto.usuario.usuario.business.UsuarioService;
import com.projeto.usuario.usuario.business.ViaCepService;
import com.projeto.usuario.usuario.business.dto.EnderecoDTO;
import com.projeto.usuario.usuario.business.dto.LoginDTO;
import com.projeto.usuario.usuario.business.dto.TelefoneDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioDTO;
import com.projeto.usuario.usuario.business.dto.UsuarioResponseDTO;
import com.projeto.usuario.usuario.business.dto.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ViaCepService viaCepService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.salvarUsuario(usuarioDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.buscarUsuario(id, authenticatedUser));
    }

    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(
            @RequestParam("email") String email, @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email, authenticatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails authenticatedUser){
        usuarioService.deletarUsuario(id, authenticatedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarDadosUsuario(
            @PathVariable Long id, @RequestBody UsuarioDTO usuarioDto,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(usuarioDto, id, authenticatedUser));
    }

    @PutMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoDTO> atualizarDadosEndereco(
            @PathVariable Long id, @RequestBody EnderecoDTO enderecoDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosEndereco(id, enderecoDTO, authenticatedUser));
    }

    @PutMapping("/telefones/{id}")
    public ResponseEntity<TelefoneDTO> atualizarDadosTelefone(
            @PathVariable Long id, @RequestBody TelefoneDTO telefoneDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosTelefone(id, telefoneDTO, authenticatedUser));
    }

    @PostMapping("/enderecos")
    public ResponseEntity<EnderecoDTO> cadastrarEndereco(
            @RequestBody EnderecoDTO enderecoDTO, @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastrarEndereco(enderecoDTO, authenticatedUser));
    }

    @PostMapping("/telefones")
    public ResponseEntity<TelefoneDTO> cadastrarTelefone(
            @RequestBody TelefoneDTO telefoneDTO, @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastrarTelefone(telefoneDTO, authenticatedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(usuarioService.autenticarUsuario(loginDTO));
    }

    @GetMapping("/enderecos/cep")
    public ResponseEntity<ViaCepDTO> buscarDadosCep(@RequestParam("cep") String cep){
        return ResponseEntity.ok(viaCepService.buscarDadosCep(cep));
    }
}