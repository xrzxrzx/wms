package com.wms.views;

import com.wms.database.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JCheckBox rememberPasswordCheckBox;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JLabel logoLabel;
    private JLabel versionLabel;

    // 颜色常量 - 更现代的配色方案
    private static final Color PRIMARY_COLOR = new Color(75, 0, 130); // 深紫色
    private static final Color SECONDARY_COLOR = new Color(138, 43, 226); // 蓝紫色
    private static final Color ACCENT_COLOR = new Color(255, 105, 180); // 热粉色
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94); // 绿色
    private static final Color ERROR_COLOR = new Color(239, 68, 68); // 红色
    private static final Color WARNING_COLOR = new Color(245, 158, 11); // 橙色
    private static final Color BACKGROUND_COLOR = new Color(249, 250, 251); // 浅灰背景
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39); // 深色文字
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128); // 灰色文字

    public LoginFrame() {
        initializeFrame();
        createComponents();
        setupLayout();
        setupEventHandlers();
        applyStyles();
        setupAnimations();
    }

    private void initializeFrame() {
        setTitle("物流管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // 设置窗口圆角（仅在某些系统上有效）
        setUndecorated(true);

        // 设置窗口图标
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png")));
        } catch (Exception e) {
            // 图标文件不存在，忽略
        }
    }

    private void createComponents() {
        // 创建组件
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");
        resetButton = new JButton("重置");
        rememberPasswordCheckBox = new JCheckBox("记住密码");
        statusLabel = new JLabel("请输入用户名和密码", SwingConstants.CENTER);
        progressBar = new JProgressBar();
        logoLabel = new JLabel("🚚", SwingConstants.CENTER); // 使用emoji作为临时logo
        versionLabel = new JLabel("v2.0", SwingConstants.CENTER);

        // 设置进度条
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setForeground(PRIMARY_COLOR);

        // 设置状态标签样式
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 设置logo样式
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        // 设置版本标签
        versionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        versionLabel.setForeground(TEXT_SECONDARY);
    }

    private void setupLayout() {
        // 创建主面板 - 使用渐变背景
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // 创建渐变背景
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(147, 51, 234), // 紫色
                        getWidth(), getHeight(), new Color(79, 70, 229) // 蓝色
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // 创建顶部面板
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 创建登录卡片
        JPanel loginCard = createLoginCard();
        mainPanel.add(loginCard, BorderLayout.CENTER);

        // 创建底部面板
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Logo面板
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        logoPanel.add(logoLabel, BorderLayout.CENTER);

        // 版本标签
        JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        versionPanel.setOpaque(false);
        versionPanel.add(versionLabel);

        logoPanel.add(versionPanel, BorderLayout.SOUTH);

        // 创建标题标签
        JLabel titleLabel = new JLabel("物流管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // 创建副标题
        JLabel subtitleLabel = new JLabel("Warehouse Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 180));

        topPanel.add(logoPanel, BorderLayout.NORTH);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // 启用抗锯齿
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制圆角矩形背景
                g2d.setColor(CARD_COLOR);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                // 绘制阴影效果
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fill(new RoundRectangle2D.Float(2, 2, getWidth()-4, getHeight()-4, 20, 20));

                g2d.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setOpaque(false);

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel usernameLabel = new JLabel("👤 用户名");
        usernameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        // 密码输入
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel passwordLabel = new JLabel("🔒 密码");
        passwordLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // 记住密码选项
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        rememberPasswordCheckBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        rememberPasswordCheckBox.setForeground(TEXT_SECONDARY);
        formPanel.add(rememberPasswordCheckBox, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        // 状态标签和进度条面板
        JPanel statusPanel = new JPanel(new BorderLayout(8, 8));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(progressBar, BorderLayout.SOUTH);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        card.add(statusPanel, BorderLayout.NORTH);

        return card;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // 版权信息
        JLabel copyrightLabel = new JLabel("© 2025 物流管理系统 版权所有", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(255, 255, 255, 150));
        bottomPanel.add(copyrightLabel, BorderLayout.CENTER);

        return bottomPanel;
    }

    private void setupEventHandlers() {
        // 登录按钮事件
        loginButton.addActionListener(e -> performLogin());

        // 重置按钮事件
        resetButton.addActionListener(e -> resetForm());

        // 键盘事件
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resetForm();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // 窗口事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                usernameField.requestFocusInWindow();
            }
        });

        // 输入验证
        usernameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
        });

        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateInput(); }
        });
    }

    private void applyStyles() {
        // 设置按钮样式
        loginButton.setPreferredSize(new Dimension(140, 40));
        resetButton.setPreferredSize(new Dimension(140, 40));

        // 设置输入框样式
        usernameField.setPreferredSize(new Dimension(250, 35));
        passwordField.setPreferredSize(new Dimension(250, 35));

        // 应用自定义样式
        applyButtonStyle(loginButton, PRIMARY_COLOR, Color.WHITE);
        applyButtonStyle(resetButton, TEXT_SECONDARY, Color.WHITE);

        // 设置输入框样式
        applyTextFieldStyle(usernameField);
        applyTextFieldStyle(passwordField);
    }

    private void applyButtonStyle(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 圆角按钮
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }

    private void applyTextFieldStyle(JTextField textField) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField.setBackground(new Color(248, 249, 250));

        // 添加焦点效果
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private void setupAnimations() {
        // 窗口淡入效果
        setOpacity(0.0f);
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            float opacity = 0.0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.1f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                setOpacity(opacity);
            }
        });
        fadeInTimer.start();
    }

    private void validateInput() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        boolean isValid = !username.isEmpty() && !password.isEmpty();
        loginButton.setEnabled(isValid);

        if (isValid) {
            statusLabel.setText("✅ 请点击登录或按回车键");
            statusLabel.setForeground(SUCCESS_COLOR);
        } else {
            statusLabel.setText("📝 请输入用户名和密码");
            statusLabel.setForeground(TEXT_SECONDARY);
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("⚠️ 请输入用户名和密码");
            return;
        }

        // 显示进度条
        setLoginState(true);

        // 在后台线程中执行登录验证
        SwingWorker<Boolean, Void> loginWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return LoginChecking(username, password);
            }

            @Override
            protected void done() {
                try {
                    boolean isLogin = get();
                    handleLoginResult(isLogin);
                } catch (Exception e) {
                    handleLoginError(e);
                } finally {
                    setLoginState(false);
                }
            }
        };

        loginWorker.execute();
    }

    private void setLoginState(boolean isLoggingIn) {
        loginButton.setEnabled(!isLoggingIn);
        resetButton.setEnabled(!isLoggingIn);
        usernameField.setEnabled(!isLoggingIn);
        passwordField.setEnabled(!isLoggingIn);
        progressBar.setVisible(isLoggingIn);

        if (isLoggingIn) {
            statusLabel.setText("🔄 正在验证登录信息...");
            statusLabel.setForeground(PRIMARY_COLOR);
        }
    }

    private void handleLoginResult(boolean isLogin) {
        if (isLogin) {
            showSuccess("🎉 登录成功，正在进入系统...");

            // 保存记住密码设置
            if (rememberPasswordCheckBox.isSelected()) {
                saveRememberPassword();
            }

            // 延迟关闭登录窗口，让用户看到成功消息
            Timer timer = new Timer(1500, e -> {
                dispose();
                new MainFrame().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showError("❌ 用户名或密码错误，请重试");
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        }
    }

    private void handleLoginError(Exception e) {
        showError("💥 登录过程中发生错误：" + e.getMessage());
        e.printStackTrace();
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(SUCCESS_COLOR);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);
    }

    private void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        rememberPasswordCheckBox.setSelected(false);
        statusLabel.setText("📝 请输入用户名和密码");
        statusLabel.setForeground(TEXT_SECONDARY);
        usernameField.requestFocusInWindow();
        validateInput();
    }

    private void saveRememberPassword() {
        // 这里可以实现记住密码功能
        // 可以使用配置文件或数据库保存加密后的密码
        System.out.println("记住密码功能已启用");
    }

    private boolean LoginChecking(String username, String password) {
        try {
            Database db = new Database();
            db.connect();
            boolean isLogin = db.callVerifyUserCredentials(username, password);
            db.Close(); // 确保关闭数据库连接
            return isLogin;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void Show() {
        try {
            // 设置系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // 设置全局字体
            setUIFont(new FontUIResource("微软雅黑", Font.PLAIN, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void setUIFont(FontUIResource font) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
} 