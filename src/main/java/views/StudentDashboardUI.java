package views;

import database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StudentDashboardUI extends JFrame {

    private String studentName;
    private int studentId;
    private Image backgroundImage7;

    // Constructor accepting student's name and id
    public StudentDashboardUI(String studentName, int studentId) {
        this.studentName = studentName;
        this.studentId = studentId;

        // Load the background image
        try {
            backgroundImage7 = ImageIO.read(new File("src/images/background7.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the JFrame
        setTitle("Student Dashboard");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Custom JPanel with background image
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage7 != null) {
                    g.drawImage(backgroundImage7, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to center-align components

        // Create and style the welcome message
        JLabel nameLabel = new JLabel("Welcome, " + studentName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE); // Set text color to white for visibility on background

        // GridBagConstraints for nameLabel to center-align it
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the label
        gbc.anchor = GridBagConstraints.CENTER; // Center the label in the grid
        contentPanel.add(nameLabel, gbc);

        // Check if student details are already filled
        if (DatabaseManager.isStudentDetailsFilled(studentId)) {
            // If details are filled, show the professor list directly
            JButton goToProfessorsButton = new JButton("Go to Professors List");
            goToProfessorsButton.setPreferredSize(new Dimension(200, 40));
            goToProfessorsButton.setFont(new Font("Arial", Font.BOLD, 14));
            goToProfessorsButton.setBackground(new Color(135, 206, 235)); // Sky blue color
            goToProfessorsButton.setForeground(Color.WHITE);

            goToProfessorsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openProfessorList();
                }
            });

            // Add the button below the nameLabel
            gbc.gridy = 1; // Move to the next row
            contentPanel.add(goToProfessorsButton, gbc);
        } else {
            // If details are not filled, show application form button
            JButton applyButton = new JButton("Fill Application Form");
            applyButton.setPreferredSize(new Dimension(200, 40));
            applyButton.setFont(new Font("Arial", Font.BOLD, 14));
            applyButton.setBackground(new Color(135, 206, 235)); // Sky blue color
            applyButton.setForeground(Color.WHITE);

            applyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Open the ApplicationForm when the button is clicked
                    openApplicationForm();
                }
            });

            // Add the button below the nameLabel
            gbc.gridy = 1; // Move to the next row
            contentPanel.add(applyButton, gbc);
        }

        // Set content panel as the frame's content
        setContentPane(contentPanel);

        // Display the JFrame
        setVisible(true);
    }

    // Method to open the ApplicationForm
    private void openApplicationForm() {
        this.setVisible(false); // Hide the current dashboard

        // Create and display the ApplicationForm for the student
        ApplicationForm applicationForm = new ApplicationForm(studentId);
        JFrame applicationFrame = new JFrame("Application Form");
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationFrame.setSize(600, 400);
        applicationFrame.setLocationRelativeTo(null);
        applicationFrame.add(applicationForm); // Add the ApplicationForm panel to the frame
        applicationFrame.setVisible(true);
    }

    // Method to open the Professor List UI
    private void openProfessorList() {
        ProfessorListUI professorListUI = new ProfessorListUI(studentId);
        professorListUI.setVisible(true);
        dispose(); // Close the current dashboard window
    }

    public static void main(String[] args) {
        new StudentDashboardUI("John Doe", 1);
    }
}
