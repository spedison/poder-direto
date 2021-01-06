package com.spedison.poderdireto.controler;

import com.spedison.poderdireto.bo.AutoridadeBO;
import com.spedison.poderdireto.bo.DadosDeConfiguracaoDeEnvioBO;
import com.spedison.poderdireto.bo.EnviaEmailBO;
import com.spedison.poderdireto.bo.TemplateBO;
import com.spedison.poderdireto.model.AppConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@Log4j2
public class SendMailController {

    @Autowired
    DadosDeConfiguracaoDeEnvioBO dadosDeConfiguracaoDeEnvioBO;

    @Autowired
    TemplateBO templateBO;

    @Autowired
    AutoridadeBO autoridadeBO;

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    private EnviaEmailBO enviaEmailBO;

    private ModelAndView montaTela(boolean test) {

        ModelAndView ret = new ModelAndView("envio-de-email");
        ret.addObject("dados-envio", dadosDeConfiguracaoDeEnvioBO.carregaDadosEnvio());
        ret.addObject("templates", templateBO.listaTemplates());
        ret.addObject("orgaos", autoridadeBO.listaOrgaos());
        ret.addObject("teste", test);
        ret.addObject("simulado", false);
        return ret;
    }

    @GetMapping("/envio-email/test")
    ModelAndView telaTeste() {
        return montaTela(true);
    }

    @GetMapping({"/envio-email/", "/envio-email"})
    ModelAndView telaEnvio() {
        return montaTela(false);
    }

    @PostMapping("/envio-mail")
    ModelAndView preparandoParaEnvio(String senha, String template, String orgao, boolean teste, boolean simulacao, int inicio, int quantidade, HttpSession session) {

        ModelAndView ret;

        EnviaEmailBO.CtxDeEnvio ctx = enviaEmailBO.iniciaEstruturaEnvio(template, orgao, senha, teste, simulacao, inicio, quantidade);

        if (teste) {
            ret = new ModelAndView("teste-de-envio-resultado");
            ret.addObject("dados-envio", ctx.getDadosDeConfiguracaoDeEnvio());
            ret.addObject("template", ctx.getTemplate().getNome());
            ret.addObject("orgao", orgao);
            enviaEmailBO.enviar(ctx);
            ret.addObject("mensagem", enviaEmailBO.getMsgErros());
        } else {
            ret = new ModelAndView("progresso-envio-email");
            ret.addObject("percentual", 0);
            ret.addObject("repete", true);
            ret.addObject("enviados", 0);
            ret.addObject("quantidade", 0);
            session.setAttribute("ctxEnvioEmail", ctx);
        }

        return ret;
    }

    @PostMapping("/envio-email/next")
    ModelAndView preparandoParaEnvio(HttpSession session) {

        EnviaEmailBO.CtxDeEnvio ctxDeEnvio = (EnviaEmailBO.CtxDeEnvio) session.getAttribute("ctxEnvioEmail");

        if (ctxDeEnvio == null)
            return montaTela(true);

        enviaEmailBO.enviar(ctxDeEnvio);

        ModelAndView ret = new ModelAndView("progresso-envio-email");
        ret.addObject("percentual", (int) (ctxDeEnvio.getLastSend() * 100.0 / ctxDeEnvio.getQuantidade()));
        ret.addObject("enviados", ctxDeEnvio.getLastSend());
        ret.addObject("quantidade", ctxDeEnvio.getQuantidade());
        ret.addObject("repete", ctxDeEnvio.getLastSend() != ctxDeEnvio.getQuantidade());

        if(ctxDeEnvio.getLastSend() == ctxDeEnvio.getQuantidade()) {
            ctxDeEnvio.setSenha("");
            session.removeAttribute("ctxEnvioEmail");
        }

        return ret;
    }

}

