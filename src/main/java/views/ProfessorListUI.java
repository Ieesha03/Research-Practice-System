package views;

import auth.LoginUI;
import database.DatabaseManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ProfessorListUI extends JPanel {

    private int studentId;
    private Image backgroundImage;

    public ProfessorListUI(int studentId) {
        this.studentId = studentId;

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/images/background13.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        // Create a custom JPanel to display the background image
        JPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add space at the top of the panel
        panel.add(Box.createVerticalStrut(140));

            // Check application status and display relevant information
            String applicationStatus = hasAlreadyApplied(studentId);
            if ("pending".equals(applicationStatus)) {
                JLabel messageLabel = new JLabel("You have a pending application for research practice.", JLabel.CENTER);
                messageLabel.setForeground(Color.RED);
                messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
                messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the message
                panel.add(messageLabel);
            } else if ("rejected".equals(applicationStatus)) {
                displayProfessorList(panel);
            } else {
                JOptionPane.showMessageDialog(this, "Application accepted or no professors available.");
            return;
        }

        // Set up scrollable panel for long lists
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Displays the list of professors with "Apply" button below their details
    // Displays the list of professors with "Apply" button below their details
    private void displayProfessorList(JPanel panel) {
        JPanel professorsContainer = new JPanel();
        professorsContainer.setLayout(new GridLayout(0, 3, 10, 10)); // Grid layout: 3 columns, dynamic rows, spacing between cells
        professorsContainer.setOpaque(false); // Ensure transparency for background

        ArrayList<String> professors = getProfessors();
        for (String professor : professors) {
            JPanel professorPanel = new JPanel();
            professorPanel.setLayout(new BoxLayout(professorPanel, BoxLayout.Y_AXIS)); // Vertical arrangement within each professor's panel
            professorPanel.setOpaque(false); // Ensure transparency

            JLabel professorLabel = new JLabel("<html>" + professor.replace("\n", "<br>") + "</html>");
            professorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            professorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the label
            professorPanel.add(professorLabel);

            JButton applyButton = new JButton("Apply");
            applyButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the button
            applyButton.addActionListener(e -> applyForResearch(professor));
            professorPanel.add(Box.createVerticalStrut(10)); // Add spacing between label and button
            professorPanel.add(applyButton);

            professorPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // Add padding around each professor's panel
            professorsContainer.add(professorPanel);
        }

        panel.add(professorsContainer);
    }


    // Checks application status based on the student's ID
    private String hasAlreadyApplied(int studentId) {
        String sql = "SELECT a.status, s.applied, a.id, a.professor_id FROM students s " +
                "JOIN applications a ON s.id = a.student_id WHERE s.id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");

                if ("rejected".equalsIgnoreCase(status)) {
                    updateStudentAppliedStatus(studentId);
                    return "rejected";
                } else if ("accepted".equalsIgnoreCase(status)) {
                    showAcceptanceMessage(resultSet.getInt("professor_id"));
                    return "accepted";
                } else if ("pending".equalsIgnoreCase(status)) {
                    return "pending";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "none";
    }

    // Displays a message if the application has been accepted
    private void showAcceptanceMessage(int professorId) {
        String professorName = getProfessorNameById(professorId);
        JOptionPane.showMessageDialog(this, "Congratulations " + getStudentName(studentId) +
                ", your application has been accepted under " + professorName);
    }

    // Fetches the student's name by ID
    private String getStudentName(int studentId) {
        String sql = "SELECT name FROM students WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Student";
    }

    // Retrieves list of professors from the database
    private ArrayList<String> getProfessors() {
        ArrayList<String> professors = new ArrayList<>();
        String sql = "SELECT id, name, area_of_work FROM professors";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String professorInfo = resultSet.getString("name") + "\n" +
                        resultSet.getString("area_of_work") + "\n" ;
                professors.add(professorInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professors;
    }

    // Handles application logic for a professor when the "Apply" button is clicked
    private void applyForResearch(String professor) {
        String[] professorDetails = professor.split("\n");
        int professorId = Integer.parseInt(professorDetails[2]);

        if (canProfessorAcceptMoreStudents(professorId)) {
            DatabaseManager.apply(studentId, professorId);
            updateStudentAppliedStatus(studentId);
            JOptionPane.showMessageDialog(this, "Application submitted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "This professor has already accepted 3 students.");
        }
    }

    // Fetches professor name by ID for display
    private String getProfessorNameById(int professorId) {
        String sql = "SELECT name FROM professors WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, professorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Professor";
    }

    // Checks if the professor can accept more students
    private boolean canProfessorAcceptMoreStudents(int professorId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE professor_id = ? AND status = 'accepted'";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, professorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) < 3;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Updates the "applied" status for the student in the database
    private void updateStudentAppliedStatus(int studentId) {
        String sql = "UPDATE students SET applied = FALSE WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
