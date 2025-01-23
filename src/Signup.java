import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Signup extends JFrame {
    private JFrame frame;
    private JPanel titlePanel, signupPanel;
    private JLabel titlemain, usernameLabel, emailLabel, passwordLabel, loginMessage;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JButton signupButton, loginButton;

    public Signup() {
        frame = new JFrame("Signup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        // Styling title
        titlemain = new JLabel("Signup for Airline Reservation", JLabel.CENTER);
        titlemain.setFont(new Font("Arial", Font.BOLD, 24));
        titlemain.setForeground(Color.WHITE);
        titlemain.setOpaque(true);
        titlemain.setBackground(new Color(70, 130, 180));
        titlemain.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Title panel
        titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titlemain, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(400, 60));

        // Input and button setup
        usernameLabel = new JLabel("Username: ");
        emailLabel = new JLabel("Email: ");
        passwordLabel = new JLabel("Password: ");
        loginMessage = new JLabel("Already have an account? Login below:", JLabel.CENTER);
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        signupButton = new JButton("Signup");
        loginButton = new JButton("Login");

        // Apply styles
        styleButton(signupButton);
        styleButton(loginButton);
        styleLabel(usernameLabel);
        styleLabel(emailLabel);
        styleLabel(passwordLabel);
        styleLabel(loginMessage);

        // Signup panel setup
        signupPanel = new JPanel(null);
        signupPanel.setBackground(new Color(240, 248, 255));

        usernameLabel.setBounds(50, 20, 100, 30);
        usernameField.setBounds(200, 20, 150, 30);
        emailLabel.setBounds(50, 60, 100, 30);
        emailField.setBounds(200, 60, 150, 30);
        passwordLabel.setBounds(50, 100, 100, 30);
        passwordField.setBounds(200, 100, 150, 30);
        signupButton.setBounds(200, 150, 150, 30);
        loginMessage.setBounds(50, 200, 500, 30);
        loginButton.setBounds(200, 240, 150, 30);

        // Add components to signup panel
        signupPanel.add(usernameLabel);
        signupPanel.add(usernameField);
        signupPanel.add(emailLabel);
        signupPanel.add(emailField);
        signupPanel.add(passwordLabel);
        signupPanel.add(passwordField);
        signupPanel.add(signupButton);
        signupPanel.add(loginMessage);
        signupPanel.add(loginButton);

        // Add panels to frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(signupPanel, BorderLayout.CENTER);

        // Button actions
        signupButton.addActionListener(new SignupAction());
        loginButton.addActionListener(e -> {
            frame.dispose();
            new Login();
        });

        frame.setVisible(true);
    }

    // Button styling helper
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Label styling helper
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.DARK_GRAY);
    }

    private class SignupAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required!");
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format!");
                return;
            }

            MongoCollection<Document> usersCollection = MongoDB.connect().getCollection("users");
            Document existingUser = usersCollection.find(new Document("email", email)).first();

            if (existingUser != null) {
                JOptionPane.showMessageDialog(frame, "Email already exists!");
            } else {
                usersCollection.insertOne(new Document("username", username).append("email", email).append("password", password));
                JOptionPane.showMessageDialog(frame, "Signup successful!");
                frame.dispose();
                new Login();
            }
        }

        private boolean isValidEmail(String email) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            return email.matches(emailRegex);
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
