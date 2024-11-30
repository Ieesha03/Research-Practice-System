import auth.SignupUI;

import javax.swing.*;

public class AppMain {

    public static void main(String[] args) {
        // Create the frame for the login UI
        JFrame frame = new JFrame("Research Practice System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        // Add the Signup UI to the frame
        SignupUI signupUI = new SignupUI();  // No need to use new SignupUI() with JFrame anymore

        // Add the Signup panel to the frame
        frame.add(signupUI);

        // Set the frame visible
        frame.setVisible(true);
    }
}
