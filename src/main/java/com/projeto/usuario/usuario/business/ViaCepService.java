package com.projeto.usuario.usuario.business;

import com.projeto.usuario.usuario.business.dto.ViaCepDTO;
import com.projeto.usuario.usuario.infraestructure.client.ViaCepClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ViaCepService {
    private final ViaCepClient viaCepClient;

    private String processarCep(String cep){
        cep = cep.replace(" ", "");
        if(cep.contains("-")){
            cep = cep.replace("-", "").trim();
        }

        if(cep.length() != 8){
            throw new IllegalArgumentException("O número de CEP deve conter exatamente 8 dígitos");
        }

        Pattern padraoCep = Pattern.compile("^[0-9]{8}$");
        Matcher matcher = padraoCep.matcher(cep);
        if(!matcher.matches()){
            throw new IllegalArgumentException("O número de CEP deve conter apenas dígitos");
        }

        return cep;
    }

    public ViaCepDTO buscarDadosCep(String cep){
        return viaCepClient.buscarDadosCep(processarCep(cep));
    }
}