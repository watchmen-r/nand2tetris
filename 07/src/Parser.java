import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parser {

    // command list and present location
    private List<String> commandList = new ArrayList<>();
    private int currentCommandNum;

    private static final List<String> arithmeticCmds = new ArrayList<>();

    static {
        arithmeticCmds.add("add");
        arithmeticCmds.add("sub");
        arithmeticCmds.add("neg");
        arithmeticCmds.add("eq");
        arithmeticCmds.add("gt");
        arithmeticCmds.add("lt");
        arithmeticCmds.add("and");
        arithmeticCmds.add("or");
        arithmeticCmds.add("not");
    }

    public Parser(File file) throws IOException {
        currentCommandNum = 0;
        parseCommandList(file.getPath());
    }

    private void parseCommandList(String filePath) throws IOException {
        Path path = Path.of(filePath);

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> addCommand(line));
        }
    }

    private void addCommand(String line) {
        String[] words = line.trim().split("\\s");

        // parse per one word.
        StringBuilder command = new StringBuilder();
        for (String word : words) {
            // ignore comment
            if (word.length() >= 2 && word.substring(0, 2).equals("//")) {
                break;
            }
            command.append(word);
        }

        if (!command.isEmpty()) {
            commandList.add(command.toString());
        }
    }

    /**
     * whether commands is exists or not(specified by the book.)
     * 
     * @return
     */
    public boolean hasMoreCommands() {
        return currentCommandNum < commandList.size() - 1;
    }

    /**
     * go forward command (specified by the book.)
     */
    public void advance() {
        if (hasMoreCommands()) {
            currentCommandNum++;
        }
    }

    /**
     * return current command type (specified by the book.)
     * 
     * @return
     */
    public String commandType() {
        String crrCmd = commandList.get(currentCommandNum);
        String[] cmds = crrCmd.split("\\s");

        if (arithmeticCmds.contains(cmds[0])) {
            return "C_ARITHMEETIC";
        }

        if (cmds[0].equals("push")) {
            return "C_PUSH";
        }

        if (cmds[0].equals("pop")) {
            return "C_POP";
        }

        if (cmds[0].equals("label")) {
            return "C_LABEL";
        }

        if (cmds[0].equals("goto")) {
            return "C_GOTO";
        }

        if (cmds[0].equals("if")) {
            return "C_IF";
        }

        if (cmds[0].equals("function")) {
            return "C_FUNCTION";
        }

        if (cmds[0].equals("return")) {
            return "C_RETURN";
        }

        if (cmds[0].equals("call")) {
            return "C_CALL";
        }

        throw new IllegalArgumentException("Unknown command.");
    }

    /**
     * return first argument.
     * 
     * @return
     */
    public String arg1() {
        String currentCmd = commandList.get(currentCommandNum);

        if (commandType().equals("C_RETURN")) {
            throw new IllegalCallerException("Illegal call");
        }

        if (commandType().equals("C_ARITHMEETIC")) {
            return currentCmd;
        }

        return currentCmd.split("\\s")[1];
    }

    /**
     * return second argument.
     * @return
     */
    public int arg2() {
        if (commandType().equals("C_PUSH") || commandType().equals("C_POP") || commandType().equals("C_FUNCTION")
                || commandType().equals("C_CALL")) {
            return Integer.parseInt(commandList.get(currentCommandNum).split("\\s")[2]);
        }

        throw new IllegalCallerException("Illegal call");
    }
}
