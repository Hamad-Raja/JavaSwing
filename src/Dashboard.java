import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private JPanel leftPanel, rightPanel;
    private CardLayout cardLayout;

    public Dashboard() {
        setTitle("Dashboard - Airline Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setLayout(new BorderLayout());

        // Left Navigation Panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
        leftPanel.setPreferredSize(new Dimension(200, 0)); // Adjust panel width
        stylePanel(leftPanel, new Color(70, 130, 180)); // Apply styling

        JButton addFlightButton = createNavButton("Add Flight");
        JButton flightDetailsButton = createNavButton("Flight Details");
        JButton viewReservationsButton = createNavButton("Booking Detail");
        JButton logoutButton = createNavButton("Logout");

        // Add buttons to the left panel with spacing
        leftPanel.add(Box.createVerticalStrut(20)); // Add top spacing
        leftPanel.add(addFlightButton);
        leftPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        leftPanel.add(flightDetailsButton);
        leftPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        leftPanel.add(viewReservationsButton);
        leftPanel.add(Box.createVerticalStrut(15)); // Spacing between buttons
        leftPanel.add(logoutButton);
        leftPanel.add(Box.createVerticalStrut(20)); // Add bottom spacing

        // Right Content Panel with CardLayout
        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        // Add panels to CardLayout
        rightPanel.add(new Admin(), "AddFlight"); // Updated Admin panel
        rightPanel.add(new flightView(), "FlightDetails"); // Updated FlightDetails class
        rightPanel.add(new ViewReservation(), "ViewReservations"); // Updated ViewReservation class

        // Button Actions
        addFlightButton.addActionListener(e -> cardLayout.show(rightPanel, "AddFlight"));
        flightDetailsButton.addActionListener(e -> cardLayout.show(rightPanel, "FlightDetails"));
        viewReservationsButton.addActionListener(e -> cardLayout.show(rightPanel, "ViewReservations"));
        logoutButton.addActionListener(e -> {
            dispose();
            new Login(); // Redirect to the Login screen
        });

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Helper Method: Create Navigation Buttons
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font size
        button.setBackground(new Color(30, 144, 255)); // Dodger Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        BorderFactory.createEmptyBorder(10, 15 ,10, 15) ;// Smaller padding

        button.setPreferredSize(new Dimension(180, 35)); // Adjust button size
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Helper Method: Style Panels
    private void stylePanel(JPanel panel, Color backgroundColor) {
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adjust panel padding
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard());
    }
}
