package com.projeto.usuario.usuario.controller;

import com.projeto.usuario.usuario.business.UsuarioService;
import com.projeto.usuario.usuario.business.dto.*;
import com.projeto.usuario.usuario.infraestructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO usuarioDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.salvarUsuario(usuarioDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarUsuario(id));
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarDadosUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDto){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(usuarioDto, id));
    }

    @PutMapping("/enderecos/{id}")
    public ResponseEntity<EnderecoDTO> atualizarDadosEndereco(@PathVariable Long id, @RequestBody EnderecoDTO enderecoDTO){
        return ResponseEntity.ok(usuarioService.atualizarDadosEndereco(id, enderecoDTO));
    }

    @PutMapping("/telefones/{id}")
    public ResponseEntity<TelefoneDTO> atualizarDadosTelefone(@PathVariable Long id, @RequestBody TelefoneDTO telefoneDTO){
        return ResponseEntity.ok(usuarioService.atualizarDadosTelefone(id, telefoneDTO));
    }

    @PostMapping("/enderecos")
    public ResponseEntity<EnderecoDTO> cadastrarEndereco(
            @RequestBody EnderecoDTO enderecoDTO, @RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarEndereco(enderecoDTO, token));
    }

    @PostMapping("/telefones")
    public ResponseEntity<TelefoneDTO> cadastrarTelefone(
            @RequestBody TelefoneDTO telefoneDTO, @RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarTelefone(telefoneDTO, token));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(), loginDTO.getSenha()
                )
        );

        return ResponseEntity.ok(jwtUtil.generateToken(authentication.getName()));
    }

    // Refact
    @GetMapping("/token/validade")
    public ResponseEntity<Boolean> verificarValidadeToken(@RequestParam("token") String token){
        return ResponseEntity.ok(usuarioService.verificarValidadeToken(token));
    }
}