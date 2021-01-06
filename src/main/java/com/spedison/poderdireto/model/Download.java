package com.spedison.poderdireto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Download {
    private String url;
    private String nomeArquivo;
}
