import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class AddTaskDialog extends JDialog {
    private JPanel contentPane;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField nameField;
    private JFormattedTextField numberField;
    private JTextField descriptionField;
    private JFormattedTextField dateField;

    private Task task;

    public AddTaskDialog(@Nullable Task task) {
        if (task != null) {
            numberField.setText(String.valueOf(task.number()));
            nameField.setText(task.name());
            descriptionField.setName(task.description());
            dateField.setName(task.date());
        }
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();
    }


    private boolean validateTask() {
        return !(numberField.getText().isBlank()
                || nameField.getText().isBlank()
                || descriptionField.getText().isBlank()
                || dateField.getText().isBlank()
                || nameField.getText().length() > 16
                || descriptionField.getText().length() > 32
        );
    }

    private void onOK() {
        // Validate inputs
        // Interact with the mainframe
        // If everything is ok close
        // Else modal error and not close

        if (!validateTask()) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Validation of the inputs failed.", "Validation Failed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            task = new Task((Integer) numberField.getFormatter().stringToValue(numberField.getText()), nameField.getText(), descriptionField.getText(), dateField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setVisible(false);
    }

    private void onCancel() {
        // Do nothing in the server
        setVisible(false);
    }

    public static void main(String[] args) {
        AddTaskDialog dialog = new AddTaskDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        numberField = new JFormattedTextField(new NumberFormatter());
        ((NumberFormatter) numberField.getFormatter()).setValueClass(Integer.class);
        dateField = new JFormattedTextField(new SimpleDateFormat("dd MM yy"));
    }

    public @Nullable Task getTask() {
        return task;
    }
}
