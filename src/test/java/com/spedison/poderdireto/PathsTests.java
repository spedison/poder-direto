package com.spedison.poderdireto;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class PathsTests {

    @Test
    public void pegaNomeArquivoCompleto() {
        String base = System.getProperty("user.home");
        Path p = Paths.get(base, ".config", "contatos.csv");
        log.info(p.toString());
        String strCompara = "/home/.../.config/contatos.csv";
        Assert.assertTrue( p.toFile().exists() );
        log.info(p.toString());
    }

}
