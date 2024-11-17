import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class EncryptionClientGUI extends JFrame {
    // private JTextField messageField;
    // private JTextArea outputArea;
    // private JComboBox<String> algorithmBox;
    // private JButton encryptButton;
    // private JButton decryptButton;
    // private JLabel timeLabel;

    private JTextField messageField;
    private JTextArea outputArea;
    private JComboBox<String> algorithmBox;
    private JButton encryptButton;
    private JButton decryptButton;
    private JLabel messageLabel;
    private JLabel timeLabel;
    private JLabel algorithmsLabel;
    private JLabel titleLabel;
    private JLabel outputLabel;

    public EncryptionClientGUI() {

        JFrame frame = new JFrame("Encryption and Decryption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set the initial window size
        frame.setLayout(new BorderLayout());

        // Set icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png")); // Adjust path as needed
        frame.setIconImage(icon.getImage());

        messageField = new JTextField(20);
        outputArea = new JTextArea(6, 60);
        outputArea.setEditable(false);

        String[] algorithms = { "AES", "RSA" };
        algorithmBox = new JComboBox<>(algorithms);

        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");

        timeLabel = new JLabel("Time: ");

        // Style button
        applyButtonStyle(List.of(encryptButton, decryptButton));
        // encryptButton.setBounds(100, 100, 200, 50);
        // encryptButton.setBackground(new Color(0, 153, 204)); // Background color
        // encryptButton.setForeground(Color.GREEN); // Text color
        // encryptButton.setFocusPainted(false); // Remove focus border
        // encryptButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Create a wrapper panel to center the grid and add margins
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setBackground(Color.CYAN);
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(35, 20, 20, 20)); // 40px margin from the top of the
                                                                                 // wrapper frame
        titleLabel = new JLabel("Encryption and Decryption");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        wrapperPanel.add(titleLabel, BorderLayout.NORTH);

        // Create the grid panel
        JPanel panel = new JPanel();
        // Create a panel with a custom rounded border (using Graphics2D)
        panel.setBorder(null); // This removes any default border
        panel.setLayout(new GridBagLayout());
        // panel.setBorder(new RoundedBorder(20)); // 20px radius for rounded corners
        GridBagConstraints gbc = new GridBagConstraints();
        // Set insets for components
        gbc.insets = new Insets(10, 10, 10, 10);

        // set Background color
        panel.setBackground(Color.CYAN);

        // Message Label
        messageLabel = new JLabel("Enter Message:");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(messageLabel, gbc);

        // Message Field
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        setPlaceholder(messageField, "Enter your message...");
        messageField.setOpaque(false); // Ensure transparency for the rounded look
        messageField.setPreferredSize(new Dimension(300, 40)); // Set size
        messageField.setBorder(new RoundedBorder(20)); // Apply the rounded border
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(messageField, gbc);

        // Algorithm Label
        algorithmsLabel = new JLabel("Algorithm:");
        algorithmsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(algorithmsLabel, gbc);

        // Algorithm ComboBox
        algorithmBox.setBorder(new RoundedBorder(5)); // Apply the rounded border
        algorithmBox.setBackground(panel.getBackground());
        algorithmBox.setForeground(Color.black);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(algorithmBox, gbc);

        // gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0; // First column
        gbc.gridy = 2; // On row 2
        gbc.anchor = GridBagConstraints.LINE_END;
        // gbc.gridwidth = 1; // Each button takes one column
        panel.add(encryptButton, gbc);

        // Decrypt Button
        gbc.gridx = 1; // Second column
        gbc.gridy = 2; // On row 2
        // gbc.gridwidth = 1; // Each button takes one column
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(decryptButton, gbc);

        // Output Label
        outputLabel = new JLabel("Output:");
        outputLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(outputLabel, gbc);

        // Output Area
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setFont(new Font("Arial", Font.PLAIN, 16));
        scrollPane.setOpaque(false); // Ensure transparency for the rounded look
        // outputArea.setPreferredSize(new Dimension(300, 40)); // Set size
        scrollPane.setBorder(new RoundedBorder(20)); // Apply the rounded border
        outputArea.setBackground(panel.getBackground());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);
        // panel.add(outputArea, gbc);

        // Time Label
        timeLabel = new JLabel("Time:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(timeLabel, gbc);

        // Set the custom border for rounded corners
        algorithmBox.setUI(new BasicComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                ComboPopup popup = super.createPopup();
                ((JComponent) popup).setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true)); // Optional: set
                                                                                                     // popup border
                return popup;
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processRequest("encrypt");
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processRequest("decrypt");
            }
        });

        // Center the grid panel with equal margins on both sides
        wrapperPanel.add(panel, BorderLayout.CENTER);

        // Add the wrapper panel to the frame
        frame.add(wrapperPanel, BorderLayout.CENTER);
        // frame.add(panel);
        // Make the frame visible
        frame.setVisible(true);

        // Add a resize listener to dynamically adjust the grid size
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                // Set the width of the panel to 70% of the frame's width
                int panelWidth = (int) (frame.getWidth() * 0.6);
                panel.setPreferredSize(new Dimension(panelWidth, frame.getHeight()));

                // Revalidate the layout so it applies the new size
                frame.revalidate();
            }
        });
        // add(panel);
        // setVisible(true);
    }

    // Custom Rounded Border Class for the JPanel (no rectangle corners)
    static class RoundedPanelBorderWithoutRectangleCorners extends AbstractBorder {
        private int radius;

        public RoundedPanelBorderWithoutRectangleCorners(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            // Create a Graphics2D object to get more control over the drawing
            Graphics2D g2d = (Graphics2D) g;

            // Set anti-aliasing for smoother corners
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set the border color
            g2d.setColor(Color.GRAY); // Border color

            // Draw a rounded rectangle without the straight rectangle corners
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 10, 10, 10); // Set padding inside the border
        }
    }

    // Button style method
    private static void applyButtonStyle(List<JButton> buttons) {
        for (JButton button : buttons) {
            // Set background, foreground, font, and remove focus border
            button.setBackground(new Color(0, 153, 204)); // Background color
            button.setForeground(Color.WHITE); // Text color
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setFocusPainted(false);

            // Customize border radius by overriding paintComponent
            button.setContentAreaFilled(false); // Remove default background
            button.setOpaque(false); // For custom rendering
            button.setBorderPainted(false);

            // Add hover effects
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(0, 204, 255)); // Lighter blue on hover
                    button.repaint(); // Ensure the button redraws
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(0, 153, 204)); // Default blue
                    button.repaint();
                }
            });

            // Rounded border
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(button.getBackground());
                    g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30); // Rounded corners

                    // Draw text
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (c.getWidth() - fm.stringWidth(button.getText())) / 2;
                    int textY = (c.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.setColor(button.getForeground());
                    g2d.drawString(button.getText(), textX, textY);
                }
            });
        }
    }

    // Custom Border for Rounded Corners
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded rectangle border
            g2.setColor(Color.LIGHT_GRAY); // Border color
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2); // Adjust padding
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = radius / 2;
            insets.right = radius / 2;
            insets.top = radius / 2;
            insets.bottom = radius / 2;
            return insets;
        }
    }

    // Method to set placeholder text and focus listeners on a JTextField
    private static void setPlaceholder(JTextField textField, String placeholder) {
        // Set the placeholder text
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        // Add FocusListener to handle placeholder behavior
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText(""); // Clear placeholder text
                    textField.setForeground(Color.BLACK); // Set text color to black
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder); // Reset to placeholder text
                    textField.setForeground(Color.GRAY); // Set placeholder color
                }
            }
        });
    }

    // Request send to the server.
    private void processRequest(String operation) {
        String text = messageField.getText();
        String algorithm = (String) algorithmBox.getSelectedItem();

        try (Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeUTF(algorithm);
            out.writeUTF(operation);
            out.writeUTF(text);
            out.flush();

            String result = in.readUTF();
            long duration = in.readLong();

            outputArea.setText(result);
            timeLabel.setText("Time: " + duration + " ms");

        } catch (IOException e) {
            e.printStackTrace();
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EncryptionClientGUI());
    }
}
