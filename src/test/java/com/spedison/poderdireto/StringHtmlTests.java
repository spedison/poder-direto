package com.spedison.poderdireto;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class StringHtmlTests {

    @Test
    public void pegaNomeArquivoCompleto() {
        String base = StringEscapeUtils.escapeHtml4("Edison Araújo");
        log.debug(base);
    }

}
