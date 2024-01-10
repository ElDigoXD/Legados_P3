package s3270;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.*;

import static java.lang.String.format;

public class Wrapper {
    private static final Logger LOGGER = Logger.getGlobal();

    @NotNull
    Process process;
    @NotNull
    PrintWriter writer;
    @NotNull
    BufferedReader reader;


    public Wrapper() throws Exception {
        LOGGER.setLevel(Level.ALL);

        var handler = new StreamHandler(System.out, new SimpleFormatter()) {
            @Override
            public synchronized void publish(final LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);


        process = new ProcessBuilder()
                .command("C:\\Program Files\\wc3270\\ws3270.exe")
                .start();


        writer = new PrintWriter(process.outputWriter(), true);

        reader = process.inputReader();
    }


    private void printfln(@NotNull String formatStr, Object... args) {
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
        throw_if_not_ok(format("Couldn't connect(\"%s\")", s));
    }

    public void exit() throws IOException {
        printfln("exit");
        throw_if_not_ok("Couldn't exit");
    }

    public void disconnect() throws IOException {
        printfln("disconnect");
        throw_if_not_ok("Couldn't disconnect");
    }

    public void string(String string) throws IOException {
        printfln("string(\"%s\")", string);
        throw_if_not_ok(format("Couldn't use string(\"%s\")", string));
    }

    public void enter() throws IOException {
        printfln("enter");
        throw_if_not_ok("Couldn't enter");
    }

    public void tab() throws IOException {
        printfln("tab");
        throw_if_not_ok("Couldn't tab");
    }

    public void pf(int fKey) throws IOException {
        printfln("pf(%s)", fKey);
        throw_if_not_ok(format("Couldn't pf(%s)", fKey));
    }

    public void wait_(double seconds, String mode) throws IOException {
        printfln("wait(%s, %s)", seconds, mode);
        throw_if_not_ok(format("Couldn't wait(%s, %s)", seconds, mode));
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
        sb.append("╔").append("═".repeat(81)).append("╗\n");
        for (int i = 0; i < lines; i++) {
            sb.append(reader.readLine().replace("data:", "║"));
            sb.append("║\n");
        }
        sb.append("╚").append("═".repeat(81)).append("╝");
        throw_if_not_ok("Couldn't ascii");
        return sb.toString();
    }

    private void throw_if_not_ok(String message) throws IOException {
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
        this.string(string);
        this.enter();
        this.waitSeconds(0.3);
    }
}
