package com.spedison.poderdireto;

import com.spedison.poderdireto.bo.AppConfigurationBO;
import com.spedison.poderdireto.bo.DownloadBO;
import com.spedison.poderdireto.model.Download;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Log4j2
class ListaPlanilhasTest {

    @Autowired
    AppConfigurationBO appConfigurationBO;

    @Test
    void contextLoads() {

      File[] dw = appConfigurationBO.listaPlanilhas();

        Arrays.stream(dw).map(File::getName).forEach(log::info);
        Assert.assertEquals(dw.length, 5);

    }



}
