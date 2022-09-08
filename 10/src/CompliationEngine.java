import java.io.File;
import java.io.PrintWriter;

public class CompliationEngine {

    private PrintWriter outputWriter;
    private PrintWriter tokenPrintWriter;
    private JackTokenizer tokenizer;
     
    public CompliationEngine(File input, File output) {
        tokenizer = new JackTokenizer(input);
        outputWriter = new PrintWriter(output);
    }

    
}
