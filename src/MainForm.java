import s3270.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static java.awt.event.KeyEvent.*;

public class MainForm {

    private static final boolean DEBUG = true;
    private static final String IP = "155.210.71.101:123";
    private static final String WS3270_PATH = "C:\\Program Files\\wc3270\\ws3270.exe";


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
            w = new Wrapper(WS3270_PATH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.mainPanel,
                    "Error loading ws3270 in path: " + WS3270_PATH);
            System.exit(0);
        }

        try {
            // Connect
            w.connect(IP);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.mainPanel,
                    "Cannot connect to " + IP);
            System.exit(0);
        }

        try {
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

            updateTaskNumber();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.mainPanel,
                    "Error while logging-in");
        }

        newTaskFileButton.addActionListener(e -> {
            try {
                w.stringEnterWait("n");
                w.stringEnterWait("y");
                w.enter();
                w.waitSeconds(0.3);
                updateTaskNumber();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel, "Error creating new file");
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
                    w.waitSeconds(0.3);
                    updateTaskNumber();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this.mainPanel, "Error adding task");
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
                w.waitSeconds(0.3);
                updateTaskNumber();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel, "Error adding dummy task");
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
                    w.waitSeconds(0.3);
                    updateTaskNumber();
                } else {
                    JOptionPane.showMessageDialog(this.mainPanel, "TASK NOT FOUND");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel, "Error removing task");
            }
        });

        searchTaskButton.addActionListener(e -> {
            try {
                w.stringEnterWait("t");
                String input = JOptionPane.showInputDialog(this.mainPanel,
                        "Enter date in format dd mm yy");
                while (input.isEmpty()) {
                    input = JOptionPane.showInputDialog(this.mainPanel,
                            "Enter date in format dd mm yy");
                }

                w.stringEnterWait(input);

                var listOfTasks = parseListOfTasks(input);

                if (listOfTasks.isBlank())
                    listOfTasks = "Tasks not found";
                JOptionPane.showMessageDialog(this.mainPanel, listOfTasks);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel,
                        "Error searching task");
            }
        });

        listTasksButton.addActionListener(e -> {
            try {

                w.stringEnterWait("l");
                var listOfTasks = parseListOfTasks(null);
                JOptionPane.showMessageDialog(this.mainPanel, listOfTasks);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel,
                        "Error listing tasks");
            }
        });

        saveTasksButton.addActionListener(e -> {
            try {

                // Untested
                w.stringEnterWait("s");

                w.enter();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel, "Error saving tasks");
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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.mainPanel, "Error exiting");
            }
        });

        if (DEBUG) {
            addTask2Button.setVisible(false);
            TermForm.openTermForm(w, this.mainPanel);
        }

    }

    private void updateTaskNumber() throws IOException {
        var ascii = w.ascii().lines().toList().get(2);
        nTasksLabel.setText(ascii.substring(1, ascii.indexOf(")")) + " task/s");
    }

    private String parseListOfTasks(String listStartToken) throws IOException {
        var moreResultsToken = "More...   ";
        listStartToken = listStartToken != null ? listStartToken : "**LIST TASK**                                                                   \n";
        var listEndToken = "**END**                                                                         \n";
        var screenEndToken = "\n------";


        var ascii = w.ascii();

        var listStartIndex = ascii.indexOf(listStartToken) + listStartToken.length();

        var sb = new StringBuilder();

        while (ascii.endsWith(moreResultsToken)) {
            var screenEndIndex = ascii.indexOf(screenEndToken);

            sb.append(ascii, listStartIndex, screenEndIndex + 1);

            w.enter();
            w.waitSeconds(0.3);

            ascii = w.ascii();
            listStartIndex = 0;
        }

        var listEndIndex = ascii.indexOf(listEndToken);

        sb.append(ascii, listStartIndex, listEndIndex);

        w.enter();
        return sb.toString();
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

    Wrapper w;
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
