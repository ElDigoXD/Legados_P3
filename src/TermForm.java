import s3270.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TermForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("TermForm");
        frame.setContentPane(new TermForm().panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(630, 840));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public TermForm() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%4$s: %5$s%6$s%n");
        try {
            wrapper = new Wrapper();


            connectButton.addActionListener(e -> {
                try {
                    wrapper.connect("155.210.71.101:123");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            disconnectButton.addActionListener(e -> {
                try {
                    wrapper.disconnect();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            exitButton.addActionListener(e -> {
                try {
                    wrapper.pf(3);
                    wrapper.pf(3);
                    wrapper.pf(3);
                    wrapper.string("e\\n");
                    wrapper.enter();
                    wrapper.pf(3);
                    wrapper.exit();
                    System.exit(0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            enterButton.addActionListener(e -> {
                try {
                    wrapper.enter();
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            tabButton.addActionListener(e -> {
                try {
                    wrapper.tab();
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            f3Button.addActionListener(e -> {
                try {
                    wrapper.string("\\pf3");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            uTpButton.addActionListener(e -> {
                try {
                    wrapper.string("prog\\tprog123\\n");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            stringButton.addActionListener(e -> {
                try {
                    wrapper.string(stringTextField.getText() + "\\n");
                    drawScreen();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            asciiButton.addActionListener(e -> {
                try {
                    wrapper.waitSeconds(0.3);
                    wrapper.ascii();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            launchButton.addActionListener(e -> {
                try {
                    wrapper.string("\\nprog\\tprog123\\ntasks2.job\\n");

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
            displayPane.setText(wrapper.waitAscii());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Wrapper wrapper;
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
