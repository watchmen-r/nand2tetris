import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Assembler {

    private static final String NUM_REG = "[\\d０-９]+";

    public static void main(String[] args) throws IOException {
        // 実行時、第一引数にファイルがくる
        File inputFile = new File(args[0]);
        int inputExtention = inputFile.getName().indexOf('.');
        String inputFileName = inputFile.getName().substring(0, inputExtention);

        // 出力用ファイルを作成する
        File outputFile = new File(inputFile.getParent(), inputFileName + ".hack");
        PrintWriter writeOut = new PrintWriter(outputFile);

        Parser parser = new Parser(inputFile.toPath().toString());
        Code code = new Code();
        SymbolTable symbolTable = new SymbolTable();

        while (true) {
            // ラベル系の場合
            if (parser.commandType().equals("L_COMMAND")) {
                if (parser.hasMoreCommands()) {
                    parser.advance();
                    continue;
                }
                break;
            }

            // Aコマンド(@Xxxのもの)の場合
            if (parser.commandType().equals("A_COMMAND")) {
                String machineLanguage = "0";
                
                // Aコマンドの@XxxのXxxが10進数整数の場合
                if (parser.symbol().matches(NUM_REG)) {
                    
                } else {
                    // @XxxのXxxがシンボルの場合
                    if (symbolTable.contains(parser.symbol())) {
                        String binary = Integer.toBinaryString((symbolTable.getAddress((parser.symbol()))));
                        String paddingBinary = String.format("%15s", binary).replace(" ", "0");
                        machineLanguage += binary;
                    }
                }



            }

            // Cコマンドの場合
        }

    }
}