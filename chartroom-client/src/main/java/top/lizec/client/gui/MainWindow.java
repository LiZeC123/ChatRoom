package top.lizec.client.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import top.lizec.core.annotation.Component;

@Component
public class MainWindow {
    private JPanel mainPanel;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton btnMsg;


    public MainWindow() {
        btnMsg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("On mouse Thread=" + Thread.currentThread().getName());
                System.out.println("On mouse textAreal=" + textArea1);
                super.mouseClicked(e);
                String text = textField1.getText();
                textField1.setText("");
                String content = textArea1.getText() + "\n" + text;
                textArea1.setText(content);
            }
        });
    }

    public static void main(String[] args) {


    }

    public void setContent(String newContent) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("On setContent Thread=" + Thread.currentThread().getName());
            System.out.println("On setContent textAreal=" + textArea1);
            System.out.println("newContent is " + newContent);
            textArea1.append(newContent);
        });

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
