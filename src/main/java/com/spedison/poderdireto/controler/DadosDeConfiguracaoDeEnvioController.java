package com.spedison.poderdireto.controler;

import com.spedison.poderdireto.bo.DadosDeConfiguracaoDeEnvioBO;
import com.spedison.poderdireto.model.DadosDeConfiguracaoDeEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class DadosDeConfiguracaoDeEnvioController {

    @Autowired
    DadosDeConfiguracaoDeEnvioBO dadosDeConfiguracaoDeEnvioBO;

    private static String pagina = "dados-de-configuracao-de-envio";

    @GetMapping("/dados-de-envio")
    public ModelAndView telaDadosEnvio() {
        ModelAndView ret = new ModelAndView(pagina);
        DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio = dadosDeConfiguracaoDeEnvioBO.carregaDadosEnvio();
        ret.addObject("dados-envio", dadosDeConfiguracaoDeEnvio);
        return ret;
    }

    @PostMapping("/dados-de-envio")
    public ModelAndView gravaDadosEnvio (@Valid DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio){
        ModelAndView ret = new ModelAndView("index");
        dadosDeConfiguracaoDeEnvioBO.gravaDadosEnvio(dadosDeConfiguracaoDeEnvio);
        return ret;
    }
}
