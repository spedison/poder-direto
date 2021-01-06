package com.spedison.poderdireto;


import com.spedison.poderdireto.model.Autoridade;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@Data
@Log4j2
public class TestStringHelper {

    @Test
    public void testaString(){

        String msg = """
                Aqui é um teste de mensagens
                Segunda linha de mensagem
                Terceira linha de mensagem
                """;

        Arrays.stream(msg.split("[\n]")).
                map(s -> ":::EMAIL SIMULADO ::: Mensagem: "+s).
                forEach(log::info);
    }

    @Test
    public void testaCamposCalculadosAutoridade(){

        Autoridade a = new Autoridade();

        a.setEmail("spedison@gmail.com");
        a.setNome("EDISON RIBEIRO DE ARAÚJO");
        a.setSexo('M');
        a.setIsTitular(true);
        a.carregaCamposCalculados();
        Assert.assertEquals("Edison Ribeiro de Araújo", a.getNomeCamelCase());
        Assert.assertEquals("Edison Araújo", a.getNome2CamelCase());
        log.info(a.toString());


        a.setNome("EDISON rIBEIRo PaRa ARaÚJO");
        a.carregaCamposCalculados();
        log.info(a.toString());

        Assert.assertEquals("Edison Ribeiro para Araújo", a.getNomeCamelCase());
        Assert.assertEquals("Edison Araújo", a.getNome2CamelCase());


    }


}
