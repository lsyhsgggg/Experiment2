package com.steamclone.listener;

import org.apache.commons.dbcp2.BasicDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class DBConnectionPoolListener implements ServletContextListener {

    private BasicDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {
            // 加载数据库配置
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            properties.load(inputStream);
            inputStream.close();

            // 创建连接池
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

            // 将数据源存储在上下文中
            context.setAttribute("dataSource", dataSource);
            context.log("数据库连接池已初始化");

        } catch (IOException e) {
            e.printStackTrace();
            context.log("数据库连接池初始化失败: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            // 关闭数据源
            if (dataSource != null) {
                dataSource.close();
                sce.getServletContext().log("数据库连接池已关闭");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sce.getServletContext().log("数据库连接池关闭失败: " + e.getMessage());
        }
    }
}