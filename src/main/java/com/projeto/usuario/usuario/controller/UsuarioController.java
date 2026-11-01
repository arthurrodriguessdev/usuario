package com.projeto.usuario.usuario.controller;

import com.projeto.usuario.usuario.business.UsuarioService;
import com.projeto.usuario.usuario.business.ViaCepService;
import com.projeto.usuario.usuario.business.dto.endereco.EnderecoResponseDTO;
import com.projeto.usuario.usuario.business.dto.LoginRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneRequestDTO;
import com.projeto.usuario.usuario.business.dto.telefone.TelefoneResponseDTO;
import com.projeto.usuario.usuario.business.dto.ViaCepResponseDTO;
import com.projeto.usuario.usuario.business.dto.endereco.EnderecoRequestDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioRequestDTO;
import com.projeto.usuario.usuario.business.dto.usuario.UsuarioResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final ViaCepService viaCepService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvarUsuario(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.salvarUsuario(usuarioRequestDTO));
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
            @PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(usuarioRequestDTO, id, authenticatedUser));
    }

    @PutMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarDadosEndereco(
            @PathVariable Long id, @RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosEndereco(id, enderecoRequestDTO, authenticatedUser));
    }

    @PutMapping("/telefones/{id}")
    public ResponseEntity<TelefoneResponseDTO> atualizarDadosTelefone(
            @PathVariable Long id, @RequestBody @Valid TelefoneRequestDTO telefoneRequestDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser){
        return ResponseEntity.ok(usuarioService.atualizarDadosTelefone(id, telefoneRequestDTO, authenticatedUser));
    }

    @PostMapping("/enderecos")
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(
            @RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastrarEndereco(enderecoRequestDTO, authenticatedUser));
    }

    @PostMapping("/telefones")
    public ResponseEntity<TelefoneResponseDTO> cadastrarTelefone(
            @RequestBody @Valid TelefoneRequestDTO telefoneRequestDTO,
            @AuthenticationPrincipal UserDetails authenticatedUser
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.cadastrarTelefone(telefoneRequestDTO, authenticatedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(usuarioService.autenticarUsuario(loginRequestDTO));
    }

    @GetMapping("/enderecos/cep")
    public ResponseEntity<ViaCepResponseDTO> buscarDadosCep(@RequestParam("cep") String cep){
        return ResponseEntity.ok(viaCepService.buscarDadosCep(cep));
    }
}