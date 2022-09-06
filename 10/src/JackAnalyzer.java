import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JackAnalyzer {
    public static void main(String[] args) {
        File fileOrDirectory = new File(args[0]);
        List<File> fileList;

        if(fileOrDirectory.isFile()) {
            fileList = Arrays.asList(fileOrDirectory);
        } else {
            File[] files = fileOrDirectory.listFiles();
            fileList = Stream.of(files).filter(file -> file.getName().endsWith(".jack")).collect(Collectors.toList());
        }

        fileList.forEach(file -> processFile(file));
    }

    private static void processFile(File file) {
        String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'));
        String outputPath = filePath + ".xml";
        String tokenOutputPath = filePath + "T.xml";
        
    }

    
}
