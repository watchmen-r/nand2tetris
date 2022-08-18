import java.io.File;
import java.io.PrintWriter;

public class VMtranslator {
    public static void main(String[] args) {
        if(args.length != 1) {
            return;
        }

        // args[0] is folder or filename whose extension is .vm
        File source = new File(args[0]);
        if(source.isDirectory()) {

        }

        
    }

    private void translateVm(File source) {
        if(source.getName().endsWith(".vm")) {
            return;
        }
    }
}
