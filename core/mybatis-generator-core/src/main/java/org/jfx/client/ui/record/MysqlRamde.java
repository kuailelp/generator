package org.jfx.client.ui.record;

public class MysqlRamde implements SQL {
    /**
     * 描述：查询全部表 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 10:52 <br>
     *
     * @param database
     */
    @Override
    public String showTables(String database) {
        return "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + database + "'";
    }
}
