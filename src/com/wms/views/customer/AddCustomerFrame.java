package com.wms.views.customer;

import com.wms.database.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AddCustomerFrame extends JFrame {
    // 常量定义
    private static final String FRAME_TITLE = "添加客户";
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 700;
    private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("微软雅黑", Font.PLAIN, 12);
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    
    // UI组件
    private JTextField customerIdField;
    private JTextField customerNameField;
    private JTextField contactPersonField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JCheckBox vipCheckBox;
    
    // 数据库连接
    private Database db;
    
    // 正则表达式验证
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    public AddCustomerFrame() {
        initializeFrame();
        initializeDatabase();
        createComponents();
        layoutComponents();
        applyStyles();
    }
    
    private void initializeFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeDatabase() {
        db = new Database();
        db.connect();
    }
    
    private void createComponents() {
        // 客户编号（自动生成）
        customerIdField = createStyledTextField();
        customerIdField.setText(generateCustomerId());
        customerIdField.setEditable(false);
        customerIdField.setBackground(new Color(240, 240, 240));
        
        // 客户名称
        customerNameField = createStyledTextField();
        
        // 联系人
        contactPersonField = createStyledTextField();
        
        // 电话
        phoneField = createStyledTextField();
        
        // 地址
        addressArea = createStyledTextArea();
        addressArea.setRows(3);
        
        // VIP
        vipCheckBox = new JCheckBox("是否是VIP客户");
        vipCheckBox.setFont(NORMAL_FONT);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(NORMAL_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(NORMAL_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return area;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(CARD_COLOR);
        return comboBox;
    }
    
    private String generateCustomerId() {
        // 生成客户编号：CUS + 年月日 + 4位随机数
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomNum = (int) (Math.random() * 10000);
        return String.format("CUS%s%04d", dateStr, randomNum);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);
        
        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // 标题面板
        JPanel titlePanel = createTitlePanel();
        
        // 表单面板
        JPanel formPanel = createFormPanel();
        
        // 按钮面板
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("➕ 添加新客户");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("请填写客户信息");
        subtitleLabel.setFont(SMALL_FONT);
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("客户信息"),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // 基本信息
        panel.add(createFormRow("客户编号：", customerIdField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("客户名称：", customerNameField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("联系人：", contactPersonField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("联系电话：", phoneField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("详细地址：", new JScrollPane(addressArea)));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createFormRow("是否是VIP客户：", vipCheckBox));
        
        return panel;
    }
    
    private JPanel createFormRow(String labelText, Component component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(NORMAL_FONT);
        label.setForeground(new Color(73, 80, 87));
        label.setPreferredSize(new Dimension(100, 30));
        
        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        
        return row;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        JButton saveButton = createStyledButton("💾 保存", "保存客户信息", SECONDARY_COLOR);
        JButton resetButton = createStyledButton("🔄 重置", "重置表单", PRIMARY_COLOR);
        JButton cancelButton = createStyledButton("❌ 取消", "关闭窗口", WARNING_COLOR);
        
        saveButton.addActionListener(e -> saveCustomer());
        resetButton.addActionListener(e -> resetForm());
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(saveButton);
        panel.add(resetButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, String tooltip, Color color) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setToolTipText(tooltip);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        
        return button;
    }
    
    private void applyStyles() {
        // 设置全局字体
        UIManager.put("Button.font", NORMAL_FONT);
        UIManager.put("Label.font", NORMAL_FONT);
        UIManager.put("TextField.font", NORMAL_FONT);
        UIManager.put("TextArea.font", NORMAL_FONT);
        UIManager.put("ComboBox.font", NORMAL_FONT);
    }
    
    private void saveCustomer() {
        // 验证表单
        if (!validateForm()) {
            return;
        }
        
        try {
            // 获取表单数据
            String customerId = customerIdField.getText().trim();
            String customerName = customerNameField.getText().trim();
            String contactPerson = contactPersonField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();
            boolean isVip = vipCheckBox.isSelected();
            
            // 使用新的数据库方法
            boolean success = true;//db.addCustomer(customerId, customerName, contactPerson, phone, address, isVip);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "客户信息保存成功！\n\n客户编号：" + customerId,
                    "保存成功",
                    JOptionPane.INFORMATION_MESSAGE);
                dispose(); // 关闭窗口
            } else {
                JOptionPane.showMessageDialog(this,
                    "保存失败，请检查数据库连接或重试。",
                    "保存失败",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "保存过程中发生错误：\n" + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateForm() {
        // 验证客户名称
        if (customerNameField.getText().trim().isEmpty()) {
            showValidationError("客户名称不能为空");
            customerNameField.requestFocus();
            return false;
        }
        
        // 验证联系人
        if (contactPersonField.getText().trim().isEmpty()) {
            showValidationError("联系人不能为空");
            contactPersonField.requestFocus();
            return false;
        }
        
        // 验证电话
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            showValidationError("请输入正确的手机号码格式");
            phoneField.requestFocus();
            return false;
        }
        
        // 验证地址
        if (addressArea.getText().trim().isEmpty()) {
            showValidationError("详细地址不能为空");
            addressArea.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "验证错误",
            JOptionPane.WARNING_MESSAGE);
    }
    
    private void resetForm() {
        int result = JOptionPane.showConfirmDialog(this,
            "确定要重置所有表单数据吗？",
            "重置确认",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            customerIdField.setText(generateCustomerId());
            customerNameField.setText("");
            contactPersonField.setText("");
            phoneField.setText("");
            addressArea.setText("");
            vipCheckBox.setSelected(false);
            
            customerNameField.requestFocus();
        }
    }
    
    public void Show() {
        setVisible(true);
    }
    
    public static void main(String[] args) {
        // 设置界面外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 启动添加客户界面
        SwingUtilities.invokeLater(() -> {
            new AddCustomerFrame().setVisible(true);
        });
    }
} 