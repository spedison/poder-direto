package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.helper.FileSizeHelper;
import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.helper.DownloadHelper;
import com.spedison.poderdireto.model.Download;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
@Log4j2
@Data
public class DownloadPlanilhaBO implements DownloadHelper.ActionOnDownload {

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    DownloadHelper downloadHelper;

    @Autowired
    DownloadBO downloadBO;

    private boolean teveErro;
    private List<String> mensagem = new LinkedList<>();

    private String arquivoCorrente;
    private long tamanhoArquivoCorrente;

    public void downloadPlanilha(boolean forcaDownload) {

        List<Download> listDownloads = downloadBO.listarListaDownloads(true);

        downloadHelper.setActionOnDownload(this);

        teveErro = false;

        listDownloads.stream().forEach(dw -> {

            downloadHelper.setFilename(Paths.get(appConfiguration.getLocalDirConfig(), dw.getNomeArquivo()).toString());
            downloadHelper.setUrl(dw.getUrl()); //appConfiguration.getUrl_contatos());

            if (!downloadHelper.arquivoJaExiste() || forcaDownload) {
                downloadHelper.downloadArquivo();
                log.info("Download realizado");
            } else {
                mensagem.add("Download do arquivo " + dw.getNomeArquivo() + " Não precisou ser realizado, pois já existe o arquivo, force a atualização");
                log.info("Download não realizado, arquivo " + dw.getNomeArquivo() + " já existe");

            }
        });
    }

    protected boolean renameIfExists(String arquivoOri) {
        String arquivoDest = arquivoOri + "-" + (new Date()).toString();
        File f1 = new File(arquivoOri);
        File f2 = new File(arquivoDest);

        if (!f1.exists() || f2.exists())
            return false;

        return f1.renameTo(f2);
    }

    @Override
    public void onPreOk(String filename) {
        arquivoCorrente = filename;
        renameIfExists(filename);
    }

    @Override
    public void onPreNOk(int errorCode) {
        log.error("Servidor retornou erro " + errorCode);
        mensagem.add("O Servidor retornou cod de erro " + errorCode + " favor tentar novamente mais tarde.");
        teveErro = true;
    }

    @Override
    public void onDownload(int lastRead, int totalRead, int totalLen) {
        tamanhoArquivoCorrente = totalLen;
        log.info("Arquivo " + arquivoCorrente + " Download em progresso : " + totalRead + "/" + totalLen);
    }

    @Override
    public void onFinish() {
        FileSizeHelper.Result r = FileSizeHelper.convertAsHuman(tamanhoArquivoCorrente);
        log.info("Download do arquivo " + arquivoCorrente + " Terminou");
        mensagem.add(String.format("Download do arquivo %s terminado com sucesso. Tamanho do arquivo é %12.3f %s.", arquivoCorrente, r.getValue(), r.getUnit()));
    }

    @Override
    public void onExcetion(Exception e) {
        log.error("Problemas durante o Download :: do arquivo " + arquivoCorrente, e);
        mensagem.add( "Tivemos erros durante o download e/ou gravação do arquivo " + arquivoCorrente + " com erro " + e.getMessage());
        teveErro = true;
    }

}
