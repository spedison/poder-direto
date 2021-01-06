package com.spedison.poderdireto.helper;

import java.io.File;

public class FilesHelper {

    public static boolean fileExists(String fileName){
        File f = new File(fileName);
        return f.exists();
    }

}
