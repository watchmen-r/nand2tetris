import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CompliationEngine {

    private PrintWriter outputWriter;
    private PrintWriter tokenPrintWriter;
    private JackTokenizer tokenizer;

    public CompliationEngine(File input, File output) throws IOException {
        tokenizer = new JackTokenizer(input);
        outputWriter = new PrintWriter(output);
    }

    public void compileClass() {
        tokenizer.advance();

        outputWriter.print("<class>\n");
        tokenPrintWriter.print("<tokens>\n");

        outputWriter.print("<keyword>class</keyword>\n");
        tokenPrintWriter.print("<tokens>class</keyword>\n");

        // to output class name
        tokenizer.advance();
        outputWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
        tokenPrintWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

        nextSymbol('{');

    }

    public void compileClassVarDec() {
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '}') {
            tokenizer.pointerBack();
            return;
        }

        if (tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("constructor"))
                || tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("function"))
                || tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("method")) {
            tokenizer.pointerBack();
            return;
        }

    }

    private void nextSymbol(char symbol) {
        tokenizer.advance();
        if (tokenizer.tokenType().equals(JackTokenizer.SYMBOL) && tokenizer.symbol() == symbol) {
            outputWriter.print("<symbol>" + symbol + "</symbol>\n");
            tokenPrintWriter.print("<symbol>" + symbol + "</symbol>\n");
            return;
        }
        throw new Error("invalid code. next symbol have to be " + symbol + ".");
    }
}
