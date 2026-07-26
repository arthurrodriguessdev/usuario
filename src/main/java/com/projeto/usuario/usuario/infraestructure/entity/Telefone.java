package com.projeto.usuario.usuario.infraestructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="telefone")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Telefone {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="numero", length = 10, nullable = false)
    private String numero;
    @Column(name="ddd", length = 3, nullable = false)
    private String ddd;
}
