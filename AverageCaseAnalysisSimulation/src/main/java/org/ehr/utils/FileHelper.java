package org.ehr.utils;

import java.io.File;
import java.util.Arrays;

public class FileHelper {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void mkdirsForOutput(String path) {
        String[] pathSegments = path.split("/");
        String pathWithoutLastSegment = String.join("/",
                Arrays.copyOf(pathSegments, pathSegments.length - 1));

        File outputDir = new File(pathWithoutLastSegment);
        outputDir.mkdirs();
    }
}
