package auth;
import java.awt.Window;
import javax.swing.*;
import database.DatabaseManager;
import models.User;
import views.*;

public class LoginController {

    public static void verifyLogin(String email, String password,LoginUI loginUI) {
        // Verify user login and fetch the user based on email and password
        User user = DatabaseManager.verifyUserLogin(email, password);

        if (user != null) {
            JOptionPane.showMessageDialog(null, "Login Successful!");



            // Transition to the user's dashboard based on their role
            switch (user.getRole().toLowerCase()) {
                case "student":
                    // Open Student Dashboard
                    disposeLoginWindow(loginUI);
                    new StudentDashboardUI(user.getName(),user.getId()); // Make sure to create this class

                    break;

                case "professor":
                    // Open Professor Dashboard
                    disposeLoginWindow(loginUI);
                    new ProfessorDashboardUI(user.getName(), user.getId()); // Make sure to create this class
                    break;



                default:
                    JOptionPane.showMessageDialog(null, "Invalid role.");
                    break;
            }

        } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password.");
        }
    }
    private static void disposeLoginWindow(LoginUI loginUI) {
        Window window = SwingUtilities.windowForComponent(loginUI);
        if (window != null) {
            window.dispose();
        }
    }
}
