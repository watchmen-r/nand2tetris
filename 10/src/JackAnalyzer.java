import java.io.File;
import java.io.IOException;
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
        File outputFile = new File(filePath + ".xml");
        File tokenOutput = new File(filePath + "T.xml");
        
        try {
            CompliationEngine compliationEngine = new CompliationEngine(file, outputFile, tokenOutput);
            compliationEngine.compileClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
