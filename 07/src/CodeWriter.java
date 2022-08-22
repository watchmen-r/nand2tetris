import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {

    PrintWriter writeOut;
    private int arthJump;

    public CodeWriter(File outputFile) throws FileNotFoundException {
        writeOut = new PrintWriter(outputFile);
    }

    public void writeArithmetic(String command) {
        if (command.equals("add")) {
            writeOut.print(arithmeticTemplate1() + "M=M+D\n");
            return;
        }

        if (command.equals("sub")) {
            writeOut.print(arithmeticTemplate1() + "M=M-D\n");
            return;
        }

        if (command.equals("and")) {
            writeOut.print(arithmeticTemplate1() + "M=M&D\n");
            return;
        }

        if (command.equals("or")) {
            writeOut.print(arithmeticTemplate1() + "M=M|D\n");
            return;
        }

        if (command.equals("gt")) {
            writeOut.print(arithmeticTemplate2("JLE"));
            arthJump++;
            return;
        }

        if (command.equals("lt")) {
            writeOut.print(arithmeticTemplate2("JGE"));
            arthJump++;
            return;
        }

        if (command.equals("eq")) {
            writeOut.print(arithmeticTemplate2("JNE"));
            arthJump++;
            return;
        }

        if (command.equals("not")) {
            writeOut.print("@SP\nA=M-1\nM=!M\n");
            return;
        }

        if (command.equals("neg")) {
            writeOut.print("D=0\n@SP\nA=M-1\nM=D-M\n");
            return;
        }

        throw new IllegalCallerException("invalid call method");
    }

    public void writePushPop(String command, String segment, int index) {
        if (command.equals("C_PUSH")) {
            writePush(segment, index);
            return;
        }

        if (command.equals("C_POP")) {
            writePop(segment, index);
            return;
        }

        throw new IllegalCallerException("invalid call method");
    }

    private void writePush(String segment, int index) {
        if (segment.equals("constant")) {
            writeOut.print("@" + index + "\n" + "D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n");
            return;
        }

        if (segment.equals("local")) {
            writeOut.print(pushTemplate("LCL", index, false));
            return;
        }

        if (segment.equals("argument")) {
            writeOut.print(pushTemplate("ARG", index, false));
            return;
        }

        if (segment.equals("this")) {
            writeOut.print(pushTemplate("THIS", index, false));
            return;
        }

        if (segment.equals("that")) {
            writeOut.print(pushTemplate("THAT", index, false));
            return;
        }

        if (segment.equals("temp")) {
            writeOut.print(pushTemplate("R5", index + 5, false));
            return;
        }

        if (segment.equals("pointer") && index == 0) {
            writeOut.print(pushTemplate("THIS", index, true));
            return;
        }

        if (segment.equals("pointer") && index == 1) {
            writeOut.print(pushTemplate("THAT", index, true));
            return;
        }

        if (segment.equals("static")) {
            writeOut.print(pushTemplate(String.valueOf(16 + index), index, true));
            return;
        }

        throw new IllegalCallerException("invalid call method");
    }

    private void writePop(String segment, int index) {

        if (segment.equals("local")) {
            writeOut.print(popTemplate("LCL", index, false));
            return;
        }

        if (segment.equals("argument")) {
            writeOut.print(popTemplate("ARG", index, false));
            return;
        }

        if (segment.equals("this")) {
            writeOut.print(popTemplate("THIS", index, false));
            return;
        }

        if (segment.equals("that")) {
            writeOut.print(popTemplate("THAT", index, false));
            return;
        }

        if (segment.equals("temp")) {
            writeOut.print(popTemplate("R5", index + 5, false));
            return;
        }

        if (segment.equals("pointer") && index == 0) {
            writeOut.print(popTemplate("THIS", index, true));
            return;
        }

        if (segment.equals("pointer") && index == 1) {
            writeOut.print(popTemplate("THAT", index, true));
            return;
        }

        if (segment.equals("static")) {
            writeOut.print(popTemplate(String.valueOf(16 + index), index, true));
            return;
        }

        throw new IllegalCallerException("invalid call method");
    }

    private String popTemplate(String segment, int index, boolean isDirect) {
        String noPointer = isDirect ? "" : "@" + index + "\n" + "A=D+A\nD=M\n";
        return "@" + segment + "\n" +
                noPointer +
                "@R13\n" +
                "M=D\n" +
                "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "@R13\n" +
                "A=M\n" +
                "M=D\n";
    }

    private String pushTemplate(String segment, int index, boolean isDirect) {
        String noPointer = isDirect ? "" : "@" + index + "\n" + "A=D+A\nD=M\n";
        return "@" + segment + "\n" +
                "D=M\n" +
                noPointer +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n";
    }

    private String arithmeticTemplate1() {
        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n";
    }

    private String arithmeticTemplate2(String type) {
        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "D=M-D\n" +
                "@FALSE" + arthJump + "\n" +
                "D;" + type + "\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "@CONTINUE" + arthJump + "\n" +
                "0;JMP\n" +
                "(FALSE" + arthJump + ")\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "(CONTINUE" + arthJump + ")\n";
    }

    public void close() {
        writeOut.close();;
    }
}
