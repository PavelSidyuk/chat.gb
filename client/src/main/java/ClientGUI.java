import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


class ChatGUI extends JFrame {
    private JTextArea textArea = new JTextArea();
    private JPanel chatPanel = new JPanel();
    private JScrollPane js = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JTextField textInput = new JTextField();
    private JLabel textInputLabel = new JLabel("Your message :");
    private final JButton sendButton = new JButton("Send");
    private final ActionListener listener = event -> {
        String massage = textInput.getText();
        if (!massage.isEmpty()) {
            this.clientNetwork.sendMessage(textInput.getText());
        }

    };
    private ClientNetwork clientNetwork;

    public ChatGUI () {
        this.clientNetwork = new ClientNetwork();
        this.clientNetwork.setCallOnMsgRecieved(message -> textArea.append(message));
        this.clientNetwork.connect();
        this.setTitle("Chat");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new FlowLayout());
        textArea.setEditable(false);
        chatPanel.setBackground(Color.white);
        chatPanel.setPreferredSize(new Dimension(490, 490));
        js.setPreferredSize(new Dimension(450, 350));
        chatPanel.add(js);
        textInput.setPreferredSize(new Dimension(150, 25));
        chatPanel.add(textInputLabel);
        chatPanel.add(textInput);

        textInput.addActionListener(listener);
        sendButton.addActionListener(listener);
        chatPanel.add(sendButton);
        this.add(chatPanel);
        this.setVisible(true);
    }


    public void sendMessage (String str) {
        textArea.append(str);
    }
}