package com.wms.database;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/db_school_wms";
    private static final String USER = "root";
    private static final String PASSWORD = "612278";

    public Connection conn = null;
    private Statement stmt;
    private ResultSet rs;

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet Sql(String sql){
        if(conn == null)
            return null;
        System.out.println("数据库连接成功！");

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void Close(){
        try {
            if(rs !=null){ rs.close(); }
            if(stmt !=null){ stmt.close(); }
            if(conn !=null){ conn.close(); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean callVerifyUserCredentials(String id, String pwd){
        String sql = "{CALL proc_VerifyUserCredentials(?, ?, ?)}";
        boolean result = false;
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            // 设置输入参数
            cstmt.setString(1, id);
            cstmt.setString(2, pwd);

            // 注册输出参数（MySQL中的BOOLEAN对应TINYINT）
            cstmt.registerOutParameter(3, Types.BOOLEAN);

            // 执行存储过程
            cstmt.execute();

            result = cstmt.getBoolean(3);
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String callGetCustomerInfo(int customerId){
        String sql = "{CALL proc_GetCustomerInfo(?, ?)}";
        String name = "NULL";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, customerId);
            cstmt.registerOutParameter(2, Types.VARCHAR);

            cstmt.execute();

            name = cstmt.getString(2);
        } catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }

    /// 当type为1时返回类型名称，为2时返回价格系数
    public String callGetLogisticsTypeInfo(int typeId, int type){
        String sql = "{CALL proc_GetLogisticsTypeInfo(?, ?, ?)}";
        String res = "NULL";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, typeId);
            cstmt.registerOutParameter(2, Types.VARCHAR);
            cstmt.registerOutParameter(3, Types.DECIMAL);

            cstmt.execute();

            if(type == 1){
                res = cstmt.getString(2);
            } else if(type == 2){
                res = String.valueOf(cstmt.getDouble(3));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public String callGetWorkerInfo(int workerId){
        String sql = "{CALL proc_GetWorkerInfo(?, ?)}";
        String name = "NULL";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, workerId);
            cstmt.registerOutParameter(2, Types.VARCHAR);

            cstmt.execute();

            name = cstmt.getString(2);
        } catch (Exception e){
            e.printStackTrace();
        }
        return name;
    }

    public Object[][] getOrdersInfo(){

        String sql = "SELECT order_id, tb_customers.customer_name, now_address, target_address,\n" +
                "        tb_types.type_name,tb_orders.weight, tb_orders.total_price,\n" +
                "        `status`, `date`\n" +
                "FROM tb_customers, tb_orders, tb_types\n" +
                "WHERE tb_orders.customer_id = tb_customers.customer_id\n" +
                "AND tb_orders.type_id = tb_types.type_id\n" +
                "ORDER BY order_id;";
        List<Object[]> rows = null;

        try{
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);

            // 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 使用List暂存结果
            rows = new ArrayList<>();

            // 遍历结果集
            while (rs.next()) {
                Object[] row = new Object[columnCount];

                // 填充行数据
                for (int i = 0; i < columnCount; i++) {
                    // 注意：JDBC列索引从1开始
                    row[i] = rs.getObject(i + 1);
                }

                rows.add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows != null ? rows.toArray(new Object[0][]) : null;
    }

    public Object[][] getOrdersInfo(int ordrId){
        String sql = "SELECT order_id, tb_customers.customer_id,\n" +
                "        target_address, tb_orders.weight,\n" +
                "        `status`, `date`\n" +
                "FROM tb_customers, tb_orders, tb_types\n" +
                "WHERE tb_orders.customer_id = tb_customers.customer_id\n" +
                "AND tb_orders.type_id = tb_types.type_id\n" +
                "AND order_id=" + ordrId + ";";
        List<Object[]> rows = null;

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 使用List暂存结果
            rows = new ArrayList<>();

            // 遍历结果集
            while (rs.next()) {
                Object[] row = new Object[columnCount];

                // 填充行数据
                for (int i = 0; i < columnCount; i++) {
                    // 注意：JDBC列索引从1开始
                    row[i] = rs.getObject(i + 1);
                }

                rows.add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows != null ? rows.toArray(new Object[0][]) : null;
    }

    public Object[][] selectOrdersInfo(int orderId){
        String sql = "SELECT order_id, now_address, tb_operators.`name`, tb_operators.operation_id, tb_operators.phone, \n" +
                "        `status`, DATE_ADD((SELECT `date` FROM tb_orders WHERE order_id="+ orderId + "),INTERVAL 3 DAY)\n" +
                "FROM tb_orders, tb_operators\n" +
                "WHERE tb_orders.operator_id=tb_operators.operation_id\n" +
                "AND order_id="+ orderId + ";";
        List<Object[]> rows = null;

        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 使用List暂存结果
            rows = new ArrayList<>();

            // 遍历结果集
            while (rs.next()) {
                Object[] row = new Object[columnCount];

                // 填充行数据
                for (int i = 0; i < columnCount; i++) {
                    // 注意：JDBC列索引从1开始
                    row[i] = rs.getObject(i + 1);
                }

                rows.add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows != null ? rows.toArray(new Object[0][]) : null;
    }

    public void deleteOrder(int orderId){
        String sql = "DELETE FROM tb_orders\n" +
                "WHERE tb_orders.order_id=" + orderId;
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.connect();

        Object res[][] = null;

        res = db.selectOrdersInfo(120);

        System.out.println(res[0][2].toString());

        db.Close();
    }
}
