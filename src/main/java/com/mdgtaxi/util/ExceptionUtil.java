package com.mdgtaxi.util;

import java.io.IOException;

public class ExceptionUtil {
    public static void throwExceptionOnObjectNull (Object object) throws IOException {
        if (object == null) {
            throw new IOException("Object null : " + object.getClass().getSimpleName());
        }
    }
}
