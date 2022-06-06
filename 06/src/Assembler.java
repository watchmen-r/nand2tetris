import java.io.File;
import java.io.PrintWriter;

public class Assembler {
    public static void main(String[] args) {
        // 実行時、第一引数にファイルがくる
        File inputFile = new File(args[0]);
        int inputExtention = inputFile.getName().indexOf('.');
        String inputFileName = inputFile.getName().substring(0, inputExtention);

        // 出力用ファイルを作成する
        File outputFile = new File(input.getParent(), inputFileName + ".hack");
        PrintWriter writeOut = new PrintWriter(outputFile);

        
    }
}