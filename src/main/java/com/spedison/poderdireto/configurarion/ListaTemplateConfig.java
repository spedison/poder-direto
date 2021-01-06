package com.spedison.poderdireto.configurarion;

import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.ListaTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListaTemplateConfig {

    @Autowired
    AppConfiguration appConfiguration;

    @Bean
    ListaTemplates getListaTemplates(){
        return new ListaTemplates();
    }
}
