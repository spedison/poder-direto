package com.spedison.poderdireto.helper;

import java.util.Arrays;
import java.util.Locale;

public class StringsHelper {

    static public String primeiraMaiscula(String nome) {
        return nome.substring(0, 1).toUpperCase() + nome.substring(1, nome.length()).toLowerCase();
    }

    static public boolean ehPreposicao(String palavra) {
        String strCompara = palavra.trim().toLowerCase(Locale.ROOT);
        boolean b = strCompara.equals("de") ||
                strCompara.equals("para") ||
                strCompara.equals("a") ||
                strCompara.equals("ante") ||
                strCompara.equals("até") ||
                strCompara.equals("ate") ||
                strCompara.equals("onde") ||
                strCompara.equals("da") ||
                strCompara.equals("do") ||
                strCompara.equals("nisso") ||
                strCompara.equals("isso") ||
                strCompara.equals("ante") ||
                strCompara.equals("após") ||
                strCompara.equals("desde") ||
                strCompara.equals("em") ||
                strCompara.equals("com") ||
                strCompara.equals("perante");
        return b;

    }

    static public String camelCaseVariasPalavras(String palavras) {

        String[] arrPalavras = palavras.split("[ ]");
        StringBuffer strBuffer = new StringBuffer();


        Arrays.stream(arrPalavras).
                filter(str -> !str.trim().isEmpty()).
                forEach(str -> {

                    if (ehPreposicao(str))
                        strBuffer.append(str.trim().toLowerCase(Locale.ROOT));
                    else
                        strBuffer.append(primeiraMaiscula(str.trim()));

                    strBuffer.append(" ");
                });


        return strBuffer.toString().trim();
    }

}
