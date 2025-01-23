import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewReservation extends JPanel {
    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    private String[] reservationColumns = {"Name", "Phone No", "Passport No", "Departure", "Destination", "Date", "Class", "Time"};

    public ViewReservation() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Set full background color

        JLabel heading = createHeading("View Reservations");
        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(null, reservationColumns);
        reservationsTable = new JTable(tableModel);
        reservationsTable.setBackground(new Color(240, 248, 255)); // Match table background
        reservationsTable.setGridColor(new Color(200, 200, 200)); // Optional: Table grid color
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setBackground(new Color(240, 248, 255)); // Match scroll pane background
        scrollPane.getViewport().setBackground(new Color(240, 248, 255)); // Match viewport background
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        add(scrollPane, BorderLayout.CENTER);

        fetchReservations();
    }

    private void fetchReservations() {
        MongoCollection<Document> bookingCollection = MongoDB.connect().getCollection("booking");
        tableModel.setRowCount(0);

        try (MongoCursor<Document> cursor = bookingCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document booking = cursor.next();
                tableModel.addRow(new Object[]{
                        booking.getString("name"),
                        booking.getString("phoneNo"),
                        booking.getString("passportNo"),
                        booking.getString("departure"),
                        booking.getString("destination"),
                        booking.getString("date"),
                        booking.getString("class"),
                        booking.getString("time")
                });
            }
        }
    }

    private JLabel createHeading(String text) {
        JLabel heading = new JLabel(text, JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        heading.setForeground(new Color(30, 144, 255));
        return heading;
    }
}
