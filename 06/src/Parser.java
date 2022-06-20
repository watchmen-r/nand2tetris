import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
    // コマンドのリストと、現在のコマンドの場所を持っておく
    private List<String> commandList = new ArrayList<>();
    private int currentCommandNum;

    public Parser(String filePath) throws IOException {
        currentCommandNum = 0;
        parseCommandList(filePath);
    }

    private void parseCommandList(String filePath) throws IOException {
        Path path = Path.of(filePath);
 
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> addCommand(line));
        }
    }

    private void addCommand(String line) {
        String[] words = line.trim().split("\\s");

        // 実際にファイルを1行ずつparseしていく
        StringBuilder command = new StringBuilder();
        for (String word : words) {
            // コメントだった時は無視する
            if (word.length() >= 2 && word.substring(0, 2).equals("//")) {
                break;
            }
            command.append(word);
        }

        if (!command.isEmpty()) {
            commandList.add(command.toString());
        }
    }

    public void reset() {
        currentCommandNum = 0;
    }

    /**
     * まだコマンドがあるかどうかを返す。(書籍指定メソッド)
     * 
     * @return
     */
    public boolean hasMoreCommands() {
        return currentCommandNum < commandList.size() - 1;
    }

    /**
     * 次のコマンドを現在のコマンドにする。(書籍指定メソッド)
     */
    public void advance() {
        if (hasMoreCommands()) {
            currentCommandNum++;
        }
    }

    /**
     * 現在のコマンドの種類を返す。(書籍指定メソッド)
     * 
     * @return
     */
    public String commandType() {
        String crrCmd = commandList.get(currentCommandNum);

        if (crrCmd.charAt(0) == '@') {
            return "A_COMMAND";
        }

        if (crrCmd.contains("=") || crrCmd.contains(";")) {
            return "C_COMMAND";
        }

        if (crrCmd.charAt(0) == '(' && crrCmd.charAt(crrCmd.length() - 1) == ')') {
            return "L_COMMAND";
        }

        return "invalid command";
    }

    /**
     * 現コマンドのsymbolを返す。(書籍指定メソッド)
     * 
     * @return
     */
    public String symbol() {
        String crrCmd = commandList.get(currentCommandNum);

        String cmdType = commandType();

        if (cmdType.equals("C_COMMAND")) {
            return "invalid call";
        }

        // Aコマンド(@Xxx)のXxxを返す。Xxxはシンボルか
        if (cmdType.equals("A_COMMAND")) {
            return crrCmd.substring(1);
        }

        if (cmdType.equals("L_COMMAND")) {
            return crrCmd.substring(1, crrCmd.length() - 1);
        }
        return "invalid call";
    }

    /**
     * C命令のdestニーモニックを返す。(書籍指定メソッド)
     * 
     * @return
     */
    public String dest() {
        if (!commandType().equals("C_COMMAND")) {
            return "invalid call";
        }

        String crrCmd = commandList.get(currentCommandNum);
        int equalIndex = crrCmd.indexOf("=");

        if (equalIndex >= 0) {
            return crrCmd.substring(0, equalIndex);
        }
        return null;
    }

    /**
     * C命令のcompニーモニックを返す。(書籍指定メソッド)
     * 
     * @return
     */
    public String comp() {
        if (!commandType().equals("C_COMMAND")) {
            return "invalid call";
        }

        String crrCmd = commandList.get(currentCommandNum);
        int equalIndex = crrCmd.indexOf("=");
        int semiIndex = crrCmd.indexOf(";");

        if (equalIndex >= 0) {
            return crrCmd.substring(equalIndex + 1, crrCmd.length());
        }

        if (semiIndex >= 0) {
            return crrCmd.substring(0, semiIndex);
        }
        return null;
    }

    /**
     * C命令のjumpニーモニックを返す。(書籍指定メソッド)
     * 
     * @return
     */
    public String jump() {
        if (!commandType().equals("C_COMMAND")) {
            return "invalid call";
        }

        String crrCmd = commandList.get(currentCommandNum);
        int semiIndex = crrCmd.indexOf(";");

        if (semiIndex >= 0) {
            return crrCmd.substring(semiIndex + 1, crrCmd.length());
        }
        return null;
    }
}
