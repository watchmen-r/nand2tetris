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

        compileClassVarDec();
        compileSubroutine();

    }

    public void compileClassVarDec() {
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '}') {
            tokenizer.pointerBack();
            return;
        }

        if (!tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("constructor"))
                || !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("function"))
                || !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("method"))) {
            tokenizer.pointerBack();
            return;
        }

        // classVarDec starts 'static' or 'field'
        if (!tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("static"))
                && !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("field"))) {
            throw new Error("invalid source code.");
        }

        outputWriter.print("<classVarDec>\n");
        outputWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");
        tokenPrintWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");

        // next is Type, according to book P233.
        compileType();

        for (;;) {
            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER) {
                throw new Error("invalid source code.");
            }
            outputWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            tokenPrintWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

            tokenizer.advance();

            if (tokenizer.symbol() == ',') {
                outputWriter.print("<symbol>,</symbol>\n");
                tokenPrintWriter.print("<symbol>,</symbol>\n");
            } else if (tokenizer.symbol() == ';') {
                outputWriter.print("<symbol>;</symbol>\n");
                tokenPrintWriter.print("<symbol>;</symbol>\n");
                break;
            } else {
                throw new Error("invalid source code.");
            }
        }
        outputWriter.print("</classVarDec>\n");
        compileClassVarDec();
    }

    public void compileSubroutine() {
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '}') {
            tokenizer.pointerBack();
            return;
        }

        // subroutineDec starts 'constructor' or 'functino' or 'method'
        if (!tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("keyword"))
                || (!tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("constructor"))
                        || !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("function"))
                        || !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("method")))) {
            throw new Error("invalid source code.");
        }

        outputWriter.print("<subroutineDec>\n");
        outputWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");
        tokenPrintWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");

        tokenizer.advance();
        // check next token is void or type
        if (tokenizer.tokenType().equals(JackTokenizer.KEYWORD)
                && tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("void"))) {
            outputWriter.print("<keyword>void</keyword>\n");
            tokenPrintWriter.print("<keyword>void</keyword>\n");
        } else {
            tokenizer.pointerBack();
            compileType();
        }

        // next token has to be subroutineName which is identifier.
        tokenizer.advance();
        if (!tokenizer.tokenType().equals(JackTokenizer.IDENTIFIER)) {
            throw new Error("invalid source code. Next token has to be identifier of subroutineName.");
        }

        outputWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
        tokenPrintWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

        nextSymbol('(');

        // next token has to be parameterList
        outputWriter.print("<parameterList>\n");
        compileParameterList();
        outputWriter.print("</parameterList>\n");

        nextSymbol(')');

        compileSubroutineBody();
    }

    public void compileType() {
        tokenizer.advance();

        // In case token is 'int' or 'char' or 'boolena'
        if (tokenizer.tokenType().equals(JackTokenizer.KEYWORD)
                && (tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("int"))
                        || tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("char"))
                        || tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("boolean")))) {
            outputWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");
            tokenPrintWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");

            // In case token is className(className is identifier)
        } else if (tokenizer.tokenType() == JackTokenizer.IDENTIFIER) {
            outputWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");
            tokenPrintWriter.print("<keyword>" + tokenizer.getToken() + "</keyword>\n");
        } else {
            throw new Error("invalid source code. Check type.");
        }
    }

    public void compileSubroutineBody() {
        outputWriter.print("<subroutineBody>\n");
        nextSymbol('{');

        // next is varDec
        compileVarDec();

        // next is statement
        outputWriter.print("<statements>\n");
        compileStatement();
        outputWriter.print("</statements>\n");

        nextSymbol('}');
        outputWriter.print("</subroutineBody>\n");
    }

    public void compileStatement() {
        // TODO implement
    }

    public void compileParameterList() {
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == ')') {
            tokenizer.pointerBack();
            return;
        }

        tokenizer.pointerBack();
        for (;;) {
            // at first. there is a type.
            compileType();

            // next is varName which is identifier.
            tokenizer.advance();
            if (!tokenizer.tokenType().equals(JackTokenizer.IDENTIFIER)) {
                throw new Error("invalid source code. Next token has to be identifier of subroutineName.");
            }
            outputWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            tokenPrintWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

            // next is '.' or ')'
            tokenizer.advance();
            if (!tokenizer.tokenType().equals(JackTokenizer.SYMBOL)
                    || (tokenizer.symbol() != ',' && tokenizer.symbol() != ')')) {
                throw new Error("invalid source code. Next token has to be identifier of subroutineName.");
            }

            if (tokenizer.symbol() == ',') {
                outputWriter.print("<symbol>,</symbol>\n");
                tokenPrintWriter.print("<symbol>,</symbol>\n");
            } else {
                tokenizer.pointerBack();
                break;
            }
        }
    }

    private void compileVarDec() {
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.KEYWORD
                || !tokenizer.keyWord().equals(JackTokenizer.keyWordMap.get("var"))) {
            tokenizer.pointerBack();
            return;
        }

        outputWriter.print("<varDec>\n");
        outputWriter.print("<keyword>var</keyword>\n");
        tokenPrintWriter.print("<keyword>var</keyword>\n");

        // next token is type
        compileType();

        for(;;) {
            // next token is varName which is identifier
            tokenizer.advance();
            if (!tokenizer.tokenType().equals(JackTokenizer.IDENTIFIER)) {
                throw new Error("invalid source code. Next token has to be identifier of compileVarDec.");
            }
            outputWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            tokenPrintWriter.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

            // next is ',' or ';'
            tokenizer.advance();
            if (!tokenizer.tokenType().equals(JackTokenizer.SYMBOL)
                    || (tokenizer.symbol() != ',' && tokenizer.symbol() != ';')) {
                throw new Error("invalid source code. Next token has to be identifier of subroutineName.");
            }

            if (tokenizer.symbol() == ',') {
                outputWriter.print("<symbol>,</symbol>\n");
                tokenPrintWriter.print("<symbol>,</symbol>\n");
            } else {
                outputWriter.print("<symbol>;</symbol>\n");
                tokenPrintWriter.print("<symbol>;</symbol>\n");
                break;
            }
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
