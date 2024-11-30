package views;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ApplicationForm extends JPanel {

    private JTextField interestField;
    private JTextField skillsField;
    private JTextField publicationsField;
    private JTextField experienceField;
    private JButton proceedButton;
    private int studentId; // Assuming the studentId will be passed from the previous screen
    private Image backgroundImage9;

    public ApplicationForm(int studentId) {
        this.studentId = studentId;

        // Load the background image
        try {
            backgroundImage9 = ImageIO.read(new File("src/images/background9.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the layout to GridBagLayout for the form fields
        setLayout(new BorderLayout());

        // Custom JPanel with background image
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage9 != null) {
                    g.drawImage(backgroundImage9, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding around components

        // Welcome and instruction text
        JLabel welcomeLabel = new JLabel("Welcome! Fill up the form to proceed.", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make the welcome label span across both columns
        contentPanel.add(welcomeLabel, gbc);

        // Form fields
        addFormField(contentPanel, "Area of Interest*:", 1);
        addFormField(contentPanel, "Skills*:", 2);
        addFormField(contentPanel, "Research Publications:", 3);
        addFormField(contentPanel, "Prior Research Experience:", 4);

        // Proceed button
        proceedButton = new JButton("Proceed");
        proceedButton.setFont(new Font("Arial", Font.BOLD, 16));
        proceedButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setPreferredSize(new Dimension(150, 40)); // Set a fixed size for the button
        proceedButton.addActionListener(new ProceedButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across both columns
        contentPanel.add(proceedButton, gbc);


        // Set the content panel as the main panel
        add(contentPanel, BorderLayout.CENTER);

    }

    // Helper method to add a form field with a label and a text field
    private void addFormField(JPanel panel, String labelText, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE); // Set text color to white for visibility on background

        // Create the text field
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 40)); // Increase the height of the text field
        textField.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 235), 2)); // Border with blue color
        textField.setMargin(new Insets(10, 10, 10, 10)); // Add internal margin to increase space inside the text field

        // GridBagConstraints for positioning the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.insets = new Insets(10, 0, 10, 0); // Add vertical space between fields (top and bottom)
        panel.add(textField, gbc);

        // Set the field based on label
        switch (labelText) {
            case "Area of Interest*:": interestField = textField; break;
            case "Skills*:": skillsField = textField; break;
            case "Research Publications:": publicationsField = textField; break;
            case "Prior Research Experience:": experienceField = textField; break;
        }
    }


    private class ProceedButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Mandatory fields validation
            String areaOfInterest = interestField.getText();
            String skills = skillsField.getText();

            if (areaOfInterest.isEmpty() || skills.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out the mandatory fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String publications = publicationsField.getText();
            String experience = experienceField.getText();

            // Submit the application to the database
            DatabaseManager.submitApplication(studentId, areaOfInterest, skills, publications, experience);

            JOptionPane.showMessageDialog(null, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // After submission, load the professor list UI
            openProfessorList();
        }
    }

    private void openProfessorList() {
        // Create and display the ProfessorListUI
        ProfessorListUI professorListUI = new ProfessorListUI(studentId); // Pass studentId to the list
        JFrame professorFrame = new JFrame("Professor List");
        professorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        professorFrame.setSize(600, 500);
        professorFrame.setLocationRelativeTo(null);
        professorFrame.add(professorListUI); // Add the professor list UI panel
        professorFrame.setVisible(true); // Show the professor list window

        // Close the current application form window
        Window window = SwingUtilities.windowForComponent(ApplicationForm.this);
        window.dispose(); // Close the application form window
    }

    public static void main(String[] args) {
        // Simulating studentId for testing purposes
        new ApplicationForm(1);
    }
}
