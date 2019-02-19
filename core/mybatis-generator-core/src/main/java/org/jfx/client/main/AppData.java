package org.jfx.client.main;

import org.jfx.client.model.DataTable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 文件名：AppData.java
 * 描述：数据存储模块
 * 创建人：廖鹏
 * 创建日期：2019/2/18 10:29
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class AppData {

    // 数据库类型
    public static String DataType;
    // 数据库表名
    public static String DataName;
    // 数据存储
    private static HashMap<String, Object> data = new HashMap<>();
    // 表集合
    public static ArrayList<DataTable> tables;

    public static HashMap<String, Object> getData() {
        return data;
    }

    public static String getString(String key) {
        return (String) data.get(key);
    }

    public static int getInt(String key) {
        return (int) data.get(key);
    }

    public static void setData(HashMap<String, Object> data) {
        AppData.data = data;
    }

    public static void setString(String key, String value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    /**
     * 描述：清理表中数据 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 10:43 <br>
     */
    public static void clearTables() {
        tables.clear();
    }
}
