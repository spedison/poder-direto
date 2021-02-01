package com.spedison.poderdireto.model;

import java.util.LinkedList;

public class ListaVariavelEmail extends LinkedList<VariavelEmail> {

    private static ListaVariavelEmail allFilled = null;


    public static ListaVariavelEmail getAllItens(){
        if(allFilled != null)
            return allFilled;

        allFilled = new ListaVariavelEmail();
        allFilled.add(new VariavelEmail("nome", "Nome do destinatário, como está cadastrado", "{{{nome}}}"));
        allFilled.add(new VariavelEmail("id ", "id do cadastro", "{{id}}"));
        allFilled.add(new VariavelEmail("email", "E-mail do destinatário cadastrado", "{{email}}"));
        allFilled.add(new VariavelEmail("orgao", "Nome do órgão ou lista que será executada", "{{{orgao}}}"));
        allFilled.add(new VariavelEmail("estado", "Sigla do Estado do parlamentar", "{{{estado}}}"));
        allFilled.add(new VariavelEmail("titular", "Se o parlamentar é titular ou suplente (Verdadeiro = Titular, Falso = Suplente)", "{{#titular}}...{{/titular}} ou  {{^titular}}...{{/titular}}"));
        allFilled.add(new VariavelEmail("nome2", "Somente o primeiro e o último nome do parlamentar", "{{{nome2}}}"));
        allFilled.add(new VariavelEmail("nome2CamelCase", "Somente o primeiro e o último nome do parlamentar - Mantendo somente a primeira letra maiúscula", "{{{nome2CamelCase}}}"));
        allFilled.add(new VariavelEmail("nome", "Nome completo do parlamentar", "{{nome}}"));
        allFilled.add(new VariavelEmail("nomeCamelCase", "Nome completo do parlamentar - Mantendo somente as primeiras letras maiúsculas", "{{{nomeCamelCase}}}"));
        allFilled.add(new VariavelEmail("partido", "Sigla do partido", "{{partido}}"));
        allFilled.add(new VariavelEmail("sexo", "Sexo (M ou F)", "{{sexo}}"));
        allFilled.add(new VariavelEmail(
                "masculino",
                "Variável indica que é masculino, Valores verdadeiro ou falso ", "{{#masculino}}...{{/masculino}} {{^masculino}}...{{/masculino}}"));
        allFilled.add(new VariavelEmail("telefone", "Número do telefone do gabinete do parlamentar", "{{telefone}}"));
        allFilled.add(new VariavelEmail("tratamento", "Qual a forma de tratamento da autoridade/contato", "{{{tratamento}}}"));
        allFilled.add(new VariavelEmail("meuNome", "Nome de quem envia o e-mail", "{{{meuNome}}}"));
        allFilled.add(new VariavelEmail("meuCPF", "Número do CPF de quem envia o e-mail (se aplicável)", "{{meuCPF}}"));
        allFilled.add(new VariavelEmail("meuEmail", "Email de quem envia essa mala direta", "{{meuEmail}}"));
        allFilled.add(new VariavelEmail("meuSmtp", "SMTP utilizado", "{{meuSmtp}}"));
//        allFilled.add(new VariavelEmail("", "", "{{}}"));



        return allFilled;
    }


}
