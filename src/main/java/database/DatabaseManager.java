package database;
import models.User;
import java.sql.*;
import models.Application;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/research_practice";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "ieesha03";

    // Establish database connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        if (conn != null) {
            System.out.println("Successfully connected to the database!");
        }
        return conn;
    }

    // Register a new user based on role
    public static boolean registerUser(String name, String email, String password, String role) {
        if (checkEmailExists(email)) {
            System.out.println("Email already exists in the database.");
            return false; // Return false if email is already registered
        }

        switch (role.toLowerCase()) {
            case "student":
                return registerStudent(name, email, password);
            case "professor":
                return registerProfessor(name, email, password);
            case "admin":
                return registerAdmin(name, email, password);
            default:
                System.out.println("Invalid role specified.");
                return false;
        }
    }

    // Register a student
    private static boolean registerStudent(String name, String email, String password) {
        String query = "INSERT INTO students (name, email, password_hash) VALUES (?, ?, ?)";
        return executeRegistrationQuery(query, name, email, password);
    }

    // Register a professor
    private static boolean registerProfessor(String name, String email, String password) {
        String query = "INSERT INTO professors (name, email, password_hash) VALUES (?, ?, ?)";
        return executeRegistrationQuery(query, name, email, password);
    }

    // Register an admin
    private static boolean registerAdmin(String name, String email, String password) {
        String query = "INSERT INTO admins (name, email, password_hash) VALUES (?, ?, ?)";
        return executeRegistrationQuery(query, name, email, password);
    }

    // Executes the insert query for registration
    private static boolean executeRegistrationQuery(String query, String name, String email, String password) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Registration successful if at least one row is inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if an email is already registered in any of the tables
    public static boolean checkEmailExists(String email) {
        String studentQuery = "SELECT * FROM students WHERE email = ?";
        String professorQuery = "SELECT * FROM professors WHERE email = ?";
        String adminQuery = "SELECT * FROM admins WHERE email = ?";

        try (Connection conn = getConnection()) {
            if (checkEmailInTable(conn, studentQuery, email)) return true;
            if (checkEmailInTable(conn, professorQuery, email)) return true;
            if (checkEmailInTable(conn, adminQuery, email)) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper function to check if an email exists in a specified table
    private static boolean checkEmailInTable(Connection conn, String query, String email) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Verify user login credentials and identify role
    public static User verifyUserLogin(String email, String password) {
        User user;
        System.out.println("****");
        if ((user = getUserFromTable("students", email, password)) != null){
            System.out.println(user);
            return user;
        }
        if ((user = getUserFromTable("professors", email, password)) != null) return user;
        if ((user = getUserFromTable("admins", email, password)) != null) return user;
        System.out.println(user);
        System.out.println("Login failed. User not found.");
        return null;
    }

    // Method to check if student details are already filled
    public static boolean isStudentDetailsFilled(int studentId) {
        String sql = "SELECT * FROM applications WHERE student_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a row exists, details are already filled
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error in database operation
        }
    }

    public static void submitApplication(int studentId, String areaOfInterest,
                                         String skills, String publications, String experience) {
        String sql = "INSERT INTO applications (student_id, area_of_interest, skills, " +
                "research_publications, prior_research_experience, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the values for the PreparedStatement
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, areaOfInterest);
            preparedStatement.setString(3, skills);
            preparedStatement.setString(4, publications);
            preparedStatement.setString(5, experience);
            preparedStatement.setString(6, "Rejected"); // Default status for new applications

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Application submitted successfully!");
            } else {
                System.out.println("Application submission failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve user from specified table
    private static User getUserFromTable(String tableName, String email, String password) {
        System.out.println("calling table");
        String query = String.format("SELECT * FROM %s WHERE email = ? AND password_hash = ?", tableName);
        System.out.println(query);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Debugging: print the values of the columns
                System.out.println("User found:");
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String userEmail = rs.getString("email");
                String userPassword = rs.getString("password_hash");
                String role = "";
                if ("students".equals(tableName)) {
                    role = "student";
                } else if ("professors".equals(tableName)) {
                    role = "professor";
                } else if ("admins".equals(tableName)) {
                    role = "admin";
                }
                // Create a new User object with the retrieved data
                return new User( id, name, userEmail, userPassword,role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void apply(int studentId, int professorId) {
        // SQL query to update professor_id for the specific student's application
        String sql = "UPDATE applications SET professor_id = ? WHERE student_id = ? AND professor_id IS NULL";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the professor_id and student_id in the prepared statement
            preparedStatement.setInt(1, professorId);
            preparedStatement.setInt(2, studentId);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Application updated successfully.");
            } else {
                System.out.println("No matching application found for student with ID: " + studentId + " or the professor is already assigned.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void saveProfessorDetails(int professorId, String areaOfWork, String preferredSkills) {
        String sql = "UPDATE professors SET area_of_work = ?, preferred_skills = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, areaOfWork);
            preparedStatement.setString(2, preferredSkills);
            preparedStatement.setInt(3, professorId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Professor details updated successfully.");
            } else {
                System.out.println("No professor found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to check if the professor details are filled
    public static boolean areProfessorDetailsFilled(int professorId) {
        String sql = "SELECT area_of_work, preferred_skills FROM professors WHERE id = ?";
        boolean isFilled = false;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, professorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String areaOfWork = resultSet.getString("area_of_work");
                    String preferredSkills = resultSet.getString("preferred_skills");

                    // Check if both fields are not null and not empty
                    if (areaOfWork != null && !areaOfWork.isEmpty() &&
                            preferredSkills != null && !preferredSkills.isEmpty()) {
                        isFilled = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(isFilled){
            System.out.println("Already Filled");
        }
        else{
            System.out.println("Not Filled Yet");
        }
        return isFilled;
    }
    public static List<Application> getApplicationsForProfessor(int professorId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT a.id, a.student_id, a.professor_id, s.name AS student_name, a.area_of_interest, a.skills, a.research_publications, a.prior_research_experience " +
                "FROM applications a " +
                "JOIN students s ON a.student_id = s.id " +
                "WHERE a.professor_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, professorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Application application = new Application(
                            resultSet.getInt("id"),
                            resultSet.getInt("student_id"),
                            resultSet.getInt("professor_id"),
                            resultSet.getString("student_name"),
                            resultSet.getString("area_of_interest"),
                            resultSet.getString("skills"),
                            resultSet.getString("research_publications"),
                            resultSet.getString("prior_research_experience")
                    );
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    public static void updateApplicationStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, status); // Set status to "Accepted" or "Rejected"
            preparedStatement.setInt(2, applicationId); // Specify the application ID

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Application status updated successfully.");
            } else {
                System.out.println("Application not found or status unchanged.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}