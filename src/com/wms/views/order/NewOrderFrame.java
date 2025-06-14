package com.wms.views.order;

import javax.swing.*;
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

    private java.util.List<JLabel> labelList = new ArrayList<JLabel>();
    private ListIterator<JLabel> labelListIterator;

    private java.util.List<JTextField> textFieldList = new ArrayList<JTextField>();
    private ListIterator<JTextField> textFieldListIterator;

    public NewOrderFrame() {
        super("创建订单");
        // 设置窗口大小
        setSize(500, 400);

        // 创建主面板（上下布局）
        mainPanel = new JPanel(new BorderLayout());

        // 创建上半部分（表单区域）
        formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 创建下半部分（按钮区域）
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加主面板到窗口
        add(mainPanel);

        // 居中显示窗口
        setLocationRelativeTo(null);

        db.connect();
    }

    private JPanel createFormPanel() {
        // 创建6行2列的面板，行间距5像素
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加标签和文本框
        addLabelAndField(panel, "客户编号：");//0
        addLabelAndField(panel, "出发地：");//1
        addLabelAndField(panel, "目的地：");//2
        addLabelAndField(panel, "物流类型编号：");//3
        addLabelAndField(panel, "重量（kg）：");//4
        addLabelAndField(panel, "物流工人编号：");//5

        labelListIterator = labelList.listIterator();
        textFieldListIterator = textFieldList.listIterator();

        return panel;
    }

    private void addLabelAndField(JPanel panel, String labelText) {
        // 创建标签（右对齐）
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        panel.add(label);

        // 创建文本框（20列宽度）
        JTextField textField = new JTextField(120);
        panel.add(textField);

        labelList.add(label);
        textFieldList.add(textField);
    }

    private JPanel createButtonPanel() {
        // 创建按钮面板（流式布局居中）
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());

        // 创建保存按钮
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            /*TODO 保存数据字段：
               订单编号（自增）、客户编号（未找到客户会报错）、
               当前位置、出发地、目的地、重量、
               物流类型编号（未找到物流类型会报错）、
               物流费用（需计算）、物流工人编号
            */
            if(isNULL()){
                JOptionPane.showMessageDialog(this, "数据不能为空！");
                return;
            }
            SaveData();
        });

        // 创建取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e ->{
            this.dispose();
        });

        // 添加按钮到面板
        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    //是否有问门框为空
    private boolean isNULL() {
        while (textFieldListIterator.hasNext()) {
            if(textFieldListIterator.next().getText().isEmpty()){
                return true;
            }
        }
        return false;
    }

    private boolean SaveData(){
        int customerId = Integer.parseInt(textFieldList.get(0).getText());
        String nowLocation = textFieldList.get(1).getText();
        String targetLocation = textFieldList.get(2).getText();
        int type = Integer.parseInt(textFieldList.get(3).getText());
        double weight = Double.parseDouble(textFieldList.get(4).getText());
        int workId = Integer.parseInt(textFieldList.get(5).getText());
        double price;//物流费用（需计算）

        //客户编号验证
        if(Objects.equals(db.callGetCustomerInfo(customerId), "NULL")){
            JOptionPane.showMessageDialog(this, "客户不存在！");
            return false;
        }

        //物流类型编号验证
        if(Objects.equals(db.callGetLogisticsTypeInfo(type, 1), "NULL")){
            JOptionPane.showMessageDialog(this, "物流类型不存在！");
            return false;
        }

        //物流工人编号验证
        if(Objects.equals(db.callGetWorkerInfo(workId), "NULL")){
            JOptionPane.showMessageDialog(this, "客户不存在！");
            return false;
        }

        //物流费用=价格系数*重量
        price = Double.parseDouble(db.callGetLogisticsTypeInfo(type, 2))
                * weight;

        // 保存订单到数据库
        try {
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
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "订单保存成功！");
            } else {
                JOptionPane.showMessageDialog(this, "订单保存失败！");
                return false;
            }
            pstmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库操作异常：" + ex.getMessage());
            return false;
        }

        return true;
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
                new NewOrderFrame().Show();
            }
        });
    }
}
