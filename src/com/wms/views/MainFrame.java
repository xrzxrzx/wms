package com.wms.views;

import com.wms.database.Database;
import com.wms.views.order.EditOrderFrame;
import com.wms.views.order.NewOrderFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {
    // 常量定义
    private static final String APP_TITLE = "物流管理系统 v1.0";
    private static final String APP_VERSION = "v1.0";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final Font TITLE_FONT = new Font("微软雅黑", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("微软雅黑", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("微软雅黑", Font.PLAIN, 12);
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    
    // UI组件
    private JTabbedPane tabbedPane;
    private JPanel orderPanel;
    private JPanel trackingPanel;
    private JPanel customerPanel;
    private JPanel statisticsPanel;
    private DefaultTableModel orderTableModel;
    private DefaultTableModel customerTableModel;
    private JTable orderTable;
    private JTable customerTable;
    private JTextField trackingSearchField;
    private JLabel trackingStatusLabel;
    private JLabel trackingLocationLabel;
    private JLabel trackingDeliveryLabel;
    private JLabel trackingCourierLabel;

    private Database db;

    public MainFrame() {
        initializeFrame();
        initializeDatabase();
        createMenuBar();
        createMainContent();
        applyStyles();
    }

    private void initializeFrame() {
        setTitle(APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    private void initializeDatabase() {
        db = new Database();
        db.connect();
    }

    private void createMainContent() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(NORMAL_FONT);
        
        createOrderPanel();
        createTrackingPanel();
        createCustomerPanel();
        createStatisticsPanel();

        // 添加选项卡，使用更现代的图标
        tabbedPane.addTab("订单管理", createTabIcon("📋"), orderPanel, "管理物流订单");
        tabbedPane.addTab("货物追踪", createTabIcon("🚚"), trackingPanel, "追踪货物状态");
        tabbedPane.addTab("客户管理", createTabIcon("👥"), customerPanel, "管理客户信息");
        tabbedPane.addTab("数据统计", createTabIcon("📊"), statisticsPanel, "查看统计数据");

        add(tabbedPane);
    }

    private Icon createTabIcon(String emoji) {
        // 创建简单的文本图标
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
                g2d.setColor(PRIMARY_COLOR);
                g2d.drawString(emoji, x, y + 16);
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 20;
            }

            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BACKGROUND_COLOR);
        
        // 文件菜单
        JMenu fileMenu = createStyledMenu("文件");
        JMenuItem exitItem = createStyledMenuItem("退出", "退出应用程序");
        exitItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "确定要退出应用程序吗？", "退出确认", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        // 帮助菜单
        JMenu helpMenu = createStyledMenu("帮助");
        JMenuItem aboutItem = createStyledMenuItem("关于", "关于本系统");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private JMenu createStyledMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setFont(NORMAL_FONT);
        return menu;
    }

    private JMenuItem createStyledMenuItem(String title, String tooltip) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(NORMAL_FONT);
        item.setToolTipText(tooltip);
        return item;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            String.format("""
                %s %s
                
                本系统用于管理物流订单、追踪货物、管理客户信息和查看统计数据。
                
                功能特点：
                • 订单管理 - 创建、编辑、删除订单
                • 货物追踪 - 实时追踪货物状态
                • 客户管理 - 管理客户信息
                • 数据统计 - 查看业务统计数据
                
                技术支持：物流管理系统开发团队""", APP_TITLE, APP_VERSION),
            "关于",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void createOrderPanel() {
        orderPanel = new JPanel(new BorderLayout(10, 10));
        orderPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        orderPanel.setBackground(BACKGROUND_COLOR);
        
        // 创建工具栏
        JToolBar toolBar = createStyledToolBar();
        
        JButton addButton = createStyledButton("➕ 新建订单", "创建新的物流订单", PRIMARY_COLOR);
        JButton editButton = createStyledButton("✏️ 编辑订单", "编辑选中的订单", SECONDARY_COLOR);
        JButton deleteButton = createStyledButton("🗑️ 删除订单", "删除选中的订单", WARNING_COLOR);
        JButton refreshButton = createStyledButton("🔄 刷新", "刷新订单列表", PRIMARY_COLOR);

        addButton.addActionListener(e -> new NewOrderFrame().Show());
        editButton.addActionListener(e -> new EditOrderFrame(orderTable).Show());
        deleteButton.addActionListener(e -> deleteOrder(orderTable));
        refreshButton.addActionListener(e -> updateOrdersInfo());
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // 创建表格
        createOrderTable();
        
        orderPanel.add(toolBar, BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
    }

    private JToolBar createStyledToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(CARD_COLOR);
        toolBar.setBorder(BorderFactory.createEtchedBorder());
        return toolBar;
    }

    private JButton createStyledButton(String text, String tooltip, Color color) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        button.setToolTipText(tooltip);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 添加鼠标悬停和按下效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(color.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(color);
                }
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (button.contains(evt.getPoint())) {
                    button.setBackground(color.brighter());
                } else {
                    button.setBackground(color);
                }
            }
        });
        
        return button;
    }

    private void createOrderTable() {
        String[] columnNames = {"订单编号", "客户名称", "当前位置", "目的地", "货物类型", "重量(kg)", "物流费用(¥)", "状态", "创建时间"};
        
        orderTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };

        orderTable = new JTable(orderTableModel);
        orderTable.setFont(SMALL_FONT);
        orderTable.setRowHeight(25);
        orderTable.setSelectionBackground(PRIMARY_COLOR);
        orderTable.setSelectionForeground(Color.WHITE);
        orderTable.setGridColor(new Color(200, 200, 200));
        orderTable.setShowGrid(true);
        orderTable.setIntercellSpacing(new Dimension(1, 1));
        
        // 添加排序功能
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(orderTableModel);
        orderTable.setRowSorter(sorter);
        
        // 设置列宽
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(8).setPreferredWidth(120);

        updateOrdersInfo();
    }


    private void deleteOrder(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // 转换为模型索引
            int modelRow = table.convertRowIndexToModel(selectedRow);
        }

        int modelRow = orderTable.convertRowIndexToModel(selectedRow);
        String orderId = orderTable.getValueAt(modelRow, 0).toString();

        int result = JOptionPane.showConfirmDialog(this, 
            "确定要删除订单 " + orderId + " 吗？", "删除确认", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            try {
                db.deleteOrder(Integer.parseInt(orderId));
                updateOrdersInfo();
                JOptionPane.showMessageDialog(this, "订单删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "删除失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateOrdersInfo() {
        orderTableModel.setRowCount(0);
        try {
            Object[][] data = db.getOrdersInfo();
            if (data != null) {
                for (Object[] rowData : data) {
                    orderTableModel.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "获取订单信息失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTrackingPanel() {
        trackingPanel = new JPanel(new BorderLayout(10, 10));
        trackingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        trackingPanel.setBackground(BACKGROUND_COLOR);
        
        // 创建搜索面板
        JPanel searchPanel = createSearchPanel();
        
        // 创建追踪信息面板
        JPanel trackingInfoPanel = createTrackingInfoPanel();
        
        trackingPanel.add(searchPanel, BorderLayout.NORTH);
        trackingPanel.add(trackingInfoPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.setBorder(BorderFactory.createTitledBorder("订单查询"));
        
        JLabel searchLabel = new JLabel("订单编号：");
        searchLabel.setFont(NORMAL_FONT);
        
        trackingSearchField = new JTextField(20);
        trackingSearchField.setFont(NORMAL_FONT);
        
        JButton searchButton = createStyledButton("🔍 查询", "查询订单追踪信息", PRIMARY_COLOR);
        searchButton.addActionListener(e -> performTrackingSearch());
        
        searchPanel.add(searchLabel);
        searchPanel.add(trackingSearchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }

    private JPanel createTrackingInfoPanel() {
        JPanel trackingInfoPanel = new JPanel(new BorderLayout(10, 10));
        trackingInfoPanel.setBackground(CARD_COLOR);
        trackingInfoPanel.setBorder(BorderFactory.createTitledBorder("追踪信息"));
        
        // 创建追踪状态面板
        JPanel statusPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statusPanel.setBackground(CARD_COLOR);
        statusPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        trackingStatusLabel = createInfoLabel("订单状态：待查询");
        trackingLocationLabel = createInfoLabel("当前位置：待查询");
        trackingDeliveryLabel = createInfoLabel("预计送达：待查询");
        trackingCourierLabel = createInfoLabel("配送员：待查询");
        
        statusPanel.add(trackingStatusLabel);
        statusPanel.add(trackingLocationLabel);
        statusPanel.add(trackingDeliveryLabel);
        statusPanel.add(trackingCourierLabel);
        
        trackingInfoPanel.add(statusPanel, BorderLayout.CENTER);
        
        return trackingInfoPanel;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        label.setBackground(new Color(240, 240, 240));
        label.setOpaque(true);
        return label;
    }

    private void performTrackingSearch() {
        String orderId = trackingSearchField.getText().trim();
        if (orderId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入订单编号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 模拟查询结果
        trackingStatusLabel.setText("订单状态：运输中");
        trackingLocationLabel.setText("当前位置：北京市朝阳区物流中心");
        trackingDeliveryLabel.setText("预计送达：2024-03-22 14:30");
        trackingCourierLabel.setText("配送员：张师傅 (13800138000)");
    }

    private void createCustomerPanel() {
        customerPanel = new JPanel(new BorderLayout(10, 10));
        customerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        customerPanel.setBackground(BACKGROUND_COLOR);
        
        // 创建工具栏
        JToolBar toolBar = createStyledToolBar();
        
        JButton addButton = createStyledButton("➕ 添加客户", "添加新客户", PRIMARY_COLOR);
        JButton editButton = createStyledButton("✏️ 编辑客户", "编辑选中的客户", SECONDARY_COLOR);
        JButton deleteButton = createStyledButton("🗑️ 删除客户", "删除选中的客户", WARNING_COLOR);
        JButton refreshButton = createStyledButton("🔄 刷新", "刷新客户列表", PRIMARY_COLOR);
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        
        // 创建客户表格
        createCustomerTable();
        
        customerPanel.add(toolBar, BorderLayout.NORTH);
        customerPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
    }

    private void createCustomerTable() {
        String[] columnNames = {"客户编号", "客户名称", "联系人", "电话", "地址", "最后活跃时间"};
        
        customerTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(customerTableModel);
        customerTable.setFont(SMALL_FONT);
        customerTable.setRowHeight(25);
        customerTable.setSelectionBackground(PRIMARY_COLOR);
        customerTable.setSelectionForeground(Color.WHITE);
        customerTable.setGridColor(new Color(200, 200, 200));
        customerTable.setShowGrid(true);
        customerTable.setIntercellSpacing(new Dimension(1, 1));
        
        // 添加排序功能
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(customerTableModel);
        customerTable.setRowSorter(sorter);
        
        // 设置列宽
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        // 添加示例数据
        addSampleCustomerData();
    }

    private void addSampleCustomerData() {
        Object[][] data = {
            {"CUS001", "张三公司", "张三", "13800138001", "北京市朝阳区建国路88号", "2024-01-15"},
            {"CUS002", "李四贸易", "李四", "13800138002", "上海市浦东新区陆家嘴金融中心", "2024-02-20"},
            {"CUS003", "王五物流", "王五", "13800138003", "广州市天河区珠江新城", "2024-03-10"},
            {"CUS004", "赵六电商", "赵六", "13800138004", "深圳市南山区科技园", "2024-03-18"},
            {"CUS005", "钱七制造", "钱七", "13800138005", "杭州市西湖区文三路", "2024-03-19"}
        };
        
        for (Object[] rowData : data) {
            customerTableModel.addRow(rowData);
        }
    }

    private void createStatisticsPanel() {
        statisticsPanel = new JPanel(new BorderLayout(15, 15));
        statisticsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        statisticsPanel.setBackground(BACKGROUND_COLOR);
        
        // 创建顶部标题面板
        JPanel titlePanel = createTitlePanel();
        
        // 创建主要内容面板
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // 创建统计卡片
        JPanel orderStatsPanel = createEnhancedStatCard("📋 订单统计", 
            new String[]{"本月订单数", "待处理", "运输中", "已完成"},
            new String[]{"150", "20", "80", "50"},
            new Color[]{PRIMARY_COLOR, new Color(255, 193, 7), SECONDARY_COLOR, new Color(108, 117, 125)});
            
        JPanel revenueStatsPanel = createEnhancedStatCard("💰 收入统计", 
            new String[]{"本月收入", "平均订单金额", "最高订单金额", "同比增长"},
            new String[]{"¥50,000", "¥333", "¥2,000", "+15.2%"},
            new Color[]{new Color(40, 167, 69), new Color(23, 162, 184), new Color(255, 193, 7), new Color(220, 53, 69)});
            
        JPanel customerStatsPanel = createEnhancedStatCard("👥 客户统计", 
            new String[]{"总客户数", "本月新增", "活跃客户", "VIP客户"},
            new String[]{"500", "30", "200", "50"},
            new Color[]{PRIMARY_COLOR, SECONDARY_COLOR, new Color(255, 193, 7), new Color(220, 53, 69)});
            
        JPanel deliveryStatsPanel = createEnhancedStatCard("🚚 配送统计", 
            new String[]{"配送员数量", "覆盖城市", "准时率"},
            new String[]{"20", "30", "98.5%"},
            new Color[]{SECONDARY_COLOR, PRIMARY_COLOR, new Color(40, 167, 69)});
        
        contentPanel.add(orderStatsPanel);
        contentPanel.add(revenueStatsPanel);
        contentPanel.add(customerStatsPanel);
        contentPanel.add(deliveryStatsPanel);
        
        // 创建底部趋势面板
        JPanel trendPanel = createTrendPanel();
        
        statisticsPanel.add(titlePanel, BorderLayout.NORTH);
        statisticsPanel.add(contentPanel, BorderLayout.CENTER);
        statisticsPanel.add(trendPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(CARD_COLOR);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel titleLabel = new JLabel("📊 数据统计概览");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("实时业务数据监控");
        subtitleLabel.setFont(SMALL_FONT);
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 0));
        leftPanel.setBackground(CARD_COLOR);
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // 添加刷新按钮
        JButton refreshButton = new JButton("🔄 刷新数据");
        refreshButton.setFont(NORMAL_FONT);
        refreshButton.setToolTipText("刷新统计数据");
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(120, 35));
        
        refreshButton.addActionListener(e -> refreshStatistics());
        
        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(refreshButton, BorderLayout.EAST);
        
        return titlePanel;
    }

    private JPanel createEnhancedStatCard(String title, String[] labels, String[] values, Color[] colors) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                NORMAL_FONT,
                PRIMARY_COLOR
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // 添加阴影效果
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 2, 4, 4),
            panel.getBorder()
        ));
        
        for (int i = 0; i < labels.length; i++) {
            JPanel itemPanel = createStatItem(labels[i], values[i], colors[i]);
            panel.add(itemPanel);
            if (i < labels.length - 1) {
                panel.add(Box.createVerticalStrut(12));
            }
        }
        
        // 添加鼠标悬停效果
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(2, 2, 4, 4),
                    BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR.brighter(), 3),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        NORMAL_FONT,
                        PRIMARY_COLOR.brighter()
                    )
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(2, 2, 4, 4),
                    BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        NORMAL_FONT,
                        PRIMARY_COLOR
                    )
                ));
            }
        });
        
        return panel;
    }

    private JPanel createStatItem(String label, String value, Color color) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBackground(CARD_COLOR);
        
        // 创建标签
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(SMALL_FONT);
        labelComponent.setForeground(new Color(108, 117, 125));
        
        // 创建数值
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("微软雅黑", Font.BOLD, 16));
        valueComponent.setForeground(color);
        valueComponent.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // 创建颜色指示器
        JPanel indicator = new JPanel();
        indicator.setPreferredSize(new Dimension(4, 20));
        indicator.setBackground(color);
        indicator.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        
        itemPanel.add(indicator, BorderLayout.WEST);
        itemPanel.add(labelComponent, BorderLayout.CENTER);
        itemPanel.add(valueComponent, BorderLayout.EAST);
        
        return itemPanel;
    }

    private JPanel createTrendPanel() {
        JPanel trendPanel = new JPanel(new BorderLayout(10, 0));
        trendPanel.setBackground(CARD_COLOR);
        trendPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // 标题
        JLabel trendTitle = new JLabel("📈 业务趋势");
        trendTitle.setFont(NORMAL_FONT);
        trendTitle.setForeground(PRIMARY_COLOR);
        
        // 趋势指标
        JPanel trendIndicators = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        trendIndicators.setBackground(CARD_COLOR);
        
        trendIndicators.add(createTrendIndicator("订单量", "+12.5%", new Color(40, 167, 69)));
        trendIndicators.add(createTrendIndicator("收入", "+8.3%", new Color(23, 162, 184)));
        trendIndicators.add(createTrendIndicator("客户", "+5.7%", new Color(255, 193, 7)));
        trendIndicators.add(createTrendIndicator("配送效率", "+2.1%", new Color(220, 53, 69)));
        
        trendPanel.add(trendTitle, BorderLayout.WEST);
        trendPanel.add(trendIndicators, BorderLayout.CENTER);
        
        return trendPanel;
    }

    private JPanel createTrendIndicator(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(CARD_COLOR);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(SMALL_FONT);
        labelComponent.setForeground(new Color(108, 117, 125));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("微软雅黑", Font.BOLD, 14));
        valueComponent.setForeground(color);
        
        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshStatistics() {
        // 模拟数据刷新
        JOptionPane.showMessageDialog(this, 
            "统计数据已刷新！\n\n最新数据更新时间：" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
            "刷新成功", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyStyles() {
        // 设置全局字体
        UIManager.put("Button.font", NORMAL_FONT);
        UIManager.put("Label.font", NORMAL_FONT);
        UIManager.put("TextField.font", NORMAL_FONT);
        UIManager.put("TextArea.font", NORMAL_FONT);
        UIManager.put("Table.font", SMALL_FONT);
        UIManager.put("TableHeader.font", NORMAL_FONT);
        
        // 设置颜色主题
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.background", BACKGROUND_COLOR);
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND_COLOR);
    }

    public static void main(String[] args) {
        // 设置界面外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动主界面
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
} 