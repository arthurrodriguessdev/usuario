package com.projeto.usuario.usuario.infraestructure.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="telefone")
@Builder
public class Telefone {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="numero", length = 10, nullable = false)
    private String numero;
    @Column(name="ddd", length = 3, nullable = false)
    private String ddd;
}
