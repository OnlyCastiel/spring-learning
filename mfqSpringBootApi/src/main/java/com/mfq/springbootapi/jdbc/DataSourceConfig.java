package com.mfq.springbootapi.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * JDBC-SPI-打破双亲委派机制
 *
 * SPI（Service Provider Interface）：
 * 是Java提供的一套用来被第三方实现或者扩展的接口，它可以用来启用框架扩展和替换组件。
 * SPI的作用就是为这些被扩展的API寻找服务实现
 *
 * 核心代码：
 * ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
 * DriverManager 中static代码块，会执行上述代码逻辑，在初始化过程中便会执行
 * 通过SPI机制，ServiceLoader.load(Driver.class)；匹配应用及引入lib包中所有的 \META-INF\services\java.sql.Driver 文件
 * 获取对应的实现类，并同时通过线程上线文中的类加载器-通常为应用加载器（非默认的类加载机制） 加载对应的Drivcer实例；
 * 故而：对于java中的这些提供给第三方拓展的接口，自身也提供了一个工厂方法，返回对应的实例，而自身在类加载的过程中，需要加载子加载器才能加载的实际服务提供类；
 */
public class DataSourceConfig {


    public static void main(String[] args) {

        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/cloud_crm?useUnicode=true&characterEncoding=UTF8&useSSL=false";
        String userName = "root";
        String password = "123456";

        Connection conn= null;//连接
        PreparedStatement statement= null;//预编译语句
        ResultSet resultSet = null; //获取的结果集
        try {
            //当前线程加载器本身为应用加载器appClassLoader ,替换为父加载器ExtClassLoader后，将执行报错
            Thread.currentThread().setContextClassLoader(DataSourceConfig.class.getClassLoader().getParent());

            //Class.forName使用的是当前调用类的类加载器，默认机制--非线程上下文中的类加载器
            Class.forName(driverClassName);

            //读取用户包以及用户lib包下的 \META-INF\services\java.sql.Driver 文件，其中定义了对应的dirver类
            //jdbc4.0后 java spi机制，自动扫描用户引入的路径，加载对应的driver-class
            //使用的类加载器为当前线程 Thread.currentThread().setContextClassLoader 的类加载器，即用户应用appClassLoader
            //相当于打破了双亲委派机制，DriverManager属于核心类，由bootstrapClassLoader加载，其需要加载的类是有同一个类加载器加载（jvm安全机制）
            //但此时由于jvm需要加载用户自行引入的数据库driver,,所以在此处需要打破安全机制，引入外部driver,通过appClassLoader加载数据库dirver
            conn = DriverManager.getConnection(url, userName, password);

            String sql = "select * from crm_dict";
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();

            List<Map> maps = resultSetToMap(resultSet);

            System.out.println(maps);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static List<Map> resultSetToMap(ResultSet resultSet) throws SQLException {
        List<Map> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, Object> jsonMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnTypeName = metaData.getColumnTypeName(i);
                String columnName = metaData.getColumnName(i);
                if ("INT".equals(columnTypeName)) {
                    int anInt = resultSet.getInt(columnName);
                    jsonMap.put(columnName, anInt);
                } else {
                    String s = resultSet.getString(columnName);
                    jsonMap.put(columnName, s);
                }
            }
            list.add(jsonMap);
        }
        return list;
    }
}
