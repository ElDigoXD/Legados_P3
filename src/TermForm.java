import s3270.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class TermForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("TermForm");
        frame.setContentPane(new TermForm(Optional.empty()).panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(630, 840));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void openTermForm(Wrapper wrapper, Component relativeTo) {
        JFrame frame = new JFrame("TermForm");
        frame.setContentPane(new TermForm(Optional.ofNullable(wrapper)).panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(630, 840));
        frame.setLocationRelativeTo(relativeTo);
        frame.pack();
        frame.setVisible(true);
    }

    public TermForm(Optional<Wrapper> wrapper) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s%6$s%n");
        try {
            w = wrapper.orElse(new Wrapper());
            displayPane.setText(w.ascii());

            connectButton.addActionListener(e -> {
                try {
                    w.connect("155.210.71.101:123");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            disconnectButton.addActionListener(e -> {
                try {
                    w.disconnect();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            exitButton.addActionListener(e -> {
                try {
                    w.pf(3);
                    w.pf(3);
                    w.pf(3);
                    w.string("e\\n");
                    w.enter();
                    w.pf(3);
                    w.disconnect();
                    System.exit(0);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            enterButton.addActionListener(e -> {
                try {
                    w.enter();
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            tabButton.addActionListener(e -> {
                try {
                    w.tab();
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            f3Button.addActionListener(e -> {
                try {
                    w.string("\\pf3");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            uTpButton.addActionListener(e -> {
                try {
                    w.string("prog\\tprog123\\n");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            stringButton.addActionListener(e -> {
                try {
                    w.string(stringTextField.getText() + "\\n");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            asciiButton.addActionListener(e -> {
                try {
                    w.waitSeconds(0.3);
                    w.ascii();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            launchButton.addActionListener(e -> {
                try {
                    // Login
                    w.enter();
                    w.waitSeconds(0.3);
                    w.string("prog");
                    w.tab();
                    w.waitSeconds(0.3);
                    w.string("prog123");
                    w.enter();
                    w.waitSeconds(0.3);
                    w.enter();
                    w.waitSeconds(0.3);

                    // Open the program
                    w.string("tasks2.job");
                    w.enter();
                    w.waitSeconds(0.3);


                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawScreen() {
        try {
            displayPane.setText(w.waitAscii());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Wrapper w;
    private JPanel panel1;
    private JTextPane displayPane;
    private JButton enterButton;
    private JButton tabButton;
    private JButton f3Button;
    private JButton uTpButton;
    private JButton stringButton;
    private JTextField stringTextField;
    private JButton asciiButton;
    private JButton connectButton;
    private JButton exitButton;
    private JButton disconnectButton;
    private JButton launchButton;
}
