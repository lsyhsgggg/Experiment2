package com.steamclone.util;

import org.apache.commons.dbcp2.BasicDataSource;
import jakarta.servlet.ServletContext;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBUtil {
    private static BasicDataSource dataSource;

    static {
        try {
            Properties properties = new Properties();
            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);
            inputStream.close();

            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(properties.getProperty("db.driver"));
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));

            // 设置连接池参数
            dataSource.setInitialSize(Integer.parseInt(properties.getProperty("db.initialSize")));
            dataSource.setMaxTotal(Integer.parseInt(properties.getProperty("db.maxTotal")));
            dataSource.setMaxIdle(Integer.parseInt(properties.getProperty("db.maxIdle")));
            dataSource.setMinIdle(Integer.parseInt(properties.getProperty("db.minIdle")));
            dataSource.setMaxWaitMillis(Long.parseLong(properties.getProperty("db.maxWaitMillis")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 关闭资源
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}