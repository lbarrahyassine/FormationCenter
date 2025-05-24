import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing client enrollments in formations
 */
public class EnrollmentManagementPanel extends JPanel {

    private EnrollmentDAO enrollmentDAO;
    private ClientDAO clientDAO;
    private FormationDAO formationDAO;

    // UI Components
    private JComboBox<Client> clientComboBox;
    private JComboBox<Formation> formationComboBox;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JButton enrollButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    public EnrollmentManagementPanel() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.clientDAO = new ClientDAO();
        this.formationDAO = new FormationDAO();

        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadData();
    }

    private void initializeComponents() {
        // Initialize combo boxes
        clientComboBox = new JComboBox<>();
        formationComboBox = new JComboBox<>();

        // Set preferred size for combo boxes to make them wider
        clientComboBox.setPreferredSize(new Dimension(150, 25));
        formationComboBox.setPreferredSize(new Dimension(150, 25));

        // Initialize table
        String[] columnNames = {"Client Name", "Formation Title", "Formation Duration", "Formation Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Initialize buttons
        enrollButton = new JButton("Enroll Client");
        deleteButton = new JButton("Remove Enrollment");
        refreshButton = new JButton("Refresh");

        // Initialize search components
        searchField = new JTextField(15);
        String[] filterOptions = {"All Enrollments", "By Client", "By Formation"};
        filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setPreferredSize(new Dimension(150, 25));

        // Set button properties
        enrollButton.setBackground(new Color(46, 125, 50));
        enrollButton.setForeground(Color.BLACK);
        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.RED);
        refreshButton.setBackground(new Color(25, 118, 210));
        refreshButton.setForeground(Color.BLUE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Enrollment Management"));

        // Top panel for enrollment form
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Client selection
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Select Client:"), gbc);
        gbc.gridx = 1;
        topPanel.add(clientComboBox, gbc);

        // Formation selection
        gbc.gridx = 2; gbc.gridy = 0;
        topPanel.add(new JLabel("Select Formation:"), gbc);
        gbc.gridx = 3;
        topPanel.add(formationComboBox, gbc);

        // Enroll button
        gbc.gridx = 4; gbc.gridy = 0;
        topPanel.add(enrollButton, gbc);

        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);
        searchPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 5;
        topPanel.add(searchPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Center panel for table
        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        // Enroll button action
        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enrollClient();
            }
        });

        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEnrollment();
            }
        });

        // Refresh button action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        // Search field action
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEnrollments();
            }
        });

        // Filter combo box action
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEnrollments();
            }
        });
    }

    private void loadData() {
        loadClients();
        loadFormations();
        loadEnrollments();
    }

    private void loadClients() {
        clientComboBox.removeAllItems();
        try {
            List<Client> clients = clientDAO.getAllClients();
            for (Client client : clients) {
                clientComboBox.addItem(client);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading clients: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFormations() {
        formationComboBox.removeAllItems();
        try {
            List<Formation> formations = formationDAO.getAllFormations();
            for (Formation formation : formations) {
                formationComboBox.addItem(formation);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading formations: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);
        try {
            // Get all clients and their enrollments
            List<Client> clients = clientDAO.getAllClients();
            for (Client client : clients) {
                List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByClient(client.getClientId());
                for (Enrollment enrollment : enrollments) {
                    Object[] row = {
                            enrollment.getClient().toString(),
                            enrollment.getFormation().getTitle(),
                            enrollment.getFormation().getduree() + " days",
                            "$" + enrollment.getFormation().getPrice()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading enrollments: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enrollClient() {
        Client selectedClient = (Client) clientComboBox.getSelectedItem();
        Formation selectedFormation = (Formation) formationComboBox.getSelectedItem();

        if (selectedClient == null || selectedFormation == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both a client and a formation.",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if client is already enrolled
        if (enrollmentDAO.isClientEnrolled(selectedClient.getClientId(), selectedFormation.getFormationId())) {
            JOptionPane.showMessageDialog(this,
                    "Client is already enrolled in this formation.",
                    "Already Enrolled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment(0, selectedClient.getClientId(), selectedFormation.getFormationId(), null, null);
        enrollment.setClient(selectedClient);
        enrollment.setFormation(selectedFormation);

        if (enrollmentDAO.createEnrollment(enrollment)) {
            JOptionPane.showMessageDialog(this,
                    "Client enrolled successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadEnrollments(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to enroll client. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEnrollment() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an enrollment to delete.",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clientName = (String) tableModel.getValueAt(selectedRow, 0);
        String formationTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the enrollment for:\n" +
                        "Client: " + clientName + "\n" +
                        "Formation: " + formationTitle + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Find the client and formation IDs
                Client client = findClientByName(clientName);
                Formation formation = findFormationByTitle(formationTitle);

                if (client != null && formation != null) {
                    if (enrollmentDAO.deleteEnrollment(client.getClientId(), formation.getFormationId())) {
                        JOptionPane.showMessageDialog(this,
                                "Enrollment deleted successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadEnrollments(); // Refresh table
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete enrollment.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting enrollment: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filterEnrollments() {
        String searchText = searchField.getText().toLowerCase().trim();
        String filterType = (String) filterComboBox.getSelectedItem();

        if (searchText.isEmpty()) {
            loadEnrollments();
            return;
        }

        tableModel.setRowCount(0);
        try {
            List<Client> clients = clientDAO.getAllClients();
            for (Client client : clients) {
                List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByClient(client.getClientId());
                for (Enrollment enrollment : enrollments) {
                    boolean shouldInclude = false;

                    switch (filterType) {
                        case "All Enrollments":
                            shouldInclude = enrollment.getClient().toString().toLowerCase().contains(searchText) ||
                                    enrollment.getFormation().getTitle().toLowerCase().contains(searchText);
                            break;
                        case "By Client":
                            shouldInclude = enrollment.getClient().toString().toLowerCase().contains(searchText);
                            break;
                        case "By Formation":
                            shouldInclude = enrollment.getFormation().getTitle().toLowerCase().contains(searchText);
                            break;
                    }

                    if (shouldInclude) {
                        Object[] row = {
                                enrollment.getClient().toString(),
                                enrollment.getFormation().getTitle(),
                                enrollment.getFormation().getduree() + " weeks",
                                "$" + enrollment.getFormation().getPrice()
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error filtering enrollments: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Client findClientByName(String name) {
        try {
            List<Client> clients = clientDAO.getAllClients();
            for (Client client : clients) {
                if (client.getLastname().equals(name) || client.getFirstname().equals(name)) {
                    return client;
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding client: " + e.getMessage());
        }
        return null;
    }

    private Formation findFormationByTitle(String title) {
        try {
            List<Formation> formations = formationDAO.getAllFormations();
            for (Formation formation : formations) {
                if (formation.getTitle().equals(title)) {
                    return formation;
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding formation: " + e.getMessage());
        }
        return null;
    }

    /**
     * Refresh the panel data
     */
    public void refreshData() {
        loadData();
    }
}