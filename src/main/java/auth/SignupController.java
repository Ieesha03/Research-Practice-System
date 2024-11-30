package auth;

import javax.swing.*;
import database.DatabaseManager;
import models.User;

public class SignupController {

    public static void registerUser(String name, String email, String password, String role) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return;
        }

        if (!isBitsEmail(email)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid BITS email address.");
            return;
        }

        if (DatabaseManager.checkEmailExists(email)) {
            JOptionPane.showMessageDialog(null, "Email already registered.");
            return;
        }

        User newUser = new User(name, email, password, role);
        if (DatabaseManager.registerUser(name, email, password, role)) {
            JOptionPane.showMessageDialog(null, "Registration Successful. Please log in.");
            new LoginUI();
        } else {
            JOptionPane.showMessageDialog(null, "Registration Failed.");
        }
    }

    private static boolean isBitsEmail(String email) {
        return email.endsWith("@pilani.bits-pilani.ac.in");
    }
}
