package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.helper.DownloadHelper;
import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.Download;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Component
@Log4j2
public class DownloadBO implements DownloadHelper.ActionOnDownload {

    @Autowired
    DownloadHelper downloadHelper;

    @Autowired
    AppConfiguration appConfiguration;

    private boolean temErro;
    static final String downloadFileName = "download.csv";


    public List<Download> listarListaDownloads(boolean force) {

        List<Download> listaRet = new LinkedList<>();

        File arq = Paths.get(appConfiguration.getLocalDirConfig(), downloadFileName).toFile();

        if (!arq.exists() || force) {
            downloadLista();
            if (temErro)
                return listaRet;
        }

        carregaLista(arq, listaRet);

        return listaRet;
    }

    private void carregaLista(File arq, List<Download> listaRet) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(arq));
            reader.
                    lines().
                    filter(s -> !s.trim().isEmpty()).
                    filter(s -> !s.trim().startsWith("##")).
                    map(s-> s.split("[;]")).
                    filter(s->s.length == 2).
                    map(s-> new Download(s[0],s[1])).
                    forEach(listaRet::add);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadLista() {
        temErro = false;
        downloadHelper.setUrl(appConfiguration.getUrlArquivoListaDownloads());
        downloadHelper.setFilename(Paths.get(appConfiguration.getLocalDirConfig(), downloadFileName).toString());
        downloadHelper.setActionOnDownload(this);
        downloadHelper.downloadArquivo();
    }


    @Override
    public void onPreOk(String filename) {
        log.info("Realizando download e gravando em o arquivo" + filename);
    }

    @Override
    public void onPreNOk(int errorCode) {
        temErro = true;
    }

    @Override
    public void onDownload(int lastRead, int totalRead, int totalLen) {
        log.debug("Executando o download : " + downloadHelper.getFilename() + " - Tamanho Total = " + totalLen + " - Tamanho Baixado " + totalRead);
    }

    @Override
    public void onFinish() {
        log.debug("Processo de download terminado.");
    }

    @Override
    public void onExcetion(Exception e) {
        temErro = true;
        log.error("Problema durante o download da tabela de arquivos.", e);
    }
}
