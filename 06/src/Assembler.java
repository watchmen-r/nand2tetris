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
        SymbolTable symbolTable = new SymbolTable(parser);

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
                String binary = "";

                // Aコマンドの@XxxのXxxが10進数整数の場合
                if (parser.symbol().matches(NUM_REG)) {
                    int deciNum = Integer.parseInt(parser.symbol());
                    binary = Integer.toBinaryString(deciNum);
                    
                    // @XxxのXxxがシンボルの場合
                } else {

                    if (!symbolTable.contains(parser.symbol())) {
                        symbolTable.addEntry(parser.symbol());
                    }

                    binary = Integer.toBinaryString((symbolTable.getAddress((parser.symbol()))));
                }
                String paddingBinary = String.format("%15s", binary).replace(" ", "0");
                machineLanguage += paddingBinary;
                writeOut.println(machineLanguage);

            }
            // Cコマンドの場合
            if (parser.commandType().equals("C_COMMAND")) {
                // C命令の機械語の最初3桁は111
                StringBuilder mlBuilder = new StringBuilder("111");
                mlBuilder.append(code.comp(parser.comp()));
                mlBuilder.append(code.dest(parser.dest()));
                mlBuilder.append(code.jump(parser.jump()));

                String machineLanguage = mlBuilder.toString();
                writeOut.println(machineLanguage);
            }

            if (parser.hasMoreCommands()) {
                parser.advance();
            } else {
                break;
            }
        }
        writeOut.close();
    }
}