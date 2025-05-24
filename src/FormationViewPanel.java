import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel that displays formations as cards with the ability to view details
 */
public class FormationViewPanel extends JPanel {

    private final FormationDAO formationDAO;
    private JPanel cardsPanel;
    private JScrollPane scrollPane;
    private JPanel detailPanel;
    private JDialog detailDialog;

    // Modern color scheme
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color CARD_HOVER = new Color(248, 250, 252);
    private final Color CARD_BORDER = new Color(226, 232, 240);
    private final Color CARD_SHADOW = new Color(0, 0, 0, 20);
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color SECONDARY_COLOR = new Color(107, 114, 128);
    private final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color BACKGROUND_COLOR = new Color(249, 250, 251);

    // Modern fonts
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private final Font DETAIL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    /**
     * Constructor
     */
    public FormationViewPanel() {
        this.formationDAO = new FormationDAO();
        initComponents();
        loadFormations();
    }

    /**
     * Initialize the UI components
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Header with modern styling
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Cards container with proper scrolling
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scroll pane with modern styling
        scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        add(scrollPane, BorderLayout.CENTER);

        // Detail dialog setup
        setupDetailDialog();
    }

    /**
     * Create modern header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        // Title section
        JPanel titleSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleSection.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Available Formations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleSection.add(titleLabel);

        // Button section
        JPanel buttonSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonSection.setBackground(Color.WHITE);

        JButton refreshButton = createModernButton("Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> loadFormations());
        buttonSection.add(refreshButton);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(buttonSection, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Create modern styled button
     */
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(SUBTITLE_FONT);
        button.setForeground(Color.blue);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Setup detail dialog
     */
    private void setupDetailDialog() {
        detailDialog = new JDialog();
        detailDialog.setTitle("Formation Details");
        detailDialog.setSize(650, 500);
        detailDialog.setLocationRelativeTo(null);
        detailDialog.setModal(true);

        detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        detailPanel.setBackground(Color.WHITE);
        detailDialog.add(detailPanel);
    }

    /**
     * Load formations from the database and display them as cards
     */
    public void loadFormations() {
        cardsPanel.removeAll();
        List<Formation> formations = formationDAO.getAllFormations();

        for (Formation formation : formations) {
            JPanel card = createModernFormationCard(formation);
            cardsPanel.add(card);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    /**
     * Create a modern, compact formation card
     */
    private JPanel createModernFormationCard(Formation formation) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 8));
        card.setBackground(CARD_BACKGROUND);
        card.setPreferredSize(new Dimension(280, 140));
        card.setBorder(createCardBorder());

