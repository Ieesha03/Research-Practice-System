package views;

import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ProfessorDashboardUI extends JFrame {
    private String professorName;
    private int professorId;
    private Image backgroundImage10; // To hold the background image

    public ProfessorDashboardUI(String professorName, int professorId) {
        this.professorName = professorName;
        this.professorId = professorId;

        // Load the background image
        try {
            backgroundImage10 = ImageIO.read(new File("src/images/background10.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("Professor Dashboard");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Custom JPanel with background image
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage10 != null) {
                    g.drawImage(backgroundImage10, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to center-align components

        // GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Display professor's name at the top
        JLabel nameLabel = new JLabel("Welcome, Professor " + professorName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE); // Set text color to white for visibility on the background
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        contentPanel.add(nameLabel, gbc);

        // Check if professor details are filled and show corresponding button
        boolean detailsFilled = DatabaseManager.areProfessorDetailsFilled(professorId);
        if (detailsFilled) {
            setupViewApplicationsButton(contentPanel, gbc);
        } else {
            setupFillDetailsButton(contentPanel, gbc);
        }

        // Set content panel as the frame's content
        setContentPane(contentPanel);
        setVisible(true);
    }

    private void setupFillDetailsButton(JPanel contentPanel, GridBagConstraints gbc) {
        // "Fill Details" button
        JButton fillDetailsButton = new JButton("Fill Details");
        fillDetailsButton.setPreferredSize(new Dimension(200, 40));
        fillDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        fillDetailsButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        fillDetailsButton.setForeground(Color.WHITE);
        fillDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDetailsForm();
            }
        });

        // Add the button below the nameLabel
        gbc.gridy = 1;
        contentPanel.add(fillDetailsButton, gbc);
    }

    private void setupViewApplicationsButton(JPanel contentPanel, GridBagConstraints gbc) {
        // "View Applications" button
        JButton viewApplicationsButton = new JButton("View Applications");
        viewApplicationsButton.setPreferredSize(new Dimension(200, 40));
        viewApplicationsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewApplicationsButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        viewApplicationsButton.setForeground(Color.WHITE);
        viewApplicationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReceivedApplicationsPage();
            }
        });

        // Add the button below the nameLabel
        gbc.gridy = 1;
        contentPanel.add(viewApplicationsButton, gbc);
    }

    private void openDetailsForm() {
        // Open the Professor Details Form, passing professorId
        ProfessorDetailsForm detailsForm = new ProfessorDetailsForm(professorId);
        detailsForm.setVisible(true);
        dispose(); // Close current window when form opens
    }

    private void openReceivedApplicationsPage() {
        // Open the Received Applications page, passing professorId
        ReceivedApplicationsUI receivedApplicationsUI = new ReceivedApplicationsUI(professorId);
        receivedApplicationsUI.setVisible(true);
        dispose(); // Close current window when applications page opens
    }

    public static void main(String[] args) {
        new ProfessorDashboardUI("Dr. John Smith", 101);
    }
}
