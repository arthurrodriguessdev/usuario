package com.projeto.usuario.usuario.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="usuario")
@Builder
public class Usuario implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="nome", length=100)
    private String nome;
    @Column(name="email", length=100, unique=true)
    private String email;
    @Column(name="senha")
    private String senha;

    // Um usuário pode ter VÁRIOS endereços (OneToMany)
//    @OneToMany(cascade=CascadeType.ALL) // Se apagar o usuário, todos os endereços serão apagados também
    // Esse nome de campo ficará na tabela de Endereco

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

    // Relacionamento unidirecional
//  @OneToMany(cascade=CascadeType.ALL)
//  @JoinColumn(name="usuario_id", referencedColumnName="id")

    // Relacionamento bidirecional (endereço sabe quem é o usuário agora)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Telefone> telefones;

    // Métodos da classe UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
