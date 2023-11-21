import javax.swing.*;
import java.awt.*;

import static java.awt.event.KeyEvent.*;

public class MainForm {

    public MainForm() {
        // Alt + key, like the original program
        newTaskFileButton.setMnemonic(VK_N);
        addTaskButton.setMnemonic(VK_A);
        removeTaskButton.setMnemonic(VK_R);
        searchTaskButton.setMnemonic(VK_T);
        listTasksButton.setMnemonic(VK_L);
        saveTasksButton.setMnemonic(VK_S);
        exitButton.setMnemonic(VK_E);

        exitButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("mainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,300));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private JFrame frame;
    private JButton newTaskFileButton;
    private JPanel mainPanel;
    private JButton addTaskButton;
    private JButton removeTaskButton;
    private JButton searchTaskButton;
    private JButton listTasksButton;
    private JButton saveTasksButton;
    private JLabel nTasksLabel;
    private JButton exitButton;
}
