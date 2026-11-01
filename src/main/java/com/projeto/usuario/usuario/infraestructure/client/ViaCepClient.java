package com.projeto.usuario.usuario.infraestructure.client;

import com.projeto.usuario.usuario.business.dto.ViaCepResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "${client.url.viacep}")
public interface ViaCepClient {
    @GetMapping("/ws/{cep}/json/")
    ViaCepResponseDTO buscarDadosCep(@PathVariable String cep);
}