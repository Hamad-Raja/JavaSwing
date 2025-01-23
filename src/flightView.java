import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class flightView extends JPanel {
    private JTable reservationsTable;
    private DefaultTableModel tableModel;

    private String[] reservationColumns = {"Departure", "Destination", "Date", "Class", "Time"};

    public flightView() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Set full background color

        JLabel heading = createHeading("Flight Details");
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
        MongoCollection<Document> reservationsCollection = MongoDB.connect().getCollection("reservations");
        tableModel.setRowCount(0);

        try (MongoCursor<Document> cursor = reservationsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document reservation = cursor.next();
                tableModel.addRow(new Object[]{
                        reservation.getString("departure"),
                        reservation.getString("destination"),
                        reservation.getString("date"),
                        reservation.getString("class"),
                        reservation.getString("time")
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
