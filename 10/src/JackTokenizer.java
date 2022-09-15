import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JackTokenizer {
    private File inputFile;
    List<String> tokens;
    int pointer;

    private static final String KEYWORD_REGEX = "class | constructor | function | method | field | static | var | int | char | boolean | void | true | false | null  this | let | do | if | else | while | return";
    private static final String SYMBOL_REGEX = "[\\{\\}\\(\\)\\[\\]\\.\\,\\;\\+\\-\\*\\/\\$\\|\\<\\>\\=\\~]";
    private static final String INTEGER_REGEX = "[0-9]+";
    private static final String STR_REGEX = "\"[^\"]*\"";
    private static final String IDENTIFIER_REGEX = "[\\w_]+";

    public static final String KEYWORD = "KEYWORD";
    public static final String SYMBOL = "SYMBOL";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String INT_CONST = "INT_CONST";
    public static final String STRING_CONST = "STRING_CONST";

    public static Map<String, String> keyWordMap = new HashMap<>();
    public static Set<Character> opSet = new HashSet<>();

    static {
        keyWordMap.put("class", "CLASS");
        keyWordMap.put("method", "METHOD");
        keyWordMap.put("function", "FUNCTION");
        keyWordMap.put("constructor", "CONSTRUCTOR");
        keyWordMap.put("int", "INT");
        keyWordMap.put("boolean", "BOOLEAN");
        keyWordMap.put("char", "CHAR");
        keyWordMap.put("void", "VOID");
        keyWordMap.put("var", "VAR");
        keyWordMap.put("static", "STATIC");
        keyWordMap.put("field", "FIELD");
        keyWordMap.put("let", "LET");
        keyWordMap.put("do", "DO");
        keyWordMap.put("if", "IF");
        keyWordMap.put("else", "ELSE");
        keyWordMap.put("while", "WHILE");
        keyWordMap.put("return", "RETURN");
        keyWordMap.put("true", "TRUE");
        keyWordMap.put("false", "FALSE");
        keyWordMap.put("null", "NULL");
        keyWordMap.put("this", "THIS");

        opSet.add('+');
        opSet.add('-');
        opSet.add('*');
        opSet.add('/');
        opSet.add('&');
        opSet.add('|');
        opSet.add('<');
        opSet.add('>');
        opSet.add('=');
    }

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
        Matcher m = tokenPattern.matcher(code);
        while (m.find()) {
            tokens.add(m.group());
        }

        pointer = 0;
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

    public boolean hasMoreTokens() {
        return pointer < tokens.size();
    }

    public void advance() {
        if (hasMoreTokens()) {
            pointer++;
        }
    }

    public void pointerBack() {
        pointer--;
    }

    public String tokenType() {
        String currentToken = tokens.get(pointer);
        if (currentToken.matches(KEYWORD_REGEX)) {
            return KEYWORD;
        }

        if (currentToken.matches(SYMBOL_REGEX)) {
            return SYMBOL;
        }

        if (currentToken.matches(IDENTIFIER_REGEX)) {
            return IDENTIFIER;
        }

        if (currentToken.matches(INTEGER_REGEX)) {
            return INT_CONST;
        }

        if (currentToken.matches(STR_REGEX)) {
            return STRING_CONST;
        }

        throw new IllegalArgumentException("invalid token");
    }

    public String keyWord() {
        if (!tokenType().equals(KEYWORD)) {
            throw new IllegalAccessError("invalid access");
        }

        String currentToken = tokens.get(pointer);
        return keyWordMap.get(currentToken);
    }

    public char symbol() {
        if (!tokenType().equals(SYMBOL)) {
            throw new IllegalAccessError("invalid access");
        }
        return tokens.get(pointer).charAt(0);
    }

    public String identifier() {
        if (!tokenType().equals(IDENTIFIER)) {
            throw new IllegalAccessError("invalid access");
        }
        return tokens.get(pointer);
    }

    public int intVal() {
        if (!tokenType().equals(INT_CONST)) {
            throw new IllegalAccessError("invalid access");
        }
        return Integer.parseInt(tokens.get(pointer));
    }

    public String stringVal() {
        if (!tokenType().equals(STRING_CONST)) {
            throw new IllegalAccessError("invalid access");
        }
        String currentToken = tokens.get(pointer);
        // to remove double quote, use substring
        return currentToken.substring(1, currentToken.length() - 1);
    }

    public String getToken() {
        return tokens.get(pointer);
    }

    public boolean isOp() {
        return opSet.contains(symbol());
    }
}
