package util

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by TY-MSI on 5/7/2016.
 */



class SCUtility {


    public static String getTextFromResource(String resourcePath) {
        InputStream aInStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        return aInStream.getText();
    }
}
