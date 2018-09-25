package sample;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class SearchingFiles {
    public static List searchAllFiles(String path) throws IOException
    {
        File dir = new File(path);
        String[] extensions = new String[] { "mp3"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }
}
