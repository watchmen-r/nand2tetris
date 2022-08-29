import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class VMtranslator {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            return;
        }

        // args[0] is folder or filename whose extension is .vm
        File source = new File(args[0]);
        String outputFileName = getOutputFile(source);

        File outputFile = new File(source.getPath(), outputFileName);
        CodeWriter writer = new CodeWriter(outputFile);

        if (source.isDirectory()) {
            File[] files = source.listFiles();
            Arrays.stream(files).forEach(file -> translateVm(file, outputFileName, writer));
        } else {
            translateVm(source, outputFileName, writer);
        }
        writer.close();
    }

    private static String getOutputFile(File file) {
        if (file.isFile()) {
            int inputExtention = file.getName().indexOf('.');
            return file.getName().substring(0, inputExtention) + ".asm";
        }
        return file.getName() + ".asm";

    }

    private static void translateVm(File source, String outputFileName, CodeWriter writer) {
        if (!source.getName().endsWith(".vm")) {
            return;
        }

        try {
            Parser parser = new Parser(source);
            while (parser.hasMoreCommands()) {
                parseLine(parser, writer);
            }
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

        } else if (type.equals(Parser.C_LABEL)) {
            writer.writeLabel(parser.arg1());

        } else if (type.equals(Parser.C_GOTO)) {
            writer.writeGoto(parser.arg1());

        } else if (type.equals(Parser.C_IF)) {
            writer.writeIf(parser.arg1());

        } else if (type.equals(Parser.C_RETURN)) {
            writer.writeReturn();

        } else if (type.equals(Parser.C_FUNCTION)) {
            writer.writeFunction(parser.arg1(), parser.arg2());

        } else if (type.equals(Parser.C_CALL)) {
            writer.writeCall(parser.arg1(), parser.arg2());
        }
    }

}
