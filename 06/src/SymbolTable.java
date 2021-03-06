import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> symbolMap = new HashMap<>();

    private int nextMemAddress;
    private int currentLine;
    private Parser parser;
    
    public SymbolTable(Parser p) {
        // 定義済のシンボルを設定する
        symbolMap.put("SP", 0);
        symbolMap.put("LCL", 1);
        symbolMap.put("ARG", 2);
        symbolMap.put("THIS", 3);
        symbolMap.put("THAT", 4);
        for(int i = 0; i <= 15; i++) {
            symbolMap.put("R" + i, i);
        }
        symbolMap.put("SCREEN", 16384);
        symbolMap.put("KBD", 24576);

        nextMemAddress = 16;
        currentLine = 0;
        parser = p;
        saveOriginSymbol();
    }

    private void saveOriginSymbol() {
        while(true) {
            if(parser.commandType().equals("L_COMMAND")) {
                symbolMap.put(parser.symbol(), currentLine);
            } else {
                currentLine++;
            }

            if(parser.hasMoreCommands()) {
                parser.advance();
                continue;
            }
            break;
        }
        parser.reset();
    }

    public void addEntry(String symbol) {
        symbolMap.put(symbol, nextMemAddress);
        nextMemAddress++;
    }

    public boolean contains(String symbol) {
        return symbolMap.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        return symbolMap.get(symbol);
    }
}
