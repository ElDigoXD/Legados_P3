import s3270.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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


        try {
            w = new Wrapper();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            // Connect
            w.connect("155.210.71.101:123");
            // Login
            w.enter();
            w.waitSeconds(0.3);
            w.string("prog");
            w.tab();
            w.waitSeconds(0.3);
            w.stringEnterWait("prog123");
            w.enter();
            w.waitSeconds(0.3);
            // Open the program
            w.stringEnterWait("tasks2.job");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        newTaskFileButton.addActionListener(e -> {
            try {
                w.string("n");
                w.enter();
                w.waitSeconds(0.3);
                w.string("y");
                w.enter();
                w.waitSeconds(0.3);
                w.enter();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        addTaskButton.addActionListener(e -> {
            var addTaskDialog = new AddTaskDialog(null);
            addTaskDialog.setLocationRelativeTo(this.mainPanel);

            addTaskDialog.setVisible(true);
            var task = addTaskDialog.getTask();
            if (task != null) {
                try {
                    w.stringEnterWait("a");
                    w.stringEnterWait(String.valueOf(task.number()));

                    while (!w.ascii().contains("TASK NAME (MAX 16 CAR):")) {

                        w.enter();
                        JOptionPane.showMessageDialog(this.mainPanel, "TASK NUMBER REPEATED");
                        addTaskDialog.setVisible(true);
                        task = addTaskDialog.getTask();

                        w.stringEnterWait(String.valueOf(task.number()));
                    }

                    w.stringEnterWait(task.name());
                    w.stringEnterWait(task.description());
                    w.stringEnterWait(task.date());

                    w.enter();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        addTask2Button.addActionListener(e -> {
            try {
                w.stringEnterWait("a");
                w.stringEnterWait("1");

                if (!w.ascii().contains("TASK NAME (MAX 16 CAR):")) {
                    JOptionPane.showMessageDialog(this.mainPanel, "TASK NUMBER REPEATED");
                    w.stringEnterWait("2");
                }

                w.stringEnterWait("name");
                w.stringEnterWait("desc");
                w.stringEnterWait("11 11 11");

                w.enter();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        removeTaskButton.addActionListener(e -> {
            try {
                w.stringEnterWait("r");

                var inputValue = JOptionPane.showInputDialog(this.mainPanel, "Enter task number");
                w.stringEnterWait(inputValue);
                if (w.ascii().contains("CONFIRM")) {
                    w.stringEnterWait("y");
                    w.enter();
                } else {
                    JOptionPane.showMessageDialog(this.mainPanel, "TASK NOT FOUND");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        searchTaskButton.addActionListener(e -> {
            try {
                // Untested
                w.stringEnterWait("t");
                w.stringEnterWait(JOptionPane.showInputDialog(this.mainPanel, "Enter date in format dd mm yy"));

                w.enter();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        listTasksButton.addActionListener(e -> {
            try {

                // Untested
                w.stringEnterWait("l");

                w.enter();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        saveTasksButton.addActionListener(e -> {
            try {

                // Untested
                w.stringEnterWait("s");
                w.stringEnterWait("y");

                w.enter();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exitButton.addActionListener(e -> {
            try {
                // Important to not break the mainframe
                w.pf(3);
                w.pf(3);
                w.pf(3);
                w.stringEnterWait("e");
                w.stringEnterWait("e");
                w.enter();
                w.waitSeconds(0.3);
                w.enter();
                w.pf(3);
                w.pf(3);
                w.waitSeconds(0.3);
                w.disconnect();
                w.exit();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        TermForm.openTermForm(w, this.mainPanel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("mainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200, 300));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    Wrapper w = null;
    private JFrame frame;
    private JButton newTaskFileButton;
    private JPanel mainPanel;
    private JButton addTaskButton;

    private JButton addTask2Button;

    private JButton removeTaskButton;
    private JButton searchTaskButton;
    private JButton listTasksButton;
    private JButton saveTasksButton;
    private JLabel nTasksLabel;
    private JButton exitButton;
}
