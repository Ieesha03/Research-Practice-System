package auth;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LoginUI extends JPanel {  // Changed from JFrame to JPanel
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel signupLink;
    private Image backgroundImage3, backgroundImage4 ;

    public LoginUI() {

        try {
            backgroundImage3 = ImageIO.read(new File("src/images/background3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            backgroundImage4 = ImageIO.read(new File("src/images/background4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Set layout and background color for the panel (light purple background)
        setLayout(new BorderLayout());


        // Title label at the top
        JLabel titleLabel = new JLabel("Welcome to Login!!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Components for the form
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

        // Buttons with colors
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(52, 152, 219)); // Blue color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 40)); // Set a custom size for buttons

        // "Don't have an account?" label as clickable link
        signupLink = new JLabel("<HTML><U>Don't have an account? Sign up here</U></HTML>");
        signupLink.setFont(new Font("Arial", Font.PLAIN, 12));
        signupLink.setForeground(new Color(46, 204, 113)); // Green color
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor changes to a hand on hover

        // Layout manager (GridBagLayout for precise control)
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage3 != null) {
                    g.drawImage(backgroundImage3, 0, 0, getWidth(), getHeight(), this); // Draw image
                }
            }
        };


        formPanel.setLayout(new GridBagLayout()); // Use GridBagLayout to control placement


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components

        // Title label at the top (full width)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Add formPanel to the center of the LoginUI
        add(formPanel, BorderLayout.CENTER);


        // Create bottomPanel with a custom background image
        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage4 != null) {
                    g.drawImage(backgroundImage4, 0, 0, getWidth(), getHeight(), this); // Draw the image
                }
            }
        };

// Set BoxLayout to stack components vertically
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

// Create a new panel to center the components inside the bottomPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center-align components inside this panel
        centerPanel.setOpaque(false); // Make the background transparent

// Center-align loginButton and signupLink inside the centerPanel
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupLink.setAlignmentX(Component.CENTER_ALIGNMENT);

// Add components (loginButton and signupLink) to the centerPanel
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 70))); // Space between button and link
        centerPanel.add(signupLink); // Add Signup link below the button

// Add the centerPanel to the bottomPanel (which already has BoxLayout)
        bottomPanel.add(centerPanel);

// Add bottomPanel to the main UI container, at the bottom
        add(bottomPanel, BorderLayout.SOUTH);


        // Action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                LoginController.verifyLogin(email,password,LoginUI.this);
            }
        });

        signupLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Close the login page and open the sign-up page in a new JFrame
                Window window = SwingUtilities.windowForComponent(LoginUI.this);
                window.dispose(); // Close current window
                JFrame signupFrame = new JFrame("Sign Up");
                signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                signupFrame.setSize(600, 500); // Adjust size as needed
                signupFrame.setLocationRelativeTo(null);
                signupFrame.add(new SignupUI()); // Add SignupUI panel to the frame
                signupFrame.setVisible(true); // Make the signup window visible
            }
        });
    }

    // Override paintComponent to add the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage3 != null) {
            // Draw the background image
            g.drawImage(backgroundImage3, 0, 0, getWidth(), getHeight(), this);
        }
        if (backgroundImage4 != null) {
            // Draw the background image
            g.drawImage(backgroundImage4, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Override getPreferredSize to ensure correct layout
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }


}

