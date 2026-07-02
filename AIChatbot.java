import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AIChatbot extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public AIChatbot() {
        // 1. Setup the GUI Window
        setTitle("CodeAlpha AI Chatbot");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Chat display area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Bottom panel for input and button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0, 122, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 2. Action Listeners (Triggers when 'Send' is clicked or 'Enter' is pressed)
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        };
        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
        
        // Initial bot greeting
        appendMessage("🤖 Bot", "Hello! I am your AI assistant. How can I help you today?");
    }

    // 3. Process the user input
    private void processInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;

        // Display user message
        appendMessage("👤 You", userText);
        inputField.setText("");

        // Generate and display bot response
        String response = getBotResponse(userText.toLowerCase());
        appendMessage("🤖 Bot", response);
    }

    // Helper method to format text in the chat area
    private void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); // Auto-scroll to bottom
    }

    // 4. Rule-Based NLP Logic (FAQs and Keywords)
    private String getBotResponse(String input) {
        if (input.contains("hello") || input.contains("hi") || input.contains("hey")) {
            return "Hi there! How is your day going?";
        } 
        else if (input.contains("how are you")) {
            return "I am a computer program, so I don't have feelings, but my code is running perfectly! Thanks for asking.";
        } 
        else if (input.contains("your name") || input.contains("who are you")) {
            return "I am a virtual Chatbot created in Java for the CodeAlpha internship.";
        } 
        else if (input.contains("time")) {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            return "The current time is " + formatter.format(new Date()) + ".";
        } 
        else if (input.contains("weather")) {
            return "I cannot check live internet weather, but it's always sunny inside this computer!";
        } 
        else if (input.contains("java") || input.contains("programming")) {
            return "Java is an excellent Object-Oriented programming language. In fact, my GUI is built using Java Swing!";
        }
        else if (input.contains("internship") || input.contains("codealpha")) {
            return "CodeAlpha offers great virtual internships to help developers enhance their coding skills.";
        } 
        else if (input.contains("bye") || input.contains("exit") || input.contains("quit")) {
            return "Goodbye! Have a fantastic day ahead!";
        } 
        else {
            return "I'm sorry, I don't quite understand that yet. Could you rephrase your question?";
        }
    }

    public static void main(String[] args) {
        // Run the GUI in a thread-safe manner
        SwingUtilities.invokeLater(() -> {
            AIChatbot bot = new AIChatbot();
            bot.setVisible(true);
        });
    }
}
