package org.jfx.client.ui.auxiliary;

import org.jfx.client.main.AppData;

/**
 * 文件名：SwitchSql.java
 * 描述：切换sql查询
 * 创建人：廖鹏
 * 创建日期：2019/3/26 9:02
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class SwitchSql {

    private String host;
    private String port;
    private String databases;
    private String url;


    public void init() {
        this.host = AppData.getString("host");
        this.port = AppData.getString("port");
        this.databases = AppData.DataName;
        switch (AppData.DataType.toUpperCase()) {
            case "MYSQL":
                url = "jdbc:mysql://" + host + ":" + port + "/" + databases;
                AppData.setString("url", url);
                AppData.setString("drives", "com.mysql.jdbc.Driver");
                break;
            default:
                url = "jdbc:mysql://" + host + ":" + port + "/" + databases;
                AppData.setString("url", url);
                AppData.setString("drives", "com.mysql.jdbc.Driver");
                break;
        }

    }


}
