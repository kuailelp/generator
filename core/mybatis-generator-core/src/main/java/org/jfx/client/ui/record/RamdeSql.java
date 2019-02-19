package org.jfx.client.ui.record;

import org.jfx.client.main.AppData;

public class RamdeSql {

    private SQL sql;

    private String dataBases;

    public RamdeSql() {
        dataBases = AppData.DataName;
        switch (AppData.DataType.toUpperCase()) {
            case "MYSQL":
                sql = new MysqlRamde();
            default:
                sql = new MysqlRamde();
        }

    }

    /**
     * 描述：显示全部表 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 10:54 <br>
     */
    public String showTables() {
        return sql.showTables(dataBases);
    }


}
