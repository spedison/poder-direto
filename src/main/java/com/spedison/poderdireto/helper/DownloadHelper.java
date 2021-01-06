package com.spedison.poderdireto.helper;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Data
@Component
@Log4j2
public class DownloadHelper {

    public interface ActionOnDownload {
        void onPreOk(String filename);
        void onPreNOk(int errorCode);
        void onDownload(int lastRead, int totalRead, int totalLen);
        void onFinish();
        void onExcetion(Exception e);
    }


   private String url;
   private String filename;
   private int lastResponseCode;
   private int lastContentLength;
   private ActionOnDownload actionOnDownload = null;
   private int BUFFER_SIZE = 4096;

   public boolean arquivoJaExiste(){
       File f = new File (filename);
       return f.exists();
   }


    public void downloadArquivo() {

        try {

            URL url = new URL(getUrl());
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            lastResponseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (lastResponseCode == HttpURLConnection.HTTP_OK) {
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                lastContentLength = httpConn.getContentLength();

                if (actionOnDownload != null) {
                    actionOnDownload.onPreOk(filename);
                }

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(filename);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                int allRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    allRead += bytesRead;
                    outputStream.write(buffer, 0, bytesRead);
                    if (actionOnDownload != null)
                        actionOnDownload.onDownload(bytesRead, allRead, lastContentLength);
                }

                outputStream.close();
                inputStream.close();

                if (actionOnDownload != null )
                    actionOnDownload.onFinish();

                log.debug("File downloaded -- From " + url + " -- To " + filename);
            } else {
                actionOnDownload.onPreNOk(lastResponseCode);
                log.error("No file to download. Server replied HTTP code: " + lastResponseCode);
            }
            httpConn.disconnect();
        } catch (MalformedURLException e2) {
            actionOnDownload.onExcetion(e2);
            log.error("Mal formatação da URL", e2);
        } catch (IOException e1) {
            actionOnDownload.onExcetion(e1);
            log.error("Erro de IO ou rede", e1);
        }
    }

}
