package com.projeto.usuario.usuario.infraestructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="endereco")
public class Endereco {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "rua", length = 100)
    private String rua;
    @Column(name = "bairro", length = 100)
    private String bairro;
    @Column(name="cep", length = 9)
    private String cep;
    @Column(name="cidade", length = 9)
    private String cidade;
}
