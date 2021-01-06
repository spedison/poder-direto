package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.ListaTemplates;
import com.spedison.poderdireto.model.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@Log4j2
public class TemplateBO {

    @Autowired
    ListaTemplates listaTemplates;

    @Autowired
    AppConfiguration appConfiguration;

    public boolean addTemplate(Template template) {
        template.setFileName(template.getNome() + ".template");

        if (Paths.get(appConfiguration.getLocalDirConfig(), template.getFileName()).toFile().exists())
            return false;

        template.setId(System.currentTimeMillis());

        return editTemplate(template);
    }

    public boolean editTemplate(Template template) {
        try {
            FileOutputStream fos = new FileOutputStream(Paths.get(appConfiguration.getLocalDirConfig(), template.getFileName()).toFile());
            PrintWriter pw = new PrintWriter(fos);
            pw.println("##as primeiras linhas que iniciarem com # serão ignoradas");
            pw.println("id=" + template.getId());
            pw.println("nome=" + template.getNome());
            pw.println("assunto=" + template.getSubject());
            pw.println("filename=" + template.getFileName());
            pw.println("conteudo=\n\n");
            pw.println(template.getConteudo());
            pw.close();
            return true;
        } catch (IOException ioe) {
            log.error("Erro ao gravar aquivo de template.", ioe);
            return false;
        }
    }

    public boolean deleteTemplate(Template template) {
        File f = Paths.get(appConfiguration.getLocalDirConfig(), template.getFileName()).toFile();
        if (!f.exists())
            return false;
        return f.delete();
    }

    public ListaTemplates listaTemplates() {
        return carregaLista();
    }

    public Template carregaArquivoTemplate(String nomeArquivo) {
        return carregaArquivoTemplate(nomeArquivo, true);
    }

    public Template carregaArquivoTemplate(String nomeArquivo, boolean carregaOTemplate) {
        Template template = new Template();
        try {
            FileInputStream fis = new FileInputStream(Paths.get(appConfiguration.getLocalDirConfig(), nomeArquivo).toFile());
            Scanner scanner = new Scanner(fis);

            boolean ehConteudo = false;
            StringBuffer conteudo = new StringBuffer();
            while (scanner.hasNextLine()) {

                String linha = scanner.nextLine();

                if (ehConteudo) {
                    conteudo.append(linha);
                    conteudo.append("\n");
                    continue;
                }

                if (linha.startsWith("#"))
                    continue;

                if (linha.startsWith("id=")) {
                    linha = linha.replace("id=", "");
                    template.setId(Long.parseLong(linha.trim()));
                } else if (linha.startsWith("nome=")) {
                    linha = linha.replace("nome=", "");
                    template.setNome(linha);
                } else if (linha.startsWith("assunto=")) {
                    linha = linha.replace("assunto=", "");
                    template.setSubject(linha);
                } else if (linha.startsWith("filename=")) {
                    linha = linha.replace("filename=", "");
                    template.setFileName(linha.trim());
                } else if (linha.startsWith("conteudo=")) {
                    ehConteudo = true;
                    if(carregaOTemplate == false)
                        break;
                    continue;
                }
            }
            template.setConteudo(conteudo.toString());
            scanner.close();
            return template;
        } catch (IOException ioe) {
            log.error("Erro ao gravar aquivo de template.", ioe);
            template.setId(-1);
            return template;
        }
    }

    public boolean remoteTemplate(String nomeTemplate) {
        File f = Paths.get(appConfiguration.getLocalDirConfig(), nomeTemplate + ".template").toFile();
        if (f.exists())
            return f.delete();
        return false;
    }


    public ListaTemplates carregaLista() {

        ListaTemplates ret = new ListaTemplates();

        File f = new File(appConfiguration.getLocalDirConfig());
        Arrays.stream(f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.getAbsolutePath().endsWith(".template"));
            }
        })).map(fi -> carregaArquivoTemplate(fi.getName(),false)).
                forEach(i -> ret.add(i));

        return ret;
    }


}
