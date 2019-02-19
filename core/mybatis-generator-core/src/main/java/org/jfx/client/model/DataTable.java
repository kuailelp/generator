package org.jfx.client.model;

/**
 * 文件名：DataTable.java
 * 描述：扫描数据库文件
 * 创建人：廖鹏
 * 创建日期：2019/2/18 9:45
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class DataTable {

    // 是否生成
    private boolean create;
    // 表名称
    private String tableName;
    // 表别称
    private String tableAils;
    // 备注
    private String remark;
    // 是否视图
    private boolean view;


    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAils() {
        return tableAils;
    }

    public void setTableAils(String tableAils) {
        this.tableAils = tableAils;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }
}
