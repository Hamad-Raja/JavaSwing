import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Booking {
    private JFrame frame;
    private JPanel panel;
    private JTextField nameField, phoneNoField, passportField;

    public Booking(String departure, String destination, String date, String flightClass, String flightTime) {
        frame = new JFrame("Booking System");
        panel = new JPanel();
        panel.setLayout(null);

        JLabel departureLabel = new JLabel("Departure: " + departure);
        departureLabel.setBounds(20, 20, 300, 25);
        panel.add(departureLabel);

        JLabel destinationLabel = new JLabel("Destination: " + destination);
        destinationLabel.setBounds(20, 50, 300, 25);
        panel.add(destinationLabel);

        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setBounds(20, 80, 300, 25);
        panel.add(dateLabel);

        JLabel classLabel = new JLabel("Class: " + flightClass);
        classLabel.setBounds(20, 110, 300, 25);
        panel.add(classLabel);

        JLabel timeLabel = new JLabel("Time: " + flightTime);
        timeLabel.setBounds(20, 140, 300, 25);
        panel.add(timeLabel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 180, 100, 25);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 180, 200, 25);
        panel.add(nameField);

        JLabel phoneNoLabel = new JLabel("Phone No:");
        phoneNoLabel.setBounds(20, 220, 100, 25);
        panel.add(phoneNoLabel);

        phoneNoField = new JTextField();
        phoneNoField.setBounds(120, 220, 200, 25);
        panel.add(phoneNoField);

        JLabel passportLabel = new JLabel("Passport No:");
        passportLabel.setBounds(20, 260, 100, 25);
        panel.add(passportLabel);

        passportField = new JTextField();
        passportField.setBounds(120, 260, 200, 25);
        panel.add(passportField);

        JButton bookButton = new JButton("Book");
        bookButton.setBounds(120, 300, 100, 30);
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    saveToDatabase(departure, destination, date, flightClass, flightTime);
                    JOptionPane.showMessageDialog(frame, "Successfully Booked!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(bookButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(240, 300, 100, 30);
        cancelButton.addActionListener(e -> frame.dispose());
        panel.add(cancelButton);

        frame.add(panel);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private boolean validateFields() {
        return !nameField.getText().trim().isEmpty() &&
                !phoneNoField.getText().trim().isEmpty() &&
                !passportField.getText().trim().isEmpty();
    }

    private void saveToDatabase(String departure, String destination, String date, String flightClass, String flightTime) {
        MongoCollection<Document> bookingCollection = MongoDB.connect().getCollection("booking");
        Document bookingData = new Document("departure", departure)
                .append("destination", destination)
                .append("date", date)
                .append("class", flightClass)
                .append("time", flightTime)
                .append("name", nameField.getText().trim())
                .append("phoneNo", phoneNoField.getText().trim())
                .append("passportNo", passportField.getText().trim());

        bookingCollection.insertOne(bookingData);
    }
}
