import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/**
 * Panel for managing clients using ClientDAO
 */
public class ClientManagementPanel extends JPanel {
    private ClientDAO clientDAO;
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton refreshButton;

    /**
     * Constructor for the client management panel
     */
    public ClientManagementPanel() {
        this.clientDAO = new ClientDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create components
        initializeComponents();

        // Load initial data
        refreshClientTable();
    }

    /**
     * Initialize all UI components
     */
    private void initializeComponents() {
        // Top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchClients());
        searchPanel.add(new JLabel("Search by Name: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Client");
        editButton = new JButton("Edit Client");
        refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddClientDialog());
        editButton.addActionListener(e -> showEditClientDialog());
        refreshButton.addActionListener(e -> refreshClientTable());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        // Table
        String[] columnNames = {"ID", "first Name", "last name", "Email", "Phone Number", "Registration Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        clientTable = new JTable(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        clientTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        clientTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Name
        clientTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Name
        clientTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
        clientTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Phone
        clientTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Date

        JScrollPane scrollPane = new JScrollPane(clientTable);

        // Add components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refresh the client table with all clients
     */
    private void refreshClientTable() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Get all clients and add to table
        List<Client> clients = clientDAO.getAllClients();
        for (Client client : clients) {
            addClientToTable(client);
        }
    }

    /**
     * Search clients by name
     */
    private void searchClients() {
        String searchTerm = searchField.getText().trim();

        // Clear existing data
        tableModel.setRowCount(0);

        // If search field is empty, show all clients
        if (searchTerm.isEmpty()) {
            refreshClientTable();
            return;
        }

        // Get clients by search term
        List<Client> clients = clientDAO.searchClientsByName(searchTerm);
        for (Client client : clients) {
            addClientToTable(client);
        }
    }

    /**
     * Add a client to the table
     */
    private void addClientToTable(Client client) {
        tableModel.addRow(new Object[]{
                client.getClientId(),
                client.getFirstname(),
                client.getLastname(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getRegistrationDate()
        });
    }

    /**
     * Show dialog to add a new client
     */
    private void showAddClientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Client", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = createClientFormPanel(null);
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveNewClient(formPanel)) {
                    dialog.dispose();
                }
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Show dialog to edit an existing client
     */
    private void showEditClientDialog() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a client to edit.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int clientId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Client client = clientDAO.getClientById(clientId);

        if (client == null) {
            JOptionPane.showMessageDialog(this,
                    "Error loading client data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Client", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = createClientFormPanel(client);
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (updateExistingClient(formPanel, client)) {
                    dialog.dispose();
                }
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Create a form panel for client data
     */
    private JPanel createClientFormPanel(Client client) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField fnameField = new JTextField(20);
        JTextField lnameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);

        // Set values if editing an existing client
        if (client != null) {
            fnameField.setText(client.getFirstname());
            lnameField.setText(client.getLastname());
            emailField.setText(client.getEmail());
            phoneField.setText(client.getPhoneNumber());
        }

        panel.add(new JLabel("first Name:"));
        panel.add(fnameField);
        panel.add(new JLabel("last Name:"));
        panel.add(lnameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        // Add component names for later retrieval
        fnameField.setName("fnameField");
        lnameField.setName("lnameField");
        emailField.setName("emailField");
        phoneField.setName("phoneField");

        return panel;
    }

    /**
     * Save a new client from form data
     */
    private boolean saveNewClient(JPanel formPanel) {
        JTextField fnameField = (JTextField) findComponentByName(formPanel, "fnameField");
        JTextField lnameField = (JTextField) findComponentByName(formPanel, "lnameField");
        JTextField emailField = (JTextField) findComponentByName(formPanel, "emailField");
        JTextField phoneField = (JTextField) findComponentByName(formPanel, "phoneField");

        String firstname = fnameField.getText().trim();
        String lastname = lnameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Basic validation
        if (!validateFormData(firstname,lastname, email, phone)) {
            return false;
        }

        // Create and save new client
        Client newClient = new Client();
        newClient.setFirstname(firstname);
        newClient.setLastname(lastname);
        newClient.setEmail(email);
        newClient.setPhoneNumber(phone);
        newClient.setRegistrationDate(new Date()); // Current date

        Client createdClient = clientDAO.createClient(newClient);

        if (createdClient != null) {
            // Add to table and refresh
            addClientToTable(createdClient);
            JOptionPane.showMessageDialog(this,
                    "Client added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error adding new client.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Update an existing client from form data
     */
    private boolean updateExistingClient(JPanel formPanel, Client client) {
        JTextField firstnameField = (JTextField) findComponentByName(formPanel, "firstnameField");
        JTextField lastnameField = (JTextField) findComponentByName(formPanel, "lastnameField");
        JTextField emailField = (JTextField) findComponentByName(formPanel, "emailField");
        JTextField phoneField = (JTextField) findComponentByName(formPanel, "phoneField");

        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Basic validation
        if (!validateFormData(firstname, lastname, email, phone)) {
            return false;
        }

        // Update client data
        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setEmail(email);
        client.setPhoneNumber(phone);

        boolean updated = clientDAO.updateClient(client);

        if (updated) {
            // Refresh table
            refreshClientTable();
            JOptionPane.showMessageDialog(this,
                    "Client updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error updating client.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Basic validation of form data
     */
    private boolean validateFormData(String firstname, String lastname, String email, String phone) {
        if (firstname.isEmpty() || email.isEmpty() || phone.isEmpty()|| lastname.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Simple email validation
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Find a component by name
     */
    private Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
            if (component instanceof Container) {
                Component found = findComponentByName((Container) component, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * Main method for testing the panel
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Client Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new ClientManagementPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}