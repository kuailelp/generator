package org.jfx.client.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.jfx.client.main.AppData;
import org.jfx.client.model.DataTable;

import java.sql.SQLException;

public class WInMain_InitTableViews {

    private WinMain winMain;

    private boolean all = false;
    private boolean all_table = false;
    private boolean all_view = false;

    public WInMain_InitTableViews(WinMain winMain) {
        this.winMain = winMain;
    }

    public void paintingTabel() throws SQLException, ClassNotFoundException {
        AppData.setString("username", winMain.getUsername().getText());
        AppData.setString("password", winMain.getPassword().getText());
        AppData.setString("host", winMain.getHost().getText());
        AppData.setString("port", winMain.getPort().getText());
        AppData.DataName = winMain.getDatabases().getText();
        AppData.DataType = winMain.getDataType().getValue().toString();
        winMain.getSwitchSql().init();
        winMain.getScannData().getTabels();
        // 处理表格
        winMain.getCreate().setCellValueFactory(
                arg0 -> {
                    DataTable data = arg0.getValue();
                    CheckBox checkBox = new CheckBox();
                    checkBox.setAlignment(Pos.CENTER);
                    checkBox.selectedProperty().setValue(data.isCreate());
                    checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                        data.setCreate(new_val);
                        checkBox.setSelected(new_val);
                    });
                    return new SimpleObjectProperty(checkBox);
                });
        winMain.getTableName().setCellValueFactory(new PropertyValueFactory<>("tableName"));
        winMain.getTableRemark().setCellValueFactory(new PropertyValueFactory<>("remark"));
        ObservableList<DataTable> tableModels = FXCollections.observableArrayList(
                AppData.tables
        );
        winMain.getTableViews().setCellValueFactory(arg0 -> {
            DataTable data = arg0.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(data.isView());
            checkBox.setDisable(true);


            return new SimpleObjectProperty(checkBox);
        });
        winMain.getTableAslis().setCellValueFactory(item -> {
            TextField aslis = new TextField();
            VBox vBox = new VBox();
            String as = item.getValue().getTableAils();
            aslis.setText(as);
            aslis.focusedProperty().addListener((observable, oldValue, newValue) -> {
                String value = aslis.getText();
                item.getValue().setTableAils(value);
            });
            aslis.setText(as);
            vBox.getChildren().add(aslis);
            return new SimpleObjectProperty<>(vBox);
        });
        winMain.getDatatable().setItems(tableModels);
        winMain.getButtonCreate().setDisable(false);
    }

    // 全选
    public void selectAll() {
        ObservableList<DataTable> list = winMain.getDatatable().getItems();
        list.forEach(item -> {
            if (!all) {
                item.setCreate(true);
            } else {
                item.setCreate(false);
            }
        });
        winMain.getDatatable().setItems(list);
        winMain.getDatatable().refresh();
        if (all) {
            all = false;
            winMain.getAllT().setText("全   选");
        } else {
            all = true;
            winMain.getAllT().setText("取消全选");
        }
    }
}