        // Header with title and price
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);

        JLabel titleLabel = new JLabel(truncateText(formation.getTitle(), 25));
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setToolTipText(formation.getTitle());

        JLabel priceLabel = new JLabel(String.format("%.0f€", formation.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLabel.setForeground(PRIMARY_COLOR);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(priceLabel, BorderLayout.EAST);

        card.add(headerPanel, BorderLayout.NORTH);

        // Content panel with key info
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BACKGROUND);

        // Date and duration row
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String startDate = dateFormat.format(formation.getStartDate());

        contentPanel.add(createCompactInfoRow("📅", startDate));
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(createCompactInfoRow("⏱️", formation.getduree() + " days"));
        contentPanel.add(Box.createVerticalStrut(4));

        // Availability status
        boolean isAvailable = formationDAO.isFormationAvailable(formation.getFormationId());
        JPanel statusPanel = createStatusPanel(isAvailable);
        contentPanel.add(statusPanel);

        card.add(contentPanel, BorderLayout.CENTER);

        // Footer with view button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerPanel.setBackground(CARD_BACKGROUND);

        JButton viewButton = new JButton("View Details");
        viewButton.setFont(SMALL_FONT);
        viewButton.setForeground(PRIMARY_COLOR);
        viewButton.setBackground(Color.WHITE);
        viewButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        viewButton.setFocusPainted(false);
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> showFormationDetails(formation));

        footerPanel.add(viewButton);
        card.add(footerPanel, BorderLayout.SOUTH);

        // Add hover effects and click handler
        addCardInteractivity(card, formation);

        return card;
    }

    /**
     * Create modern card border with shadow effect
     */
    private Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
    }

    /**
     * Add hover effects and click functionality to card
     */
    private void addCardInteractivity(JPanel card, Formation formation) {
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showFormationDetails(formation);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.setBackground(CARD_HOVER);
                updateChildrenBackground(card, CARD_HOVER);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                        BorderFactory.createEmptyBorder(12, 14, 12, 14)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.setBackground(CARD_BACKGROUND);
                updateChildrenBackground(card, CARD_BACKGROUND);
                card.setBorder(createCardBorder());
            }
        };

        card.addMouseListener(mouseHandler);
        addMouseListenerToChildren(card, mouseHandler);
    }

    /**
     * Update background color of child components
     */
    private void updateChildrenBackground(Container container, Color color) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(color);
                if (component instanceof Container) {
                    updateChildrenBackground((Container) component, color);
                }
            }
        }
    }

    /**
     * Add mouse listener to child components for consistent hover behavior
     */
    private void addMouseListenerToChildren(Container container, MouseListener listener) {
        for (Component component : container.getComponents()) {
            component.addMouseListener(listener);
            if (component instanceof Container) {
                addMouseListenerToChildren((Container) component, listener);
            }
        }
    }

    /**
     * Create compact info row with icon
     */
    private JPanel createCompactInfoRow(String icon, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(CARD_BACKGROUND);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(SMALL_FONT);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(SMALL_FONT);
        textLabel.setForeground(SECONDARY_COLOR);

        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(textLabel);

        return panel;
    }

    /**
     * Create status panel showing availability
     */
    private JPanel createStatusPanel(boolean isAvailable) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(CARD_BACKGROUND);

        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        if (isAvailable) {
            statusLabel.setText("AVAILABLE");
            statusLabel.setForeground(SUCCESS_COLOR);
            statusLabel.setBackground(new Color(34, 197, 94, 20));
        } else {
            statusLabel.setText("FULL");
            statusLabel.setForeground(DANGER_COLOR);
            statusLabel.setBackground(new Color(239, 68, 68, 20));
        }
        statusLabel.setOpaque(true);

        panel.add(statusLabel);
        return panel;
    }

    /**
     * Truncate text to specified length
     */
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Show detailed information about a formation
     */
    private void showFormationDetails(Formation formation) {
        detailPanel.removeAll();

        // Header panel with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(formation.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        detailPanel.add(headerPanel, BorderLayout.NORTH);

        // Details panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);

        // Format the start date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String startDate = dateFormat.format(formation.getStartDate());

        // Add information fields with modern styling
        infoPanel.add(createDetailRow("Formation ID", String.valueOf(formation.getFormationId())));
        infoPanel.add(Box.createVerticalStrut(15));

        // Description section
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        descLabel.setForeground(new Color(31, 41, 55));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(8));

        JTextArea descArea = new JTextArea(formation.getDescription());
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setFont(DETAIL_FONT);
        descArea.setBackground(new Color(249, 250, 251));
        descArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(550, 100));
        descScroll.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        infoPanel.add(descScroll);
        infoPanel.add(Box.createVerticalStrut(15));

        infoPanel.add(createDetailRow("Start Date", startDate));
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(createDetailRow("Duration", formation.getduree() + " days"));
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(createDetailRow("Price", String.format("%.2f €", formation.getPrice())));
        infoPanel.add(Box.createVerticalStrut(15));

        // Enrollment information
        int enrollmentCount = formationDAO.getEnrollmentCount(formation.getFormationId());
        boolean isAvailable = formationDAO.isFormationAvailable(formation.getFormationId());

        infoPanel.add(createDetailRow("Current Enrollment", String.valueOf(enrollmentCount)));
        infoPanel.add(Box.createVerticalStrut(15));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setBackground(Color.WHITE);
        JLabel statusTitleLabel = new JLabel("Status: ");
        statusTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusTitleLabel.setForeground(new Color(31, 41, 55));

        JLabel statusValueLabel = new JLabel(isAvailable ? "Available" : "Fully Booked");
        statusValueLabel.setFont(DETAIL_FONT);
        statusValueLabel.setForeground(isAvailable ? SUCCESS_COLOR : DANGER_COLOR);

        statusPanel.add(statusTitleLabel);
        statusPanel.add(statusValueLabel);
        infoPanel.add(statusPanel);

        JScrollPane infoScroll = new JScrollPane(infoPanel);
        infoScroll.setBorder(BorderFactory.createEmptyBorder());
        detailPanel.add(infoScroll, BorderLayout.CENTER);

        // Footer with close button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));

        JButton closeButton = createModernButton("Close", SECONDARY_COLOR);
        closeButton.addActionListener(e -> detailDialog.setVisible(false));
        footerPanel.add(closeButton);

        detailPanel.add(footerPanel, BorderLayout.SOUTH);

        // Show the dialog
        detailDialog.setVisible(true);
    }

    /**
     * Create a detail row for the formation details view
     */
    private JPanel createDetailRow(String label, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);

        JLabel labelComponent = new JLabel(label + ": ");
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComponent.setForeground(new Color(31, 41, 55));
        labelComponent.setPreferredSize(new Dimension(140, 20));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(DETAIL_FONT);
        valueComponent.setForeground(SECONDARY_COLOR);

        panel.add(labelComponent);
        panel.add(valueComponent);

        return panel;
    }

    /**
     * Custom layout manager for wrapping cards
     */
    private static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                Container container = target;

                while (container.getSize().width == 0 && container.getParent() != null) {
                    container = container.getParent();
                }
                targetWidth = container.getSize().width;

                if (targetWidth == 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();

                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);

                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = d.width;
                            rowHeight = d.height;
                        } else {
                            rowWidth += hgap + d.width;
                            rowHeight = Math.max(rowHeight, d.height);
                        }
                    }
                }

                addRow(dim, rowWidth, rowHeight);

                dim.width = Math.max(dim.width, rowWidth);
                dim.height += insets.top + insets.bottom + vgap * 2;

                Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                if (scrollPane != null && target.isValid()) {
                    dim.width -= (hgap + 1);
                }

                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);

            if (dim.height > 0) {
                dim.height += getVgap();
            }

            dim.height += rowHeight;
        }
    }

    /**
     * Test method to demonstrate the panel
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better appearance
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Use default look and feel if Nimbus is not available
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    // Fallback to default
                }
            }

            JFrame frame = new JFrame("Formation Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            FormationViewPanel panel = new FormationViewPanel();
            frame.add(panel);

            frame.setVisible(true);
        });
    }
}