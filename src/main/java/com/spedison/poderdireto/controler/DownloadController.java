package com.spedison.poderdireto.controler;

import com.spedison.poderdireto.bo.AppConfigurationBO;
import com.spedison.poderdireto.bo.DownloadPlanilhaBO;
import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.helper.FileSizeHelper;
import com.spedison.poderdireto.model.Planilha;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
public class DownloadController {

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private AppConfigurationBO appConfigurationBO;

    @Autowired
    DownloadPlanilhaBO downloadPlanilhaBO;

    @GetMapping("/download/localPlanilha")
    public ModelAndView localPlanilha() {
        // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        File[] arquivos = appConfigurationBO.listaPlanilhas();

        List<Planilha> lstaPlanilhas = Arrays.stream(arquivos).map(f -> new Planilha(f)).collect(Collectors.toList());

        ModelAndView ret = new ModelAndView("local_planilha");


        ret.addObject("planilhas", lstaPlanilhas);

        //File f = new File(appConfiguration.getLocalArquivoContatos());

        //ret.addObject("localPlanilha", appConfiguration.getLocalArquivoContatos());
        //ret.addObject("dataAtualizacaoPlanilha", sdf.format(new Date(f.lastModified())) );

        //FileSizeHelper.Result r = FileSizeHelper.convertAsHuman(f.length());
        //ret.addObject("tamanho", String.format("%15.3f",r.getValue()));
        //ret.addObject("unidade", r.getUnit());
        //ret.addObject("dataAtualizacaoPlanilha", sdf.format(new Date(f.lastModified())) );

        return ret;
    }

    private ModelAndView realizaDownload(boolean forcado) {

        downloadPlanilhaBO.getMensagem().clear();
        downloadPlanilhaBO.downloadPlanilha(forcado);

        // File f = new File(appConfiguration.getLocalArquivoContatos());

        ModelAndView ret = new ModelAndView("download");

        ret.addObject("mensagens", downloadPlanilhaBO.getMensagem());
        ret.addObject("sucesso", !downloadPlanilhaBO.isTeveErro());

        /*FileSizeHelper.Result r = FileSizeHelper.convertAsHuman(f.length());
        ret.addObject("tamanho", String.format("%15.3f",r.getValue()) );
        ret.addObject("unidade", r.getUnit());*/

        return ret;

    }

    @GetMapping("/download")
    public ModelAndView download() {
        return realizaDownload(false);
    }

    @GetMapping("/download/forca")
    public ModelAndView downloadForca() {
        return realizaDownload(true);
    }
}
