package com.spedison.poderdireto;

import com.spedison.poderdireto.bo.DownloadBO;
import com.spedison.poderdireto.model.Download;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
class DownloadTest {

    @Autowired
    DownloadBO downloadBO;

    @Test
    void contextLoads() {

      List<Download> dw = downloadBO.listarListaDownloads(true);

      dw.stream().map(Download::toString).forEach(log::info);

    }



}
