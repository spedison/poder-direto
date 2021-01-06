package com.spedison.poderdireto.model;

import com.spedison.poderdireto.helper.FileSizeHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * Classe criada para mostrar as planilhas baixadas da internet na tela com nome, tamanho, unidade e data da atualização
 */

@Data
public class Planilha {
    private String local;
    private String nome;
    private String tamanho; // Já formatada com 3 casas decimais.
    private String unidade;
    private String dataAtualizacao;

    static private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Planilha(File f){

        local = f.getAbsolutePath();
        nome = f.getName();

        FileSizeHelper.Result r = FileSizeHelper.convertAsHuman(f.length());

        tamanho = String.format("%12.3f", r.getValue());
        unidade = r.getUnit();
        dataAtualizacao = sdf.format(new Date(f.lastModified()));

    }
}
