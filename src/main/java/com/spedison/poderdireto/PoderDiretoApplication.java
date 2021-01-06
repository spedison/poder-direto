package com.spedison.poderdireto;


import com.spedison.poderdireto.bo.DownloadPlanilhaBO;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Arrays;

@SpringBootApplication
@Log4j2
public class PoderDiretoApplication {

    @Autowired
    DownloadPlanilhaBO downloadPlanilhaBO;

    public static void main(String[] args) {
        SpringApplication.run(PoderDiretoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            String pathConf = System.getProperty("user.home") + File.separator + ".config" +File.separator+ "poder-direto";
            File f1 = new File (pathConf);
            if(!f1.exists())
                f1.mkdirs();

            downloadPlanilhaBO.downloadPlanilha(false);


            //log.trace ("Exibindo as variáveis do Spring Boot:");

            //String[] beanNames = ctx.getBeanDefinitionNames();
            /*Arrays.sort(beanNames);

            for (String beanName : beanNames) {
                Object obj = ctx.getBean(beanName);
                String obj_str;
                if (obj instanceof Integer ) {
                    obj_str = Integer.toString((Integer)obj);
                }else if (obj instanceof Long) {
                    obj_str = Long.toString((Long)obj);
                } else {
                    obj_str = obj.toString();
                }
                log.trace("Bean -> [" + beanName + "] tem o valor Valor => "+obj_str);
            }*/

            log.trace("Nome da aplicação " + ctx.getApplicationName());
        };
    }

}
