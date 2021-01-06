package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.DadosDeConfiguracaoDeEnvio;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

@Component
@Log4j2
public class DadosDeConfiguracaoDeEnvioBO {

    @Autowired
    AppConfiguration appConfiguration;

    public File getConfigDadosEnvio() {
        File f = Paths.get(appConfiguration.getLocalDirConfig(), "config-envio.properties").toFile();
        return f;
    }

    public DadosDeConfiguracaoDeEnvio carregaDadosEnvio() {

        DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio = new DadosDeConfiguracaoDeEnvio();
        File f = getConfigDadosEnvio();
        if (!f.exists()) {
            return dadosDeConfiguracaoDeEnvio;
        } else {
            try {
                Properties prop = new Properties();
                FileInputStream fis = new FileInputStream(f);
                prop.load(fis);
                dadosDeConfiguracaoDeEnvio.setCPF(prop.getProperty("cpf"));
                dadosDeConfiguracaoDeEnvio.setNome(prop.getProperty("nome"));
                dadosDeConfiguracaoDeEnvio.setSmtp(prop.getProperty("smtp"));

                dadosDeConfiguracaoDeEnvio.setSmtpPort(Integer.parseInt(prop.getProperty("smtpPort", "465")));
                dadosDeConfiguracaoDeEnvio.setIntervalo(Integer.parseInt(prop.getProperty("intervalo", "500")));

                dadosDeConfiguracaoDeEnvio.setComAutenticacao(
                        prop.getProperty("comAutenticacao") == null ||
                        prop.getProperty("comAutenticacao").trim().compareToIgnoreCase("true") == 0 ||
                        prop.getProperty("comAutenticacao").trim().compareToIgnoreCase("1") == 0 ||
                        prop.getProperty("comAutenticacao").trim().compareToIgnoreCase("on") == 0 ||
                        prop.getProperty("comAutenticacao").trim().compareToIgnoreCase("liagado") == 0);
                dadosDeConfiguracaoDeEnvio.setEmail(prop.getProperty("email"));

                dadosDeConfiguracaoDeEnvio.setUsaSsl(
                        prop.getProperty("usaSsl") == null ||
                                prop.getProperty("usaSsl").trim().compareToIgnoreCase("true") == 0 ||
                                prop.getProperty("usaSsl").trim().compareToIgnoreCase("1") == 0 ||
                                prop.getProperty("usaSsl").trim().compareToIgnoreCase("on") == 0 ||
                                prop.getProperty("usaSsl").trim().compareToIgnoreCase("ligado") == 0);

                dadosDeConfiguracaoDeEnvio.setTlsHabilitado(
                        prop.getProperty("tlsHabilitado") == null ||
                                prop.getProperty("tlsHabilitado").trim().compareToIgnoreCase("true") == 0 ||
                                prop.getProperty("tlsHabilitado").trim().compareToIgnoreCase("1") == 0 ||
                                prop.getProperty("tlsHabilitado").trim().compareToIgnoreCase("on") == 0 ||
                                prop.getProperty("tlsHabilitado").trim().compareToIgnoreCase("ligado") == 0);

                dadosDeConfiguracaoDeEnvio.setTlsRequerido(
                        prop.getProperty("tlsRequerido") == null ||
                                prop.getProperty("tlsRequerido").trim().compareToIgnoreCase("true") == 0 ||
                                prop.getProperty("tlsRequerido").trim().compareToIgnoreCase("1") == 0 ||
                                prop.getProperty("tlsRequerido").trim().compareToIgnoreCase("on") == 0 ||
                                prop.getProperty("tlsRequerido").trim().compareToIgnoreCase("ligado") == 0);


                fis.close();
            } catch (FileNotFoundException fnfe) {
                log.error("Arquivo não encontrado", fnfe);
            } catch (IOException ioe) {
                log.error("Manipulação do arquivo com erro", ioe);
            }
        }
        return dadosDeConfiguracaoDeEnvio;
    }

    public void gravaDadosEnvio(DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio) {
        try {
            Properties prop = new Properties();
            FileOutputStream fos = new FileOutputStream(getConfigDadosEnvio());
            prop.setProperty("cpf", dadosDeConfiguracaoDeEnvio.getCPF());
            prop.setProperty("nome", dadosDeConfiguracaoDeEnvio.getNome());
            prop.setProperty("smtp", dadosDeConfiguracaoDeEnvio.getSmtp());
            prop.setProperty("smtpPort", Integer.toString(dadosDeConfiguracaoDeEnvio.getSmtpPort()));
            prop.setProperty("intervalo", Integer.toString(dadosDeConfiguracaoDeEnvio.getIntervalo()));
            prop.setProperty("comAutenticacao", Boolean.toString(dadosDeConfiguracaoDeEnvio.getComAutenticacao()));
            prop.setProperty("usaSsl", Boolean.toString(dadosDeConfiguracaoDeEnvio.getUsaSsl()));
            prop.setProperty("tlsHabilitado", Boolean.toString(dadosDeConfiguracaoDeEnvio.getTlsHabilitado()));
            prop.setProperty("tlsRequerido", Boolean.toString(dadosDeConfiguracaoDeEnvio.getTlsRequerido()));
            prop.setProperty("email", dadosDeConfiguracaoDeEnvio.getEmail());
            prop.save(fos, "Nao altere esse arquivo, ele pode ser sobreescrito pela aplicaçao na tela de dados do envio.");
            fos.close();
        } catch (FileNotFoundException fnfe) {
            log.error("Arquivo não encontrado", fnfe);
        } catch (IOException ioe) {
            log.error("Manipulação do arquivo com erro", ioe);
        }
    }
}
