import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Login extends JFrame {
    private JFrame frame;
    private JPanel titlePanel, loginPanel;
    private JLabel titlemain, emailLabel, passwordLabel, accLabel;
    private JTextField emailField;
    private JPasswordField passwordText;
    private JButton loginButton, signupButton;

    public Login() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        // Styling title
        titlemain = new JLabel("Airline Reservation System", JLabel.CENTER);
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
        emailLabel = new JLabel("Email: ");
        passwordLabel = new JLabel("Password: ");
        accLabel = new JLabel("Don't have an account? ");
        emailField = new JTextField(20);
        passwordText = new JPasswordField(20);
        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");

        // Apply styles
        styleButton(loginButton);
        styleButton(signupButton);
        styleLabel(emailLabel);
        styleLabel(passwordLabel);
        styleLabel(accLabel);

        // Login panel setup
        loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(240, 248, 255));

        emailLabel.setBounds(50, 20, 100, 30);
        emailField.setBounds(200, 20, 150, 30);
        passwordLabel.setBounds(50, 60, 100, 30);
        passwordText.setBounds(200, 60, 150, 30);
        loginButton.setBounds(200, 100, 150, 30);
        accLabel.setBounds(200, 140, 250, 30);
        signupButton.setBounds(200, 180, 150, 30);

        // Add components to login panel
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        loginPanel.add(loginButton);
        loginPanel.add(accLabel);
        loginPanel.add(signupButton);

        // Add panels to frame
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(loginPanel, BorderLayout.CENTER);

        // Button actions
        loginButton.addActionListener(new LoginAction());
        signupButton.addActionListener(e -> {
            frame.dispose();
            new Signup();
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

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordText.getPassword());

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format!");
                return;
            }

            MongoCollection<Document> usersCollection = MongoDB.connect().getCollection("users");
            Document user = usersCollection.find(new Document("email", email)).first();

            if (user == null || !user.getString("password").equals(password)) {
                JOptionPane.showMessageDialog(frame, "Invalid email or password!");
            } else {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                frame.dispose();
                new Main(); // Replace this with your main app class
            }
        }

        private boolean isValidEmail(String email) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            return email.matches(emailRegex);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
