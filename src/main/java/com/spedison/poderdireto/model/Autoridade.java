package com.spedison.poderdireto.model;

import com.spedison.poderdireto.helper.StringsHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Autoridade implements Cloneable {
    private long id;
    private String orgao;
    private String nome;
    private String nomeCamelCase;
    // Calculado
    private String nome2;
    private String nome2CamelCase;
    private String estado;
    private String partido;
    private Boolean isTitular;
    private String telefone;
    private String email;
    private String tratamento;
    private char sexo;
    // Calculado
    private boolean masculino;

    public void carregaCamposCalculados() {
        masculino = (sexo == 'M' || sexo == 'm');
        String[] nomes = nome.split("[ ]");
        nome2 = nomes[0] + " " + nomes[nomes.length - 1];

        nomeCamelCase = StringsHelper.camelCaseVariasPalavras(nome);
        nome2CamelCase = StringsHelper.camelCaseVariasPalavras(nome2);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Autoridade aC = (Autoridade) super.clone();
        BeanUtils.copyProperties(this, aC);
        return aC;
    }

    public Autoridade copy(){
        try {
            Autoridade ret = (Autoridade) this.clone();
            return ret;
        }catch (CloneNotSupportedException cnse){
            return new Autoridade();
        }
    }

}
