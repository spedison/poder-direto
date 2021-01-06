package com.spedison.poderdireto;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class ContagemListaTest {

    @Test
    public void pegaNomeArquivoCompleto() {

        List<Integer> a = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //a.stream().skip(4).limit(7).forEach(log::debug); // 4,5,6
        //a.stream().skip(0).limit(4).forEach(log::debug); // 0,1,2,3


        a = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
        int passo = 4;
        for (int i = 0; i < 7; i++) {
            a.stream().skip(i * passo).limit(passo).forEach(log::debug); // .. Fica na sequência 0...27
            log.debug("PASSO = " + passo + " i = " + i);
        }

        //a.stream().skip(0).limit(4).forEach(log::debug); // 0,1,2,3


    }

}
