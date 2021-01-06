package com.spedison.poderdireto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DadosDeConfiguracaoDeEnvio {

    public DadosDeConfiguracaoDeEnvio(){
        nome = "";
        CPF = "";
        email = "";
        smtp = "smtp.gmail.com";
        smtpPort = 465;
        comAutenticacao = true;
        intervalo = 500;
        quantidadePorEnvio = 10;
        tlsRequerido = false;
        tlsHabilitado = false;
        usaSsl = true;
    }


    @NotBlank
    public String nome;
    private String CPF;
    @NotBlank
    private String email;
    @NotBlank

    private String smtp;

    private Integer smtpPort;

    private Boolean comAutenticacao;

    private Integer intervalo;

    private Boolean tlsRequerido;
    private Boolean tlsHabilitado;

    private Boolean usaSsl;
    private Integer quantidadePorEnvio;
}
