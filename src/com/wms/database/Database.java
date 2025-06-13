package com.wms.database;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        String sql = "{CALL GetCustomerInfo(?, ?)}";
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
        String sql = "{CALL GetLogisticsTypeInfo(?, ?, ?)}";
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

    public static void main(String[] args) {
        Database db = new Database();
        db.connect();

        boolean is_vaild = db.callVerifyUserCredentials("111", "123456");

        System.out.println(is_vaild);

        db.Close();
    }
}
