package top.lizec.client.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import top.lizec.client.request.UserRequester;
import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
public class Login {
    private JTextField textField1;
    private JTextField textField2;
    private JButton 重置;
    private JButton 登录;
    private JPanel mainPanel;

    private JFrame parent;

    @Automatique
    private UserRequester userRequester;

    @Automatique
    private MainWindow mainWindow;

    public Login() {
        登录.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String username = textField1.getText();
                String password = textField2.getText();
                User user = new User(username, password);
                String token = userRequester.login(user);
                if (token != null) {
                    JFrame jFrame = new JFrame("Test");
                    jFrame.setContentPane(mainWindow.getMainPanel());
                    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    jFrame.pack();
                    jFrame.setVisible(true);
                    mainWindow.setUsername(username);
                    mainWindow.setToken(token);
                    parent.setVisible(false);
                }

            }
        });
    }

    public static void main(String[] args) {
        //MainWindow mainWindow = context.getObjectByType(MainWindow.class);
        JFrame jFrame = new JFrame("Test");
        jFrame.setContentPane(new Login().mainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setParent(JFrame parent) {
        this.parent = parent;
    }
}
