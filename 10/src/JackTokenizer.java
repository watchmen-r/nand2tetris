import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JackTokenizer {
    private File inputFile;
    List<String> tokens;

    private static final String KEYWORD_REGEX = "class | constructor | function | method | field | static | var | int | char | boolean | void | true | false | null  this | let | do | if | else | while | return";
    private static final String SYMBOL_REGEX = "[\\{\\}\\(\\)\\[\\]\\.\\,\\;\\+\\-\\*\\/\\$\\|\\<\\>\\=\\~]";
    private static final String INTEGER_REGEX = "[0-9]+";
    private static final String STR_REGEX = "\"[^\"]*\"";
    private static final String IDENTIFIER_REGEX = "[\\w_]+";

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

        Pattern tokenPattern = Pattern.compile(
                KEYWORD_REGEX + "|" + SYMBOL_REGEX + "|" + INTEGER_REGEX + "|" + STR_REGEX + "|" + IDENTIFIER_REGEX);
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
