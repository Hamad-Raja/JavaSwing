import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Admin extends JPanel {
    private JTextField departureField, destinationField, dateField, timeField;
    private JComboBox<String> flightClassComboBox;
    private JButton addFlightButton;

    public Admin() {
        setLayout(null); // Use absolute layout for positioning
        setBackground(new Color(240, 248, 255)); // Light background color

        JLabel heading = createHeading("Add Flight");
        heading.setBounds(0, 10, 800, 50); // Adjust based on panel width
        add(heading);

        // Labels and Input Fields
        JLabel departureLabel = createLabel("Departure:");
        departureLabel.setBounds(200, 80, 100, 25);
        add(departureLabel);

        departureField = createTextField();
        departureField.setBounds(320, 80, 200, 25);
        add(departureField);

        JLabel destinationLabel = createLabel("Destination:");
        destinationLabel.setBounds(200, 120, 100, 25);
        add(destinationLabel);

        destinationField = createTextField();
        destinationField.setBounds(320, 120, 200, 25);
        add(destinationField);

        JLabel flightClassLabel = createLabel("Flight Class:");
        flightClassLabel.setBounds(200, 160, 100, 25);
        add(flightClassLabel);

        flightClassComboBox = new JComboBox<>(new String[]{"Economy", "Business", "First Class"});
        styleComboBox(flightClassComboBox);
        flightClassComboBox.setBounds(320, 160, 200, 25);
        add(flightClassComboBox);

        JLabel dateLabel = createLabel("Date (MM/dd/yyyy):");
        dateLabel.setBounds(200, 200, 150, 25);
        add(dateLabel);

        dateField = createTextField();
        dateField.setBounds(370, 200, 150, 25);
        add(dateField);

        JLabel timeLabel = createLabel("Time (HH:mm):");
        timeLabel.setBounds(200, 240, 100, 25);
        add(timeLabel);

        timeField = createTextField();
        timeField.setBounds(320, 240, 200, 25);
        add(timeField);

        // Add Flight Button
        addFlightButton = createButton("Add Flight");
        addFlightButton.setBounds(350, 300, 150, 30);
        add(addFlightButton);

        // Add Flight Button Action
        addFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddFlight();
            }
        });
    }

    // Helper Method: Create Heading
    private JLabel createHeading(String text) {
        JLabel heading = new JLabel(text, JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(30, 144, 255)); // Dodger Blue
        return heading;
    }

    // Helper Method: Create Labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    // Helper Method: Create Text Fields
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        return textField;
    }

    // Helper Method: Create Buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(30, 44, 155)); // Dodger Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Helper Method: Style ComboBox
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }

    // Handle Add Flight Action
    private void handleAddFlight() {
        String departure = departureField.getText().trim();
        String destination = destinationField.getText().trim();
        String flightClass = (String) flightClassComboBox.getSelectedItem();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        if (departure.isEmpty() || destination.isEmpty() || flightClass.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (departure.equalsIgnoreCase(destination)) {
            JOptionPane.showMessageDialog(this, "Departure and destination cannot be the same!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use MM/dd/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!time.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid time format! Use HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            MongoCollection<Document> reservationsCollection = MongoDB.connect().getCollection("reservations");
            Document newReservation = new Document("departure", departure)
                    .append("destination", destination)
                    .append("class", flightClass)
                    .append("date", date)
                    .append("time", time);

            reservationsCollection.insertOne(newReservation);

            JOptionPane.showMessageDialog(this, "Flight added successfully!");

            departureField.setText("");
            destinationField.setText("");
            dateField.setText("");
            timeField.setText("");
            flightClassComboBox.setSelectedIndex(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving flight: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
