package com.wms.views.order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditOrderFrame extends JFrame {
    public EditOrderFrame() {
        super("编辑订单");
        // 设置窗口大小
        setSize(500, 350);
        // 设置关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板（上下布局）
        JPanel mainPanel = new JPanel(new BorderLayout());

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

    private JPanel createFormPanel() {
        // 创建6行2列的面板，行间距5像素
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加标签和文本框
        addLabelAndField(panel, "订单编号：");
        addLabelAndField(panel, "客户名称：");
        addLabelAndField(panel, "目的地：");
        addLabelAndField(panel, "总价：");
        addLabelAndField(panel, "订单状态：");
        addLabelAndField(panel, "订单创建时间：");

        return panel;
    }

    private void addLabelAndField(JPanel panel, String labelText) {
        // 创建标签（右对齐）
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        panel.add(label);

        // 创建文本框（20列宽度）
        JTextField textField = new JTextField(120);
        panel.add(textField);
    }

    private JPanel createButtonPanel() {
        // 创建按钮面板（流式布局居中）
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());

        // 创建保存按钮
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "数据已保存！");
            }
        });

        // 创建取消按钮
        JButton cancelButton = new JButton("恢复·");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // 添加按钮到面板
        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    public void Show(){
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
                new EditOrderFrame().Show();
            }
        });
    }
}
