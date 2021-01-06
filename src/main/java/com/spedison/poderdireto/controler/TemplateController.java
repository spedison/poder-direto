package com.spedison.poderdireto.controler;

import com.spedison.poderdireto.bo.TemplateBO;
import com.spedison.poderdireto.model.ListaTemplates;
import com.spedison.poderdireto.model.ListaVariavelEmail;
import com.spedison.poderdireto.model.TelaTemplate;
import com.spedison.poderdireto.model.Template;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TemplateController {



    @Autowired
    TemplateBO templateBO;

    @Data
    @AllArgsConstructor
    static class ItemLista {
        private Long id;
        private String nome;
        private String dataCriacao;
    }

    private List<ItemLista> converteLista(ListaTemplates listaTemplates){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return listaTemplates.stream().
                map(i->new ItemLista(i.getId(),i.getNome(),simpleDateFormat.format(new Date(i.getId())))).
                collect(Collectors.toList());
    }

    @GetMapping(path = "/templates/edit/{nome_template}")
    public ModelAndView altera(@PathVariable("nome_template") String nomeTemplate){

        ModelAndView ret = new ModelAndView("template_editor");
        Template template = templateBO.carregaArquivoTemplate(nomeTemplate+".template");

        ret.addObject("helpVars", ListaVariavelEmail.getAllItens());
        ret.addObject("template", template);
        return ret;
    }

    @GetMapping(path = "/templates/add")
    public ModelAndView adiciona(){
        ModelAndView ret = new ModelAndView("template_editor");

        ret.addObject("template", new Template());
        ret.addObject("helpVars", ListaVariavelEmail.getAllItens());
        return ret;
    }

    // Tela com loistagem de templates (id, nome e acao)
    @GetMapping(path = {"/templates/", "/templates"})
    public ModelAndView listaTudo(){
        ModelAndView ret = new ModelAndView("template_listagem");
        ret.addObject("templates", converteLista(templateBO.listaTemplates()));
        return ret;
    }

    @PostMapping(path = {"/templates/add"})
    public ModelAndView grava(Template template){

        boolean ret ;

        if (template.getId() == -1){
            ret = templateBO.addTemplate(template);
        } else {
            ret = templateBO.editTemplate(template);
        }

        if (ret == false) {
            ModelAndView model = new ModelAndView("template_editor");
            model.addObject("template", template);
            model.addObject("helpVars", ListaVariavelEmail.getAllItens());
            return model;
        }

        return listaTudo();
    }

    @GetMapping(path = "/templates/remove/{nome_template}")
    public ModelAndView remove(@PathVariable("nome_template")String nomeTemplate){
        templateBO.remoteTemplate(nomeTemplate);
        return listaTudo();
    }

}
