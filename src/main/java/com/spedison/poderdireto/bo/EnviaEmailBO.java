package com.spedison.poderdireto.bo;

import com.samskivert.mustache.Mustache;
import com.spedison.poderdireto.helper.EnviadorDeEmailHelper;
import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.Autoridade;
import com.spedison.poderdireto.model.DadosDeConfiguracaoDeEnvio;
import com.spedison.poderdireto.model.Template;
import com.sun.el.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class EnviaEmailBO {

    @Autowired
    private DadosDeConfiguracaoDeEnvioBO dadosDeConfiguracaoDeEnvioBO;

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private AutoridadeBO autoridadeBO;

    @Autowired
    private TemplateBO templateBO;

    private List<String> msgErros = new LinkedList<>();

    @Data
    public static class CtxDeEnvio {
        private AppConfiguration appConfiguration;
        private DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio;
        private Template template;
        private com.samskivert.mustache.Template templateMustache;
        private List<Autoridade> autoridades;
        private List<Autoridade> autoridadesComErro;
        private String senha;
        private Long lastSend;
        private Long quantidade;
        private Boolean repete;
        private Integer percentual;
        private boolean ehTeste;
        private boolean ehSimulacao;
    }

    public CtxDeEnvio iniciaEstruturaEnvio(String nomeTemplate, String nomeOrgao, String senha, boolean ehTeste, boolean ehSimulacao, int inicio, int quantidade) {

        CtxDeEnvio ctxDeEnvio = new CtxDeEnvio();

        ctxDeEnvio.setTemplate(templateBO.carregaArquivoTemplate(nomeTemplate + ".template"));
        ctxDeEnvio.setAppConfiguration(appConfiguration);
        ctxDeEnvio.setDadosDeConfiguracaoDeEnvio(dadosDeConfiguracaoDeEnvioBO.carregaDadosEnvio());
        com.samskivert.mustache.Template tmpl = Mustache.compiler().compile(ctxDeEnvio.getTemplate().getConteudo());
        ctxDeEnvio.setTemplateMustache(tmpl);
        ctxDeEnvio.setAutoridadesComErro(new LinkedList<Autoridade>());
        ctxDeEnvio.setAutoridades(autoridadeBO.carregaLista(nomeOrgao, ehTeste, inicio, quantidade));
        ctxDeEnvio.setQuantidade((long) ctxDeEnvio.getAutoridades().size());
        ctxDeEnvio.setEhTeste(ehTeste);
        ctxDeEnvio.setEhSimulacao(ehSimulacao);
        ctxDeEnvio.setSenha(senha);
        ctxDeEnvio.setLastSend(0L);


        return ctxDeEnvio;
    }

    private boolean enviarEmail(String msg, String assunto, String emailTo, String senha, DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio) {

        EnviadorDeEmailHelper enviadorDeEmailHelper = new EnviadorDeEmailHelper();
        enviadorDeEmailHelper.setMailTo(emailTo);
        enviadorDeEmailHelper.setMailFrom(dadosDeConfiguracaoDeEnvio.getEmail());

        enviadorDeEmailHelper.setSubject(assunto);
        enviadorDeEmailHelper.setMessage(msg);
        enviadorDeEmailHelper.setSmtpPort(dadosDeConfiguracaoDeEnvio.getSmtpPort());
        enviadorDeEmailHelper.setSmtpServer(dadosDeConfiguracaoDeEnvio.getSmtp());

        enviadorDeEmailHelper.setUseAutentication(dadosDeConfiguracaoDeEnvio.getComAutenticacao());
        enviadorDeEmailHelper.setTlsHabilitado(dadosDeConfiguracaoDeEnvio.getTlsHabilitado());
        enviadorDeEmailHelper.setTlsRequerido(dadosDeConfiguracaoDeEnvio.getTlsRequerido());
        enviadorDeEmailHelper.setSslHabilitado(dadosDeConfiguracaoDeEnvio.getUsaSsl());
        enviadorDeEmailHelper.setPassword(senha);
        enviadorDeEmailHelper.setMessageIsHTML(true);
        enviadorDeEmailHelper.enviar();

        if (enviadorDeEmailHelper.isHasError())
            msgErros.add(enviadorDeEmailHelper.getMensagemDeErro());

        return enviadorDeEmailHelper.isHasError();
    }

    public String formataMensagem(com.samskivert.mustache.Template templateMustache, Autoridade autoridade, DadosDeConfiguracaoDeEnvio dadosDeConfiguracaoDeEnvio){


        Map<String, Object> mapaDados = new HashMap<>();

        autoridade.carregaCamposCalculados();
        mapaDados.put("email", autoridade.getEmail());
        mapaDados.put("orgao", StringEscapeUtils.escapeHtml4(autoridade.getOrgao()));
        mapaDados.put("estado",StringEscapeUtils.escapeHtml4(autoridade.getEstado()));
        mapaDados.put("id",autoridade.getId());
        mapaDados.put("titular",autoridade.getIsTitular());
        mapaDados.put("nome2",StringEscapeUtils.escapeHtml4(autoridade.getNome2()));
        mapaDados.put("nome2CamelCase",StringEscapeUtils.escapeHtml4(autoridade.getNome2CamelCase()));
        mapaDados.put("nome",StringEscapeUtils.escapeHtml4(autoridade.getNome()));
        mapaDados.put("nomeCamelCase",StringEscapeUtils.escapeHtml4(autoridade.getNomeCamelCase()));
        mapaDados.put("partido",autoridade.getPartido());
        mapaDados.put("sexo",autoridade.getSexo());
        mapaDados.put("masculino",autoridade.isMasculino());
        mapaDados.put("telefone",autoridade.getTelefone());
        mapaDados.put("tratamento",StringEscapeUtils.escapeHtml4(autoridade.getTratamento()));
        mapaDados.put("meuNome",StringEscapeUtils.escapeHtml4(dadosDeConfiguracaoDeEnvio.getNome()));
        mapaDados.put("meuCPF",dadosDeConfiguracaoDeEnvio.getCPF());
        mapaDados.put("meuEmail",StringEscapeUtils.escapeHtml4(dadosDeConfiguracaoDeEnvio.getEmail()));
        mapaDados.put("meuSmtp",dadosDeConfiguracaoDeEnvio.getSmtp());

        return templateMustache.execute(mapaDados);

    }

    public void enviar(CtxDeEnvio ctxDeEnvio) {

        int enviados = 0;

        // Envia X emails a partir da quantidade do último envio
        long limit = ctxDeEnvio.getAppConfiguration().getQuantidadeDeEmailsPorVez();
        Long toSkip = ctxDeEnvio.getLastSend();
        List<Autoridade> autoridades = ctxDeEnvio.getAutoridades();
        for (Autoridade autoridade : autoridades) {

            if (!ctxDeEnvio.isEhTeste()) {
                if (toSkip > 0L) {
                    toSkip--;
                    continue;
                }
                if (limit-- == 0)
                    break;
            }
            // autoridade.carregaCamposCalculados();
            String msg = formataMensagem(ctxDeEnvio.getTemplateMustache(),autoridade,ctxDeEnvio.getDadosDeConfiguracaoDeEnvio());

            enviados++;
            if (!ctxDeEnvio.ehSimulacao) {
                if (enviarEmail(msg, ctxDeEnvio.getTemplate().getSubject(),
                        autoridade.getEmail(), ctxDeEnvio.getSenha(), ctxDeEnvio.dadosDeConfiguracaoDeEnvio) == false) {
                    ctxDeEnvio.getAutoridadesComErro().add(autoridade);
                }
            } else {
                simulaEnvioEmail(msg, ctxDeEnvio.getTemplate().getSubject(), autoridade.getEmail());
            }

            if (ctxDeEnvio.isEhTeste())
                break;

            // Tempo entre o envio de um e-mail e outro para enviar a marcação de Span.
            try {
                Thread.sleep(ctxDeEnvio.getDadosDeConfiguracaoDeEnvio().getIntervalo());
            } catch (InterruptedException ie) {
            }
        }
        if (ctxDeEnvio.isEhTeste())
            ctxDeEnvio.setLastSend(ctxDeEnvio.getQuantidade());
        else
            ctxDeEnvio.setLastSend(ctxDeEnvio.getLastSend() + enviados);
    }

    private void simulaEnvioEmail(String msg, String subject, String email) {
        log.info(":::EMAIL SIMULADO ::: Para    : " + email);
        log.info(":::EMAIL SIMULADO ::: Assunto : " + subject);
        Arrays.stream(msg.split("[\n]")).
                map(s -> ":::EMAIL SIMULADO ::: Mensagem: "+s).
                forEach(log::info);
    }
}