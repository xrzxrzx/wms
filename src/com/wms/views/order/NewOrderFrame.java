package com.wms.views.order;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;

import com.wms.database.Database;

public class NewOrderFrame extends JFrame {
    Database db = new Database();

    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;

    private java.util.List<JLabel> labelList = new ArrayList<JLabel>();
    private ListIterator<JLabel> labelListIterator;

    private java.util.List<JTextField> textFieldList = new ArrayList<JTextField>();
    private ListIterator<JTextField> textFieldListIterator;

    // 定义颜色主题
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(230, 126, 34);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public NewOrderFrame() {
        super("创建新订单");
        initializeFrame();
        createUI();
        db.connect();
    }

    private void initializeFrame() {
        // 设置窗口大小和属性
        setSize(600, 600);
        setMinimumSize(new Dimension(500, 500));
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 设置窗口图标（如果有的话）
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/order_icon.png")));
        } catch (Exception e) {
            // 图标文件不存在时忽略
        }
    }

    private void createUI() {
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // 创建头部面板
        headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 创建内容面板
        contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建表单面板
        formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = createButtonPanel();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        // 居中显示窗口
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(600, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // 创建标题标签
        JLabel titleLabel = new JLabel("创建新订单", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        // 添加装饰线
        JPanel linePanel = new JPanel();
        linePanel.setBackground(SECONDARY_COLOR);
        linePanel.setPreferredSize(new Dimension(600, 2));
        panel.add(linePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFormPanel() {
        // 创建表单容器面板
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // 创建表单标题
        JLabel formTitleLabel = new JLabel("订单信息", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        formTitleLabel.setForeground(PRIMARY_COLOR);
        formTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        containerPanel.add(formTitleLabel, BorderLayout.NORTH);

        // 使用简单的GridLayout来确保所有字段都显示
        JPanel formContentPanel = new JPanel(new GridLayout(6, 2, 10, 8));
        formContentPanel.setBackground(Color.WHITE);

        // 添加表单字段
        String[] labels = {"客户编号：", "出发地：", "目的地：", "物流类型编号：", "重量（kg）：", "物流工人编号："};
        String[] placeholders = {"请输入客户编号", "请输入出发地", "请输入目的地", "请输入物流类型编号", "请输入重量", "请输入物流工人编号"};

        for (int i = 0; i < labels.length; i++) {
            // 标签
            JLabel label = new JLabel(labels[i], SwingConstants.RIGHT);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            label.setForeground(new Color(73, 80, 87));
            formContentPanel.add(label);
            labelList.add(label);

            // 文本框
            JTextField textField = createStyledTextField(placeholders[i]);
            formContentPanel.add(textField);
            textFieldList.add(textField);
        }

        containerPanel.add(formContentPanel, BorderLayout.CENTER);
        return containerPanel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        // 添加焦点监听器来改变边框颜色
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(2, 7, 2, 7)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(3, 8, 3, 8)
                ));
            }
        });

        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // 创建保存按钮
        JButton saveButton = createStyledButton("保存订单", SUCCESS_COLOR);
        saveButton.addActionListener(e -> {
            if(isNULL()){
                showErrorDialog("数据验证失败", "请确保所有字段都已填写完整！");
                return;
            }
            if(SaveData()) {
                showSuccessDialog("订单创建成功", "新订单已成功创建并保存到数据库！");
                clearForm();
            }
        });

        // 创建取消按钮
        JButton cancelButton = createStyledButton("取消", WARNING_COLOR);
        cancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                this, 
                "确定要取消创建订单吗？", 
                "确认取消", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });

        // 添加按钮到面板
        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            title, 
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccessDialog(String title, String message) {
        JOptionPane.showMessageDialog(
            this, 
            message, 
            title, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void clearForm() {
        for (JTextField textField : textFieldList) {
            textField.setText("");
        }
    }

    //是否有文本框为空
    private boolean isNULL() {
        textFieldListIterator = textFieldList.listIterator();
        while (textFieldListIterator.hasNext()) {
            if(textFieldListIterator.next().getText().trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    private boolean SaveData(){
        try {
            int customerId = Integer.parseInt(textFieldList.get(0).getText().trim());
            String nowLocation = textFieldList.get(1).getText().trim();
            String targetLocation = textFieldList.get(2).getText().trim();
            int type = Integer.parseInt(textFieldList.get(3).getText().trim());
            double weight = Double.parseDouble(textFieldList.get(4).getText().trim());
            int workId = Integer.parseInt(textFieldList.get(5).getText().trim());
            double price;//物流费用（需计算）

            //客户编号验证
            if(Objects.equals(db.callGetCustomerInfo(customerId), "NULL")){
                showErrorDialog("验证失败", "客户不存在！请检查客户编号。");
                return false;
            }

            //物流类型编号验证
            if(Objects.equals(db.callGetLogisticsTypeInfo(type, 1), "NULL")){
                showErrorDialog("验证失败", "物流类型不存在！请检查物流类型编号。");
                return false;
            }

            //物流工人编号验证
            if(Objects.equals(db.callGetWorkerInfo(workId), "NULL")){
                showErrorDialog("验证失败", "物流工人不存在！请检查工人编号。");
                return false;
            }

            // 重量验证
            if (weight <= 0) {
                showErrorDialog("验证失败", "重量必须大于0！");
                return false;
            }

            //物流费用=价格系数*重量
            price = Double.parseDouble(db.callGetLogisticsTypeInfo(type, 2)) * weight;

            // 保存订单到数据库
            String sql = "INSERT INTO tb_orders (customer_id, type_id, total_price, operator_id, target_address, now_address, status, date) VALUES (?, ?, ?, ?, ?, ?, ?, curdate())";
            java.sql.PreparedStatement pstmt = db.conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, type);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, workId);
            pstmt.setString(5, targetLocation);
            pstmt.setString(6, nowLocation);
            pstmt.setString(7, "待发货");
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            
            if (rows > 0) {
                return true;
            } else {
                showErrorDialog("保存失败", "订单保存失败！请重试。");
                return false;
            }
        } catch (NumberFormatException ex) {
            showErrorDialog("输入错误", "请确保客户编号、物流类型编号、重量和工人编号都是有效的数字！");
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorDialog("数据库错误", "数据库操作异常：" + ex.getMessage());
            return false;
        }
    }

    public void Show(){
        try {
            // 设置系统外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // 设置全局字体
            UIManager.put("Button.font", new Font("微软雅黑", Font.PLAIN, 12));
            UIManager.put("Label.font", new Font("微软雅黑", Font.PLAIN, 12));
            UIManager.put("TextField.font", new Font("微软雅黑", Font.PLAIN, 12));
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
                new NewOrderFrame().Show();
            }
        });
    }
}
