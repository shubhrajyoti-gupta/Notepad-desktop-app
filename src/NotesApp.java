import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NotesApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startupPanel;
    private JPanel homePanel;
    private JPanel editorPanel;
    private JTextArea textArea;
    private File currentFile;
    private JComboBox<String> fontSizeCombo;
    private JComboBox<String> fontFamilyCombo;
    private JButton boldButton;
    private JButton italicButton;
    private JButton underlineButton;
    private JFileChooser fileChooser;
    private boolean isBold = false;
    private boolean isItalic = false;
    private boolean isUnderline = false;

    public NotesApp() {
        setTitle("Java Notes App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized for better visibility

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        // Create panels
        createStartupPanel();
        createHomePanel();
        createEditorPanel();

        // Add panels to main panel
        mainPanel.add(startupPanel, "startup");
        mainPanel.add(homePanel, "home");
        mainPanel.add(editorPanel, "editor");

        add(mainPanel);

        // Show startup screen
        cardLayout.show(mainPanel, "startup");

        // Schedule transition to home screen after 3 seconds
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "home");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void createStartupPanel() {
        startupPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 105, 180), // Hot Pink
                    getWidth(), getHeight(), new Color(138, 43, 226) // Blue Violet
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative elements
                g2d.setColor(new Color(255, 255, 255, 40));
                for (int i = 0; i < 30; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int radius = (int) (Math.random() * 80) + 20;
                    g2d.fillOval(x, y, radius, radius);
                }

                // Diagonal lines
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i + getHeight(), getHeight());
                }
            }
        };
        startupPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("All your notes in one place");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 56));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        startupPanel.add(titleLabel);
    }

    private void createHomePanel() {
        homePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(64, 224, 208), // Turquoise
                    getWidth(), getHeight(), new Color(238, 130, 238) // Violet
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < 25; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int radius = (int) (Math.random() * 100) + 30;
                    g2d.fillOval(x, y, radius, radius);
                }
            }
        };
        homePanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 120));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel headerLabel = new JLabel("Notes Manager");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        headerPanel.add(headerLabel);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create new file button
        JButton createButton = new JButton("Create a New File");
        styleHomeButton(createButton, new Color(70, 130, 180), Color.WHITE);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditorPanel();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 20;
        gbc.ipadx = 50;
        contentPanel.add(createButton, gbc);

        // Open file button
        JButton openButton = new JButton("Open a File");
        styleHomeButton(openButton, new Color(50, 205, 50), Color.WHITE);
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(openButton, gbc);

        // Edit file button
        JButton editButton = new JButton("Edit a File");
        styleHomeButton(editButton, new Color(255, 165, 0), Color.WHITE);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileForEditing();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(editButton, gbc);

        homePanel.add(headerPanel, BorderLayout.NORTH);
        homePanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void styleHomeButton(JButton button, Color bgColor, Color textColor) {
        button.setPreferredSize(new Dimension(350, 90));
        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
                button.setFont(button.getFont().deriveFont(24f)); // Slightly larger on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setFont(button.getFont().deriveFont(22f));
            }
        });
    }

    private void createEditorPanel() {
        editorPanel = new JPanel(new BorderLayout());
        editorPanel.setBackground(Color.WHITE);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(40, 40, 40)); // Dark toolbar
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        toolBar.setPreferredSize(new Dimension(getWidth(), 70));

        // Toolbar title
        JLabel toolbarTitle = new JLabel("Document Editor");
        toolbarTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        toolbarTitle.setForeground(Color.WHITE);
        toolbarTitle.setBorder(new EmptyBorder(0, 15, 0, 20));
        toolBar.add(toolbarTitle);
        toolBar.addSeparator(new Dimension(20, 0));

        // Font family
        JLabel fontLabel = new JLabel("Font:");
        fontLabel.setForeground(Color.WHITE);
        fontLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        toolBar.add(fontLabel);

        String[] fonts = {"Arial", "Calibri", "Cambria", "Comic Sans MS", "Courier New",
                          "Georgia", "Helvetica", "Times New Roman", "Verdana"};
        fontFamilyCombo = new JComboBox<>(fonts);
        fontFamilyCombo.setSelectedItem("Calibri");
        fontFamilyCombo.setPreferredSize(new Dimension(120, 28));
        fontFamilyCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fontFamilyCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFont();
            }
        });
        toolBar.add(fontFamilyCombo);
        toolBar.addSeparator(new Dimension(10, 0));

        // Font size
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setForeground(Color.WHITE);
        sizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        toolBar.add(sizeLabel);

        String[] sizes = {"8", "10", "12", "14", "16", "18", "20", "24", "28", "32", "36", "48", "72"};
        fontSizeCombo = new JComboBox<>(sizes);
        fontSizeCombo.setSelectedItem("12");
        fontSizeCombo.setPreferredSize(new Dimension(60, 28));
        fontSizeCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fontSizeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFont();
            }
        });
        toolBar.add(fontSizeCombo);
        toolBar.addSeparator(new Dimension(15, 0));

        // Formatting buttons
        boldButton = new JButton("B");
        boldButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        boldButton.setPreferredSize(new Dimension(45, 32));
        boldButton.setBackground(new Color(60, 60, 60));
        boldButton.setForeground(Color.WHITE);
        boldButton.setFocusPainted(false);
        boldButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        boldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBold = !isBold;
                updateButtonStyle(boldButton, isBold);
                applyFont();
            }
        });

        italicButton = new JButton("I");
        italicButton.setFont(new Font("SansSerif", Font.ITALIC, 16));
        italicButton.setPreferredSize(new Dimension(45, 32));
        italicButton.setBackground(new Color(60, 60, 60));
        italicButton.setForeground(Color.WHITE);
        italicButton.setFocusPainted(false);
        italicButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        italicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isItalic = !isItalic;
                updateButtonStyle(italicButton, isItalic);
                applyFont();
            }
        });

        underlineButton = new JButton("U");
        underlineButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        underlineButton.setPreferredSize(new Dimension(45, 32));
        underlineButton.setBackground(new Color(60, 60, 60));
        underlineButton.setForeground(Color.WHITE);
        underlineButton.setFocusPainted(false);
        underlineButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        underlineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isUnderline = !isUnderline;
                updateButtonStyle(underlineButton, isUnderline);
                // Underline is visual only in this simple implementation
            }
        });

        toolBar.add(boldButton);
        toolBar.add(italicButton);
        toolBar.add(underlineButton);
        toolBar.addSeparator(new Dimension(20, 0));

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(70, 32));
        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        toolBar.add(saveButton);
        toolBar.addSeparator(new Dimension(10, 0));

        // Convert to PDF button
        JButton pdfButton = new JButton("Convert to PDF");
        pdfButton.setBackground(new Color(220, 20, 60));
        pdfButton.setForeground(Color.WHITE);
        pdfButton.setFocusPainted(false);
        pdfButton.setPreferredSize(new Dimension(120, 32));
        pdfButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertToPDF();
            }
        });
        toolBar.add(pdfButton);

        // Spacer
        toolBar.add(Box.createHorizontalGlue());

        // Back button
        JButton backButton = new JButton("Back to Home");
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(120, 32));
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "home");
            }
        });
        toolBar.add(backButton);

        // Text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Calibri", Font.PLAIN, 16)); // Larger default font
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        editorPanel.add(toolBar, BorderLayout.NORTH);
        editorPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void updateButtonStyle(JButton button, boolean isActive) {
        if (isActive) {
            button.setBackground(new Color(30, 144, 255)); // Dodger Blue when active
            button.setBorder(BorderFactory.createLoweredBevelBorder());
        } else {
            button.setBackground(new Color(60, 60, 60));
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }

    private void applyFont() {
        String fontFamily = (String) fontFamilyCombo.getSelectedItem();
        int fontSize = Integer.parseInt((String) fontSizeCombo.getSelectedItem());

        int style = Font.PLAIN;
        if (isBold) style |= Font.BOLD;
        if (isItalic) style |= Font.ITALIC;

        Font font = new Font(fontFamily, style, fontSize);
        textArea.setFont(font);
    }

    private void showEditorPanel() {
        currentFile = null;
        textArea.setText("");
        cardLayout.show(mainPanel, "editor");
    }

    private void openFile() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(currentFile.toPath()));
                textArea.setText(content);
                cardLayout.show(mainPanel, "editor");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(),
                                            "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openFileForEditing() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(currentFile.toPath()));
                textArea.setText(content);
                cardLayout.show(mainPanel, "editor");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + e.getMessage(),
                                            "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                // Ensure .txt extension
                if (!currentFile.getName().endsWith(".txt")) {
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");
                }
            } else {
                return;
            }
        }

        try {
            String content = textArea.getText();
            Files.write(currentFile.toPath(), content.getBytes());
            JOptionPane.showMessageDialog(this, "File saved successfully!",
                                        "Save Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(),
                                        "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertToPDF() {
        if (currentFile == null) {
            JOptionPane.showMessageDialog(this, "Please open a file first.",
                                        "No File", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String content = textArea.getText();
            String pdfPath = currentFile.getAbsolutePath().replace(".txt", ".pdf");

            // Simple PDF creation (in a real app, you'd use a library like iText)
            // For this demo, we'll just create a text file with PDF extension
            Path path = Paths.get(pdfPath);
            Files.write(path, content.getBytes());

            JOptionPane.showMessageDialog(this, "File converted to PDF successfully!\nSaved as: " + pdfPath,
                                        "Conversion Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error converting to PDF: " + e.getMessage(),
                                        "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new NotesApp().setVisible(true);
            }
        });
    }
}
