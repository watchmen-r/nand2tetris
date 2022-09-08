import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JackTokenizer {
    private File inputFile;
    List<String> tokens;


    public JackTokenizer(File source) throws IOException {
        inputFile = source;
        tokens = new ArrayList<>();

        Path path = Path.of(source.getPath());
        StringBuilder content = new StringBuilder();
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> addToken(line, content));
        }

        // remove block comment
        String code = content.toString().replaceAll("/\\*.*\\*/", " ").replaceAll("(?s)/\\*.*\\*/", " ");
        
    }

    private void addToken(String line, StringBuilder contentBuilder) {
         contentBuilder.append(removeComment(line).trim()).append("\n");
    }

    private String removeComment(String line) {
        int slash = line.indexOf("//");

        if (slash == -1) {
            return line;
        }

        return line.substring(0, slash);
    }

}