//    @GetMapping("/mail/pre-send")
//    ModelAndView mostraTela(){
//        ModelAndView ret = new ModelAndView("envio-de-email");
//        DadosDaConfiguracaoDoEnvio de = dadosDaConfiguracaoDoEnvioBO.carregaDadosEnvio();
//        ret.addObject("nome",de.getNome());
//        ret.addObject("email",de.getEmail());
//        ret.addObject("smtp",de.getSmtp());
//        ret.addObject("templates",templateBO.listaTemplates());
//        ret.addObject("orgaos",autoridadeBO.listaOrgaos());
//        return ret;
//    }
//
//
//    @PostMapping("/mail/pre-send")
//    ModelAndView preparandoParaEnvio(String senha, String template, String orgao, Boolean teste, HttpSession session){
//
//        ModelAndView ret = new ModelAndView("progresso-envio-email");
//        Template t =  templateBO.carregaArquivoTemplate(template+".template");
//        session.setAttribute("senha", senha);
//        session.setAttribute("template", t);
//        session.setAttribute("templateMustache", Mustache.compiler().compile(t.getConteudo()));
//        session.setAttribute("lastSent",(Long)0L);
//        session.setAttribute("quantidade",autoridadeBO.contaEmailsDoOrgao(orgao));
//        session.setAttribute("orgao",orgao);
//        session.setAttribute("autoridades_errros", new LinkedList<Autoridade>());
//        session.setAttribute("listaAutoridades", autoridadeBO.carregaLista(orgao,teste));
//        session.setAttribute("teste", teste);
//
//        ret.addObject("percentual", 0);
//        ret.addObject("repete", true);
//        return ret;
//    }
//
//    private String montaTemplate(com.samskivert.mustache.Template templateMustache, Autoridade autoridade){
////        String text = "One, two, {{three}}. Three sir!";
//        com.samskivert.mustache.Template tmpl = Mustache.compiler().compile(template.getConteudo());
////        Map<String, String> data = new HashMap<>();
////        data.put("three", "five");
//        autoridade.carregaCamposCalculados();
//        return templateMustache.execute(autoridade);
//    }
//
//    private int enviaEmail(Autoridade autoridade, Template template, String msg){
//
//        log.debug("Enviando email para : " + autoridade.getEmail());
//
//        return 1;
//    }
//
//    @PostMapping("/mail/send")
//    ModelAndView enviaEmails(HttpSession session){
//
//        // Pega os dados da sessão
//        String senha = (String)session.getAttribute("senha");
//        Template template = (Template)session.getAttribute("template");
//        com.samskivert.mustache.Template templateMustache = (com.samskivert.mustache.Template) session.getAttribute("templateMustache");
//
//        int enviados = 0;
//
//        // Envia X emails a partir da quantidade do último envio
//        long limit = appConfiguration.getQuantidadeDeEmailsPorVez();
//        Long toSkip = (Long) session.getAttribute("lastSent");
//        LinkedList<Autoridade> autoridades = (LinkedList<Autoridade>) session.getAttribute("listaAutoridades");
//        for (Autoridade autoridade : autoridades) {
//            if (toSkip > 0L) {
//                toSkip--;
//                continue;
//            }
//            if (limit-- == 0)
//                break;
//            String msg = montaTemplate(templateMustache,autoridade);
//            enviados ++;
//            if (this.enviaEmail(autoridade, template, msg) != 1){
//                ((LinkedList<Autoridade>)session.getAttribute("autoridades_errros")).add(autoridade);
//            }
//            // Tempo entre o envio de um e-mail e outro para enviar a marcação de Span.
//            try{
//                Thread.sleep(300);
//            }catch (InterruptedException ie){}
//        }
//
//        // Atualiza a porcentagem da página e pede para repetir essa chamada.
//        session.setAttribute ( "lastSent", ((Long) session.getAttribute("lastSent")) + enviados);
//
//        Long totalParaEnviar = (Long) session.getAttribute("quantidade");
//        Long totalEnviado = (Long) session.getAttribute("lastSent");
//
//        //  Define se chama novamente ou não
//        ModelAndView ret = new ModelAndView("progresso-envio-email");
//        ret.addObject("percentual",(int)(totalEnviado * 100.0 / totalParaEnviar) );
//        ret.addObject("repete", (Boolean)(totalEnviado != totalParaEnviar) );
//
//        return ret;
//    }


//}
