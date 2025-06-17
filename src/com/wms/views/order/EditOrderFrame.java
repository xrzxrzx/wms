package com.wms.views.order;

import com.wms.database.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditOrderFrame extends JFrame {
    private JTextField orderIdField;
    private JTextField customerNameField;
    private JTextField destinationField;
    private JTextField totalPriceField;
    private JComboBox<String> statusComboBox;
    private JTextField createTimeField;

    private Database db;
    private Object[][] data = null;

    private int orderId;

    public EditOrderFrame(int orderId) {
        super("编辑订单");
        // 设置窗口大小
        setSize(750, 600);
        // 设置关闭操作
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.orderId = orderId;

        // 设置窗口图标
        setIconImage(createWindowIcon());

        db = new Database();
        db.connect();

        // 创建主面板（上下布局）
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // 创建渐变背景
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(248, 250, 252),
                        0, getHeight(), new Color(241, 245, 249)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // 创建上半部分（表单区域）
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 创建下半部分（按钮区域）
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加主面板到窗口
        add(mainPanel);

        // 居中显示窗口
        setLocationRelativeTo(null);
    }

    private Image createWindowIcon() {
        // 创建一个简单的图标
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制订单图标
        g2d.setColor(new Color(59, 130, 246));
        g2d.fillRoundRect(4, 4, 24, 24, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("订", 12, 20);

        g2d.dispose();
        return icon;
    }

    private JPanel createFormPanel() {
        data = db.getOrdersInfo(orderId);

        // 创建带标题边框的面板
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制圆角背景
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // 绘制阴影效果
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);

                g2d.dispose();
            }
        };

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(25, 15, 15, 15), // 增加顶部边距，为标题留出空间
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(),
                        "📋 订单信息",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("微软雅黑", Font.BOLD, 16),
                        new Color(59, 130, 246)
                )
        ));

        // 使用GridBagLayout进行更灵活的布局
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 订单编号
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0; // 标签列不拉伸
        gbc.gridwidth = 1;
        panel.add(createLabel("🔢 订单编号："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // 文本框列拉伸
        gbc.gridwidth = 1;
        orderIdField = createTextField();
        orderIdField.setEditable(false);

        orderIdField.setText(String.valueOf(orderId));

        panel.add(orderIdField, gbc);

        // 客户名称
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(createLabel("👤 客户编号："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        customerNameField = createTextField();

        customerNameField.setText(data[0][1].toString());

        panel.add(customerNameField, gbc);

        // 目的地
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(createLabel("📍 目的地："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        destinationField = createTextField();

        destinationField.setText(data[0][2].toString());

        panel.add(destinationField, gbc);

        // 重量
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(createLabel("💰 重量："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        totalPriceField = createTextField();

        totalPriceField.setText(data[0][3].toString());

        panel.add(totalPriceField, gbc);

        // 订单状态
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(createLabel("📊 订单状态："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        statusComboBox = createStatusComboBox();
        panel.add(statusComboBox, gbc);

        // 订单创建时间
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        panel.add(createLabel("🕒 订单创建时间："), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 确保水平填充
        createTimeField = createTextField();
        createTimeField.setEditable(false); // 创建时间不可编辑
        createTimeField.setBackground(new Color(248, 250, 252));
        createTimeField.setPreferredSize(new Dimension(250, 30)); // 增加最小尺寸

        createTimeField.setText(data[0][5].toString());

        panel.add(createTimeField, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(new Color(55, 65, 81));
        label.setPreferredSize(new Dimension(150, 25)); // 设置标签的最小宽度
        label.setMinimumSize(new Dimension(150, 25));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                            0, 0, getWidth(), getHeight()));
                    g2d.dispose();
                }
                super.paintComponent(g);
            }
        };
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField.setBorder((Border) new RoundedCornerBorder());
        textField.setOpaque(false);
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private JComboBox<String> createStatusComboBox() {
        String[] statuses = {"待处理", "处理中", "已完成", "已取消"};
        JComboBox<String> comboBox = new JComboBox<>(statuses) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                            0, 0, getWidth(), getHeight()));
                    g2d.dispose();
                }
                super.paintComponent(g);
            }
        };
        comboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        comboBox.setBorder((Border) new RoundedCornerBorder());
        comboBox.setOpaque(false);
        comboBox.setBackground(Color.WHITE);

        switch (data[0][4].toString()) {
            case "待处理":
                comboBox.setSelectedIndex(0);
                break;
            case "处理中":
                comboBox.setSelectedIndex(1);
                break;
            case "已完成":
                comboBox.setSelectedIndex(2);
                break;
            case "已取消":
                comboBox.setSelectedIndex(3);
                break;
        }

        return comboBox;
    }

    private JPanel createButtonPanel() {
        // 创建按钮面板（流式布局居中）
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.setOpaque(false);

        // 创建保存按钮
        JButton saveButton = createStyledButton("💾 保存", new Color(34, 197, 94), new Color(22, 163, 74));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    showSuccessMessage();
                }
            }
        });

        // 创建取消按钮
        JButton cancelButton = createStyledButton("❌ 取消", new Color(239, 68, 68), new Color(220, 38, 38));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭窗口而不是退出程序
            }
        });

        // 添加按钮到面板
        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(hoverColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(hoverColor);
                } else {
                    g2d.setColor(backgroundColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // 绘制文字
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void showSuccessMessage() {
        JDialog dialog = new JDialog(this, "保存成功", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 253, 244));

        JLabel iconLabel = new JLabel("✅", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setForeground(new Color(34, 197, 94));

        JLabel messageLabel = new JLabel("订单信息已保存！", SwingConstants.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        messageLabel.setForeground(new Color(55, 65, 81));

        JButton okButton = new JButton("确定");
        okButton.addActionListener(e -> dialog.dispose());
        okButton.setBackground(new Color(34, 197, 94));
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(messageLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private boolean validateForm() {
        if (orderIdField.getText().trim().isEmpty()) {
            showErrorMessage("请输入订单编号", orderIdField);
            return false;
        }
        if (customerNameField.getText().trim().isEmpty()) {
            showErrorMessage("请输入客户编号", customerNameField);
            return false;
        }
        if (destinationField.getText().trim().isEmpty()) {
            showErrorMessage("请输入目的地", destinationField);
            return false;
        }
        if (totalPriceField.getText().trim().isEmpty()) {
            showErrorMessage("请输入重量", totalPriceField);
            return false;
        }

        // 保存订单到数据库
        int rows = 0;
        try {
            String sql = "UPDATE tb_orders " +
                    "SET customer_id = ?, " +
                    "    target_address = ?, " +
                    "    weight = ?, " +
                    "    `status` = ? " +  // 反引号转义关键字status
                    "WHERE order_id = ?";

            try{
                 PreparedStatement pstmt = db.conn.prepareStatement(sql);

                // 设置参数（注意索引顺序）
                pstmt.setInt(1, Integer.parseInt(customerNameField.getText()));                // customer_id
                pstmt.setString(2, destinationField.getText());            // target_address
                pstmt.setDouble(3, Double.parseDouble(totalPriceField.getText()));             // weight
                pstmt.setString(4, statusComboBox.getSelectedItem().toString());            // status
                pstmt.setInt(5, orderId);                // order_id

                // 执行更新
                rows = pstmt.executeUpdate();
                System.out.println(rows + " 行被更新");

                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (rows > 0) {
                return true;
            } else {
                showErrorMessage("保存失败", new JTextField("订单保存失败！请重试。"));
                return false;
            }
        } catch (NumberFormatException ex) {
            showErrorMessage("输入错误", new JTextField("请确保客户编号、物流类型编号、重量和工人编号都是有效的数字！"));
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMessage("数据库错误", new JTextField("数据库操作异常：" + ex.getMessage()));
            return false;
        }
    }

    private void showErrorMessage(String message, JTextField field) {
        JOptionPane.showMessageDialog(this, message, "验证失败", JOptionPane.WARNING_MESSAGE);
        field.requestFocus();
        field.setBorder((Border) new RoundedCornerBorder(new Color(239, 68, 68)));
    }

    // 圆角边框类
    static class RoundedCornerBorder extends AbstractBorder {
        private Color borderColor;

        public RoundedCornerBorder() {
            this(new Color(209, 213, 219));
        }

        public RoundedCornerBorder(Color color) {
            this.borderColor = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.draw(getBorderShape(x, y, width - 1, height - 1));
            g2d.dispose();
        }

        public Shape getBorderShape(int x, int y, int w, int h) {
            return new RoundRectangle2D.Double(x, y, w, h, 10, 10);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = 8;
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public void Show() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        // 使用事件调度线程创建UI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditOrderFrame(120).Show();
            }
        });
    }
}
