package org.jfx.client.ui.auxiliary;

import org.jfx.client.main.AppData;

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
