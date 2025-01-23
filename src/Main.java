import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JFrame {
    private JTextField departureField, destinationField;
    private JSpinner departingSpinner;
    private JComboBox<String> classComboBox;
    private JButton searchButton;

    public Main() {
        // Create main frame
        setTitle("Flight Reservation");
        setSize(800, 600); // Increased frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        // Departure field
        JLabel departureLabel = createStyledLabel("Departure:");
        departureLabel.setBounds(50, 50, 100, 30);
        panel.add(departureLabel);

        departureField = createStyledTextField();
        departureField.setBounds(150, 50, 250, 30);
        panel.add(departureField);

        // Destination field
        JLabel destinationLabel = createStyledLabel("Destination:");
        destinationLabel.setBounds(50, 100, 100, 30);
        panel.add(destinationLabel);

        destinationField = createStyledTextField();
        destinationField.setBounds(150, 100, 250, 30);
        panel.add(destinationField);

        // Departing date
        JLabel departingLabel = createStyledLabel("Departing:");
        departingLabel.setBounds(50, 150, 100, 30);
        panel.add(departingLabel);

        departingSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(departingSpinner, "dd/MM/yyyy");
        departingSpinner.setEditor(editor);
        departingSpinner.setBounds(150, 150, 250, 30);
        styleSpinner(departingSpinner);
        panel.add(departingSpinner);

        // Flight class dropdown
        JLabel classLabel = createStyledLabel("Class:");
        classLabel.setBounds(50, 200, 100, 30);
        panel.add(classLabel);

        String[] flightClasses = {"Economy", "Business", "First Class"};
        classComboBox = new JComboBox<>(flightClasses);
        classComboBox.setBounds(150, 200, 250, 30);
        styleComboBox(classComboBox);
        panel.add(classComboBox);

        // Search button
        searchButton = createStyledButton("Search");
        searchButton.setBounds(150, 300, 200, 40);
        searchButton.addActionListener(new SearchAction());
        panel.add(searchButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    // ActionListener class for Search button
    public class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String departure = departureField.getText().trim();
            String destination = destinationField.getText().trim();
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format((Date) departingSpinner.getValue());

            fetchAndDisplayFlights(departure, destination, formattedDate);
        }

        private void fetchAndDisplayFlights(String departure, String destination, String date) {
            MongoCollection<Document> reservationCollection = MongoDB.connect().getCollection("reservations");

            // Create query
            Document query = new Document("departure", departure)
                    .append("destination", destination)
                    .append("date", date);

            // Execute query and build the table
            try (MongoCursor<Document> cursor = reservationCollection.find(query).iterator()) {
                if (!cursor.hasNext()) {
                    JOptionPane.showMessageDialog(null, "No flights found for the selected criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Table Columns
                String[] columns = {"Departure", "Destination", "Date", "Class", "Time", "Action"};
                DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 5;
                    }
                };

                while (cursor.hasNext()) {
                    Document reservation = cursor.next();
                    Object[] row = {
                            reservation.getString("departure"),
                            reservation.getString("destination"),
                            reservation.getString("date"),
                            reservation.getString("class"),
                            reservation.getString("time"),
                            "Select"
                    };
                    tableModel.addRow(row);
                }

                JTable table = new JTable(tableModel);
                styleTable(table);
                table.getColumn("Action").setCellRenderer(new ButtonRenderer());
                table.getColumn("Action").setCellEditor(new ButtonEditor());
                JScrollPane scrollPane = new JScrollPane(table);

                JFrame resultFrame = new JFrame("Search Results");
                resultFrame.setSize(800, 600); // Increased size for results frame
                resultFrame.setLocationRelativeTo(null);
                resultFrame.add(scrollPane);
                resultFrame.setVisible(true);
            }
        }
    }

    // Styling helper methods
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Arial", Font.PLAIN, 14));
        JComponent editor = spinner.getEditor();
        editor.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
    }
}

// ButtonRenderer class for rendering table buttons
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : ""); // Set button label
        return this;
    }
}

// ButtonEditor class for editing table buttons
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;

    public ButtonEditor() {
        super(new JTextField());
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = value != null ? value.toString() : "";
        button.setText(label);

        String selectedDeparture = table.getValueAt(row, 0).toString();
        String selectedDestination = table.getValueAt(row, 1).toString();
        String selectedDate = table.getValueAt(row, 2).toString();
        String selectedClass = table.getValueAt(row, 3).toString();
        String selectedTime = table.getValueAt(row, 4).toString();

        new Booking(selectedDeparture, selectedDestination, selectedDate, selectedClass, selectedTime);
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        clicked = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
