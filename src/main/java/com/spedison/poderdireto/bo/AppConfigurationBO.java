package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.model.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

@Component
public class AppConfigurationBO {

    @Autowired
    AppConfiguration appConfiguration;


    public File [] listaArquivosDirConfiguracao(String extensao) {

        File dirConfig = new File(appConfiguration.getLocalDirConfig());

        File[] arquivos = dirConfig.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".xlsx");
            }
        });
        return arquivos;
    }

    public File [] listaPlanilhas(){
        return listaArquivosDirConfiguracao(".xlsx");
    }


    public File [] listaTemplates(){
        return listaArquivosDirConfiguracao(".template");
    }

}
