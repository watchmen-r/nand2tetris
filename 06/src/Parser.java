import java.util.ArrayList;

public class Parser {
    private List commandList = new ArrayList<>();
    public Parser(String filePath) {

    }

    private void parseCommandList(String filePath) {
        Path path = Path.of(filePath);
        
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> addCommand(line));
        }
    }

    private void addCommand(String line) {
        String[] words = line.split("\\s");
        
        // 実際にファイルを1行ずつparseしていく
        StringBuilder command = new StringBuilder();
        for (String word: words) {
            // 空だった場合
            if(word.isEmpty()) {
                break;
            }

            // コメントだった時は無視する
            if (word.length >= 2 && word.substring(0, 2).equals("//")) {
                break;
            }
            command.append(word);
        }

        if(!command.isEmpty()) {
            commandList.add(command.toString());
        }
    }
}
