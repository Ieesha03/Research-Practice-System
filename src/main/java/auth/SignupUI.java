package auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SignupUI extends JPanel {  // Changed from JFrame to JPanel
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton signupButton;
    private JLabel loginLink;
    private Image backgroundImage, backgroundImage2 ;

    public SignupUI() {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            backgroundImage2 = ImageIO.read(new File("src/images/background2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Set layout manager and allow the components to flow naturally
        setLayout(new BorderLayout());

        // Title label at the top
        JLabel titleLabel = new JLabel("Create an Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Components for the form

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE); // Set label font color to white
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(250, 30));
        nameField.setBackground(new Color(0, 0, 0, 80)); // Optional: Set background color of text field
        nameField.setForeground(Color.WHITE); // Set text color to white

        JLabel emailLabel = new JLabel("BITS Email:");
        emailLabel.setForeground(Color.WHITE); // Set label font color to white
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(250, 30));
        emailField.setBackground(new Color(0, 0, 0, 80)); // Optional: Set background color of text field
        emailField.setForeground(Color.WHITE); // Set text color to white

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // Set label font color to white
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setBackground(new Color(0, 0, 0, 80)); // Optional: Set background color of password field
        passwordField.setForeground(Color.WHITE); // Set text color to white

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(Color.WHITE);
        roleComboBox = new JComboBox<>(new String[]{"Student", "Professor"});
        roleComboBox.setPreferredSize(new Dimension(180, 30)); // Adjust size of combo box

        // Buttons with colors
        signupButton = new JButton("Sign Up");

        signupButton.setBackground(new Color(52, 152, 219)); // Blue color
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 14));
        signupButton.setPreferredSize(new Dimension(200, 40)); // Set a custom size for buttons

        // "Already have an account?" label as clickable link
        loginLink = new JLabel("<HTML><U>Already have an account? Login here</U></HTML>");
        loginLink.setFont(new Font("Arial", Font.PLAIN, 12));
        loginLink.setForeground(new Color(46, 204, 113)); // Green color
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor changes to a hand on hover

        // Layout manager (GridBagLayout for precise control)
        // formPanel definition
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw image
                }
            }
        };

// You can set other properties of formPanel (size, layout, etc.)
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components

        // Title label at the top (full width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Name label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Role label and combo box
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(roleComboBox, gbc);

        // Add formPanel to the center of the SignupUI
        add(formPanel, BorderLayout.CENTER);

        // Create a panel for the signup button and login link with BoxLayout
// Create bottomPanel with custom background image
        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage2 != null) {
                    g.drawImage(backgroundImage2, 0, 0, getWidth(), getHeight(), this); // Draw the image
                }
            }
        };

// Set BoxLayout to stack components vertically
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

// Create a new panel to center the components inside the bottomPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align components inside this panel
        centerPanel.setOpaque(false); // Make the background transparent

// Add components (signupButton and loginLink) to the centerPanel
        centerPanel.add(signupButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 60))); // Space between button and link
        centerPanel.add(loginLink); // Add Login link below the button

// Add the centerPanel to the bottomPanel (which already has BoxLayout)
        bottomPanel.add(centerPanel);

// Add bottomPanel to the bottom of the SignupUI
        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = roleComboBox.getSelectedItem().toString();

                SignupController.registerUser(name, email, password, role);
            }
        });

        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Close the sign-up page and open the login page in a new JFrame
                Window window = SwingUtilities.windowForComponent(SignupUI.this);
                window.dispose(); // Close current window
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(600, 500);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.add(new LoginUI()); // Add LoginUI panel to the frame
                loginFrame.setVisible(true); // Make the login window visible
            }
        });
    }

    // Override paintComponent to add the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (backgroundImage2 != null) {
            // Draw the background image
            g.drawImage(backgroundImage2, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Override getPreferredSize to ensure correct layout
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }


}
