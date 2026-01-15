package com.mdgtaxi.util;

import java.time.LocalDate;

public class CalcUtil {
    public static int calulerAge (LocalDate date) {
        LocalDate now = LocalDate.now();
        return now.getYear() - date.getYear()  ;
    }
}
