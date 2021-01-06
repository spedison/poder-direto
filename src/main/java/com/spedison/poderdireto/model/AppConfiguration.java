package com.spedison.poderdireto.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {

    @Setter(AccessLevel.PRIVATE)
    @Getter
    @Value("${app.local_dir_config}")
    private String localDirConfig;

    @Value("${app.quantidade_de_emails_por_vez}")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private int quantidadeDeEmailsPorVez;

    @Value("${app.lista_arquivos_para_baixar}")
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String urlArquivoListaDownloads;

    @Override
    public String toString() {
        return "";
    }
}
