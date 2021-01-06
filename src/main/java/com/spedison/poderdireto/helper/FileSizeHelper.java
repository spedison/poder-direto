package com.spedison.poderdireto.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

public class FileSizeHelper {

    @Data
    @AllArgsConstructor
    public static class Result {
        String unit;
        double value;
    }

    static public Result convertAsHuman(long bytes) {
        String[] units = {"Bytes", "KB", "MB", "GB", "TB"};
        double ajustBytes = bytes;
        int conta = 0;
        while (ajustBytes >= 1024 && conta < units.length - 1) {
            ajustBytes /= 1024.0;
            conta++;
        }
        return new Result(units[conta], ajustBytes);
    }


}
