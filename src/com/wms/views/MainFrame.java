package com.wms.views;

import com.wms.views.order.NewOrderFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel orderPanel;
    private JPanel trackingPanel;
    private JPanel customerPanel;
    private JPanel statisticsPanel;

    public MainFrame() {
        setTitle("物流管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // 创建菜单栏
        createMenuBar();

        // 创建主面板
        tabbedPane = new JTabbedPane();
        
        // 创建各个功能面板
        createOrderPanel();
        createTrackingPanel();
        createCustomerPanel();
        createStatisticsPanel();

        // 添加面板到选项卡
        tabbedPane.addTab("订单管理", new ImageIcon(), orderPanel, "管理物流订单");
        tabbedPane.addTab("货物追踪", new ImageIcon(), trackingPanel, "追踪货物状态");
        tabbedPane.addTab("客户管理", new ImageIcon(), customerPanel, "管理客户信息");
        tabbedPane.addTab("数据统计", new ImageIcon(), statisticsPanel, "查看统计数据");

        // 设置选项卡样式
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 添加选项卡面板到主窗口
        add(tabbedPane);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        JMenu helpMenu = getMenu();

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private JMenu getMenu() {
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        """
                                物流管理系统 v1.0
                                
                                本系统用于管理物流订单、追踪货物、管理客户信息和查看统计数据。""",
                    "关于",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutItem);
        return helpMenu;
    }

    private void createOrderPanel() {
        orderPanel = new JPanel(new BorderLayout());
        
        // 创建工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("新建订单");
        JButton editButton = new JButton("编辑订单");
        JButton deleteButton = new JButton("删除订单");

        addButton.addActionListener(e ->{
           new NewOrderFrame().setVisible(true);
        });
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        
        // 创建表格
        String[] columnNames = {"订单编号", "客户名称", "起始地", "目的地", "货物类型", "重量（kg）", "物流费用", "状态", "创建时间"};
        Object[][] data = {
            {"ORD001", "张三", "北京", "北京", "服装", "123", "123", "运输中", "2024-03-20"},
            {"ORD002", "李四", "上海", "北京", "易燃易爆", "123", "123", "已签收", "2024-03-19"},
            {"ORD003", "王五", "广州", "北京", "服装", "123", "123", "待发货", "2024-03-21"}
        };
        
        JTable orderTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        
        orderPanel.add(toolBar, BorderLayout.NORTH);
        orderPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createTrackingPanel() {
        trackingPanel = new JPanel(new BorderLayout());
        
        // 创建搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("订单编号："));
        searchPanel.add(new JTextField(20));
        searchPanel.add(new JButton("查询"));
        
        // 创建追踪信息面板
        JPanel trackingInfoPanel = new JPanel(new BorderLayout());
        trackingInfoPanel.setBorder(BorderFactory.createTitledBorder("追踪信息"));
        
        // 创建追踪状态面板
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statusPanel.add(new JLabel("订单状态：运输中"));
        statusPanel.add(new JLabel("当前位置：北京市朝阳区"));
        statusPanel.add(new JLabel("预计送达：2024-03-22"));
        statusPanel.add(new JLabel("配送员：张师傅 (13800138000)"));
        
        trackingInfoPanel.add(statusPanel, BorderLayout.CENTER);
        
        trackingPanel.add(searchPanel, BorderLayout.NORTH);
        trackingPanel.add(trackingInfoPanel, BorderLayout.CENTER);
    }

    private void createCustomerPanel() {
        customerPanel = new JPanel(new BorderLayout());
        
        // 创建工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton addButton = new JButton("添加客户");
        JButton editButton = new JButton("编辑客户");
        JButton deleteButton = new JButton("删除客户");
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        
        // 创建表格
        String[] columnNames = {"客户编号", "客户名称", "联系人", "电话", "地址", "最后活跃时间"};
        Object[][] data = {
            {"CUS001", "张三公司", "张三", "13800138001", "北京市朝阳区", "2024-01-01"},
            {"CUS002", "李四贸易", "李四", "13800138002", "上海市浦东新区", "2024-02-01"},
            {"CUS003", "王五物流", "王五", "13800138003", "广州市天河区", "2024-03-01"}
        };
        
        JTable customerTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        
        customerPanel.add(toolBar, BorderLayout.NORTH);
        customerPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createStatisticsPanel() {
        statisticsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statisticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建统计卡片
        JPanel orderStatsPanel = createStatCard("订单统计", "本月订单数：150", "待处理：20", "运输中：80", "已完成：50");
        JPanel revenueStatsPanel = createStatCard("收入统计", "本月收入：¥50,000", "同比增长：15%", "平均订单金额：¥333", "最高订单金额：¥2,000");
        JPanel customerStatsPanel = createStatCard("客户统计", "总客户数：500", "本月新增：30", "活跃客户：200", "VIP客户：50");
        JPanel deliveryStatsPanel = createStatCard("配送统计", "平均配送时间：2天", "准时率：95%", "配送员数量：20", "覆盖城市：30");
        
        statisticsPanel.add(orderStatsPanel);
        statisticsPanel.add(revenueStatsPanel);
        statisticsPanel.add(customerStatsPanel);
        statisticsPanel.add(deliveryStatsPanel);
    }

    private JPanel createStatCard(String title, String... stats) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        for (String stat : stats) {
            panel.add(new JLabel(stat));
            panel.add(Box.createVerticalStrut(5));
        }
        
        return panel;
    }

    public static void main(String[] args) {
        // 设置界面外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动主界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
} 