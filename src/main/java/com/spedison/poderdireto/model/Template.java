package com.spedison.poderdireto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Template {
    private long id;
    private String nome;
    private String subject; // Assunto
    private String fileName;
    private String conteudo;


    public Template() {
        id = -1;
        nome = "";
        fileName = "";
        conteudo = "";
        subject = "";
    }

}
