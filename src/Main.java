import s3270.Wrapper;

import java.io.IOException;
import java.util.logging.Logger;



public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s%6$s%n");

        var wrapper = new Wrapper();
        wrapper.connect("155.210.71.101:123");



        wrapper.enter();
        wrapper.string("PROG");
        wrapper.tab();
        wrapper.string("PROG123");
        wrapper.enter();
        wrapper.enter();



        wrapper.disconnect();
        wrapper.exit();
        //MainForm.main(args);
    }
}
