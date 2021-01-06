package com.spedison.poderdireto.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class EnviadorDeEmailHelper {

    private String smtpServer;
    private int smtpPort;
    private String mailFrom;
    private String password;

    private String mailTo;
    private String message;
    private String subject;
    private boolean messageIsHTML;
    private boolean useAutentication;
    private boolean hasError;
    private String mensagemDeErro;
    private boolean tlsRequerido;
    private boolean tlsHabilitado;
    private boolean sslHabilitado;
    private boolean htmlMessage = true;

    public void mudaEnvio(String mailTo, String message, String subject) {
        this.setMailTo(mailTo);
        this.setMessage(message);
        this.setSubject(subject);
        enviar();
    }

    public void enviar() {
        hasError = false;

        Email email;
        if (htmlMessage)
            email = new HtmlEmail();
        else
            email = new SimpleEmail();

        try {
            email.setDebug(true);
            email.setHostName(smtpServer);
            email.setSmtpPort(smtpPort);
            email.setAuthentication(mailFrom, password);
            email.setSSL(sslHabilitado);
            email.setStartTLSEnabled(tlsHabilitado);
            email.setStartTLSRequired(tlsRequerido);
            email.addTo(mailTo); //pode ser qualquer email
            email.setFrom(mailFrom); //será passado o email que você fará a autenticação
            email.setSubject(subject);
            email.setMsg(message);
            email.send();
        } catch (EmailException e) {
            hasError = true;
            mensagemDeErro = e.getMessage();
            log.error("Problemas ao enviar e-mail.", e);
        }

    }

}
