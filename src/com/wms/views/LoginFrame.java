package com.wms.views;

import com.wms.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton resetButton;

    public LoginFrame() {
        setTitle("物流管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建标题标签
        JLabel titleLabel = new JLabel("物流管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("用户名："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // 密码输入
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("密码："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        loginButton = new JButton("登录");
        resetButton = new JButton("重置");

        // 设置按钮样式
        loginButton.setPreferredSize(new Dimension(100, 30));
        resetButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        // 添加按钮事件监听器
        loginButton.addActionListener(e -> {
            boolean isLogin = LoginChecking(usernameField.getText(), new String(passwordField.getPassword()));
            if (isLogin) {
                JOptionPane.showMessageDialog(this, "登陆成功",
                        "登陆成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "账号或密码错误",
                        "登陆失败", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // 将表单面板和按钮面板添加到主面板
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置主面板
        add(mainPanel);
    }

    private boolean LoginChecking(String username, String password){
        boolean isLogin = false;
        Database db = new Database();
        db.connect();
        isLogin = db.callVerifyUserCredentials(username, password);
        return isLogin;
    }

    public void Show(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
    }
} 