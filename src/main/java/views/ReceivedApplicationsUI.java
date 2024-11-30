package views;

import database.DatabaseManager;
import models.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReceivedApplicationsUI extends JFrame {

    private int professorId;
    private Image backgroundImage;

    public ReceivedApplicationsUI(int professorId) {
        this.professorId = professorId;

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/images/background9.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Received Student Applications");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a custom JPanel to display the background image
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add space at the top of the panel
        panel.add(Box.createVerticalStrut(50));

        // Fetch applications from the database
        List<Application> applications = DatabaseManager.getApplicationsForProfessor(professorId);

        // Display each application with Accept/Reject buttons
        for (Application application : applications) {
            JPanel applicationPanel = new JPanel();
            applicationPanel.setLayout(new BoxLayout(applicationPanel, BoxLayout.Y_AXIS)); // Use vertical alignment
            applicationPanel.setOpaque(false); // Ensure transparency for the background image

            JLabel studentInfo = new JLabel("Student: " + application.getStudentName() +
                    " - Area of Interest: " + application.getAreaOfInterest());
            studentInfo.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the label
            applicationPanel.add(studentInfo);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align the buttons
            buttonPanel.setOpaque(false); // Ensure transparency for the background image

            JButton acceptButton = new JButton("Accept");
            acceptButton.addActionListener(e -> handleApplicationAction(application.getId(), "Accepted"));
            buttonPanel.add(acceptButton);

            JButton rejectButton = new JButton("Reject");
            rejectButton.addActionListener(e -> handleApplicationAction(application.getId(), "Rejected"));
            buttonPanel.add(rejectButton);

            applicationPanel.add(buttonPanel);

            panel.add(applicationPanel);

            // Reduce spacing between two application panels
            panel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Custom JPanel class to draw the background image
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Handle accept/reject actions
    private void handleApplicationAction(int applicationId, String status) {
        DatabaseManager.updateApplicationStatus(applicationId, status);
        JOptionPane.showMessageDialog(this, "Application " + status.toLowerCase() + " successfully.");
        refreshApplications(); // Optionally refresh the applications list
    }

    private void refreshApplications() {
        dispose();
        new ReceivedApplicationsUI(professorId).setVisible(true);
    }
}
