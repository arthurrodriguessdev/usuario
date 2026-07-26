package com.projeto.usuario.usuario.business.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // Essa anotação permite o padrão builder, usado para criação de objetos de forma mais simples
public class EnderecoDTO {
    private String rua;
    private String bairro;
    private String cep;
    private String cidade;
}
