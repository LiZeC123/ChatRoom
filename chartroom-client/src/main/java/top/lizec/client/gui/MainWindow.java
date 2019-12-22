package top.lizec.client.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import top.lizec.client.request.MessageRequester;
import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.Message;

@Component
public class MainWindow {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton btnMsg;

    @Automatique
    private MessageRequester requester;


    public MainWindow() {
        btnMsg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String text = textField1.getText();
                textField1.setText("");
                String content = "<LiZeC>: " + text;
                requester.addMessage(new Message("LiZeC", text));
            }
        });
    }

    public void setContent(String newContent) {
        SwingUtilities.invokeLater(() -> {
            textArea1.append(newContent);
            textArea1.append("\n");
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
