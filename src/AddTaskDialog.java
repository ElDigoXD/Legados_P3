import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
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
    }


    private boolean validateTask() {
        return !(numberField.getText().isBlank()
                || nameField.getText().isBlank()
                || descriptionField.getText().isBlank()
                || dateField.getText().isBlank());
    }

    private void onOK() {
        // Validate inputs
        // Interact with the mainframe
        // If everything is ok close
        // Else modal error and not close

        if (!validateTask()) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Validation of the inputs failed.\nMake sure all fields are full.", "Validation Failed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (false) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Mainframe error", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
    }

    private void onCancel() {
        // Do nothing in the server
        dispose();
    }

    public static void main(String[] args) {
        AddTaskDialog dialog = new AddTaskDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        numberField = new JFormattedTextField(new NumberFormatter());
        dateField = new JFormattedTextField(new SimpleDateFormat("dd-MM-yy"));
    }
}
