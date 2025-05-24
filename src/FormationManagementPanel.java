import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class FormationManagementPanel extends JPanel {
    private FormationDAO formationDAO;
    private JTable formationTable;
    private DefaultTableModel tableModel;
    private JTextField libeleField, descriptionField, dureeField, tarifField;
    private JFormattedTextField dateField;
    private JButton addButton, editButton, deleteButton, clearButton;

    public FormationManagementPanel() {
        this.formationDAO = new FormationDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadFormations();
    }

    private void initializeComponents() {
        // Table setup
        String[] columnNames = {"ID", "Libellé", "Description", "Date Début", "Durée", "Tarif"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        formationTable = new JTable(tableModel);
        formationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Form fields
        libeleField = new JTextField(20);
        descriptionField = new JTextField(20);
        dureeField = new JTextField(5);
        tarifField = new JTextField(8);
        dateField = new JFormattedTextField(new java.text.SimpleDateFormat("yyyy-MM-dd"));
        dateField.setColumns(10);

        // Buttons
        addButton = new JButton("Ajouter");
        editButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        clearButton = new JButton("Vider");

        // Style buttons
        addButton.setBackground(new Color(46, 125, 50));
        addButton.setForeground(Color.black);
        editButton.setBackground(new Color(25, 118, 210));
        editButton.setForeground(Color.black);
        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.red);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Gestion des Formations"));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields
        addFormField(formPanel, gbc, "Libellé:", libeleField, 0);
        addFormField(formPanel, gbc, "Description:", descriptionField, 1);
        addFormField(formPanel, gbc, "Date Début:", dateField, 2);
        addFormField(formPanel, gbc, "Durée (semaines):", dureeField, 3);
        addFormField(formPanel, gbc, "Tarif:", tarifField, 4);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(formationTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 300));

        // Add components to main panel
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void setupEventListeners() {
        // Add button
        addButton.addActionListener(e -> addFormation());

        // Edit button
        editButton.addActionListener(e -> editFormation());

        // Delete button
        deleteButton.addActionListener(e -> deleteFormation());

        // Clear button
        clearButton.addActionListener(e -> clearForm());

        // Table selection listener
        formationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });
    }

    private void loadFormations() {
        tableModel.setRowCount(0); // Clear existing data
        List<Formation> formations = formationDAO.getAllFormations();
        for (Formation formation : formations) {
            Object[] row = {
                    formation.getFormationId(),
                    formation.getTitle(),
                    formation.getDescription(),
                    formation.getStartDate(),
                    formation.getduree(),
                    formation.getPrice()
            };
            tableModel.addRow(row);
        }
    }

    private void addFormation() {
        try {
            Formation formation = createFormationFromInput();
            if (formation != null) {
                // In a real application, you would call formationDAO.insert(formation)
                // For now, just add to the table
                /*Object[] row = {
                        formation.getFormationId(),
                        formation.getTitle(),
                        formation.getDescription(),
                        formation.getStartDate(),
                        formation.getduree(),
                        formation.getPrice()
                };
                tableModel.addRow(row);*/
                formationDAO.insertFormation(formation);
                loadFormations();
                clearForm();
                JOptionPane.showMessageDialog(this, "Formation ajoutée avec succès!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editFormation() {
        int selectedRow = formationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une formation à modifier", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Formation formation = createFormationFromInput();
            if (formation != null) {
                // Set the ID from the selected row
                formation.setFormationId((int) tableModel.getValueAt(selectedRow, 0));

                // In a real application, you would call formationDAO.update(formation)
                // For now, just update the table
                /*tableModel.setValueAt(formation.getTitle(), selectedRow, 1);
                tableModel.setValueAt(formation.getDescription(), selectedRow, 2);
                tableModel.setValueAt(formation.getStartDate(), selectedRow, 3);
                tableModel.setValueAt(formation.getduree(), selectedRow, 4);
                tableModel.setValueAt(formation.getPrice(), selectedRow, 5);*/
                formationDAO.updateFormation(formation);
                loadFormations();
                clearForm();
                JOptionPane.showMessageDialog(this, "Formation modifiée avec succès!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFormation() {
        int selectedRow = formationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une formation à supprimer", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette formation?",
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            // In a real application, you would call formationDAO.delete(id)
            //tableModel.removeRow(selectedRow);
            formationDAO.deleteFormation(id);
            loadFormations();
            JOptionPane.showMessageDialog(this, "Formation supprimée avec succès!");
        }
    }

    private Formation createFormationFromInput() throws Exception {
        String libele = libeleField.getText().trim();
        String description = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();
        String dureeStr = dureeField.getText().trim();
        String tarifStr = tarifField.getText().trim();

        // Validation
        if (libele.isEmpty() || description.isEmpty() || dateStr.isEmpty() || dureeStr.isEmpty() || tarifStr.isEmpty()) {
            throw new Exception("Tous les champs sont obligatoires");
        }

        try {
            Date dateDebut = Date.valueOf(dateStr);
            int duree = Integer.parseInt(dureeStr);
            double tarif = Double.parseDouble(tarifStr);

            return new Formation(0, libele, description, dateDebut, duree, tarif);
        } catch (IllegalArgumentException e) {
            throw new Exception("Format invalide: " + e.getMessage());
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = formationTable.getSelectedRow();
        if (selectedRow >= 0) {
            libeleField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            descriptionField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            dateField.setValue(tableModel.getValueAt(selectedRow, 3));
            dureeField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            tarifField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    private void clearForm() {
        libeleField.setText("");
        descriptionField.setText("");
        dateField.setValue(null);
        dureeField.setText("");
        tarifField.setText("");
        formationTable.clearSelection();
    }

    // Example of how to use this panel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestion des Formations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new FormationManagementPanel());
        frame.setVisible(true);
    }
}