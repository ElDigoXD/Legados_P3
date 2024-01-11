package s3270;

import java.io.*;
import java.util.logging.*;

import static java.lang.String.format;

public class Wrapper {
    private static final Logger LOGGER = Logger.getGlobal();

    Process process;
    PrintWriter writer;
    BufferedReader reader;


    public Wrapper(String ws3270_path) throws IOException {
        LOGGER.setLevel(Level.ALL);

        var handler = new StreamHandler(System.out, new SimpleFormatter()) {
            @Override
            public synchronized void publish(final LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        handler.setLevel(Level.FINE);
        LOGGER.addHandler(handler);


        process = new ProcessBuilder()
                .command(ws3270_path)
                .start();


        writer = new PrintWriter(process.outputWriter(), true);

        reader = process.inputReader();
    }

    public Wrapper() throws IOException {
        this("C:\\Program Files\\wc3270\\ws3270.exe");
    }


    private void printfln(String formatStr, Object... args) {
        writer.printf(formatStr + '\n', args);
        writer.flush();
        LOGGER.fine(format("Written '" + formatStr + "'", args).replace("\n", "\\n"));
    }

    private void ignoreLine() throws IOException {
        var line = reader.readLine();
        LOGGER.finer(format("Ignored '%s'", line).replace("\n", "\\n"));

    }

    private String readLine() throws IOException {

        var line = reader.readLine();
        LOGGER.fine(format("Read '%s'", line).replace("\n", "\\n"));
        return line;
    }

    private void emptyReader() throws IOException {
        while (reader.ready()) //noinspection ResultOfMethodCallIgnored
            reader.read();
    }

    public void connect(String s) throws IOException {
        printfln("connect(\"%s\")", s);
        throwIfNotOk(format("Couldn't connect(\"%s\")", s));
    }

    public void exit() throws IOException {
        printfln("exit");
        throwIfNotOk("Couldn't exit");
    }

    public void disconnect() throws IOException {
        printfln("disconnect");
        throwIfNotOk("Couldn't disconnect");
    }

    public void string(String string) throws IOException {
        printfln("string(\"%s\")", string);
        throwIfNotOk(format("Couldn't use string(\"%s\")", string));
    }

    public void enter() throws IOException {
        printfln("enter");
        throwIfNotOk("Couldn't enter");
    }

    public void tab() throws IOException {
        printfln("tab");
        throwIfNotOk("Couldn't tab");
    }

    public void pf(int fKey) throws IOException {
        printfln("pf(%s)", fKey);
        throwIfNotOk(format("Couldn't pf(%s)", fKey));
    }

    public void wait_(double seconds, String mode) throws IOException {
        printfln("wait(%s, %s)", seconds, mode);
        throwIfNotOk(format("Couldn't wait(%s, %s)", seconds, mode));
    }

    public void waitSeconds(double seconds) throws IOException {
        wait_(seconds, "seconds");
    }

    public void waitOutput() throws IOException {
        wait_(5, "output");
    }

    public String ascii() throws IOException {
        int lines = 43;
        printfln("ascii");
        var sb = new StringBuilder();

        for (int i = 0; i < lines - 1; i++) {
            sb.append(reader.readLine().replace("data: ", ""));
            sb.append("\n");
        }
        sb.append(reader.readLine().replace("data: ", ""));

        throwIfNotOk("Couldn't ascii");
        return sb.toString();
    }

    private void throwIfNotOk(String message) throws IOException {
        ignoreLine();
        if (!readLine().startsWith("ok")) {
            emptyReader();
            throw new RuntimeException(message);
        }
    }

    public String waitAscii() throws IOException {
        waitOutput();
        waitSeconds(0.5);
        return ascii();
    }

    public void stringEnterWait(String string) throws IOException {
        string(string);
        enter();
        waitSeconds(0.3);
    }
}
