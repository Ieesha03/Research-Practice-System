package views;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ProfessorDetailsForm extends JFrame {
    private int professorId;
    private Image backgroundImage8; // To hold the background image
    private JTextField areaField; // Instance variable for area of work field
    private JTextField skillsField; // Instance variable for preferred skills field

    public ProfessorDetailsForm(int professorId) {
        this.professorId = professorId;

        // Load the background image
        try {
            backgroundImage8 = ImageIO.read(new File("src/images/background8.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Enter Professor Details");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Custom JPanel with background image
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage8 != null) {
                    g.drawImage(backgroundImage8, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Padding around components

        // Welcome label
        JLabel welcomeLabel = new JLabel("Enter Your Details Below");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE); // Set text color to white
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        contentPanel.add(welcomeLabel, gbc);

        // Form fields for professor details
        areaField = addFormField(contentPanel, "Area of Work*:", 1);
        skillsField = addFormField(contentPanel, "Preferred Skills*:", 2);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(150, 40)); // Set a fixed size for the button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDetails();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across both columns
        contentPanel.add(saveButton, gbc);

        // Set content panel as the main panel
        setContentPane(contentPanel);
        setVisible(true);
    }

    // Helper method to add a form field with a label and a text field
    private JTextField addFormField(JPanel panel, String labelText, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE); // Set text color to white for visibility on the background

        // Create the text field
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 40)); // Increase the height of the text field
        textField.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 235), 2)); // Blue border
        textField.setMargin(new Insets(10, 10, 10, 10)); // Internal margin to increase space inside

        // GridBagConstraints for positioning the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.insets = new Insets(10, 0, 10, 0); // Vertical space between fields (top and bottom)
        panel.add(textField, gbc);

        return textField; // Return the text field so it can be assigned to an instance variable
    }

    // Method to save professor details to the database
    private void saveDetails() {
        // Extract details from the form fields
        String areaOfWork = areaField.getText();
        String preferredSkills = skillsField.getText();

        // Validate mandatory fields
        if (areaOfWork.isEmpty() || preferredSkills.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill out the mandatory fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to the database
        DatabaseManager.saveProfessorDetails(professorId, areaOfWork, preferredSkills);
        JOptionPane.showMessageDialog(null, "Details saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Open the received applications page
        openReceivedApplicationsPage();
    }

    private void openReceivedApplicationsPage() {
        // Open the Received Applications page, passing professorId
        ReceivedApplicationsUI receivedApplicationsUI = new ReceivedApplicationsUI(professorId);
        receivedApplicationsUI.setVisible(true);
        dispose(); // Close the professor details form
    }

    public static void main(String[] args) {
        // Simulating professorId for testing purposes
        new ProfessorDetailsForm(101);
    }
}
