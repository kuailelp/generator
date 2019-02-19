package org.jfx.client.ui.auxiliary;

import org.jfx.client.main.AppData;
import org.jfx.client.model.DataTable;
import org.jfx.client.ui.record.RamdeSql;

import java.sql.*;
import java.util.ArrayList;

/**
 * 描述：扫描数据库 <br>
 * 创建人：廖鹏 | 创建日期：2019/2/18 10:28 <br>
 */
public class ScannData {

    private String drives;
    private String username;
    private String password;
    private String url;


    private static Connection connection;

    private static RamdeSql ramdeSql;


    public void init() throws ClassNotFoundException, SQLException {
        this.drives = AppData.getString("drives");
        this.username = AppData.getString("username");
        this.password = AppData.getString("password");
        this.url = AppData.getString("url");
        Class.forName(drives);

        connection = DriverManager.getConnection(url, username, password);
        ramdeSql = new RamdeSql();
    }


    public void getTabels() throws SQLException, ClassNotFoundException {
        init();
        String sql = ramdeSql.showTables();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        AppData.tables = new ArrayList<>();
        while (rs.next()) {
            DataTable table = new DataTable();
            table.setTableName(rs.getString("TABLE_NAME"));
            String remark = rs.getString("TABLE_COMMENT");
            if ("VIEW".equals(remark)) {
                table.setCreate(false);
                table.setView(true);
            }
            table.setRemark(remark);
            AppData.tables.add(table);
        }
    }

}
