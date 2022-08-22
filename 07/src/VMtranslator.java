import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class VMtranslator {
    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }

        // args[0] is folder or filename whose extension is .vm
        File source = new File(args[0]);
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            Arrays.stream(files).forEach(file -> translateVm(file));
        }

    }

    private static void translateVm(File source) {
        if (!source.getName().endsWith(".vm")) {
            return;
        }

        int inputExtention = source.getName().indexOf('.');
        String inputFileName = source.getName().substring(0, inputExtention);

        try {
            Parser parser = new Parser(source);
            File outputFile = new File(source.getParent(), inputFileName + ".hack");
            CodeWriter writer = new CodeWriter(outputFile);
            while (parser.hasMoreCommands()) {
                parseLine(parser, writer);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseLine(Parser parser, CodeWriter writer) {
        parser.advance();
        String type = parser.commandType();
        if (type.equals(Parser.C_ARITHMEETIC)) {
            writer.writeArithmetic(parser.arg1());
        } else if (type.equals(Parser.C_POP) || type.equals(Parser.C_PUSH)) {
            writer.writePushPop(type, parser.arg1(), parser.arg2());
        }
    }

}
