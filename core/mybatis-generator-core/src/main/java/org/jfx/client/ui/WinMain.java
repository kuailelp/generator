package org.jfx.client.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfx.client.main.App;
import org.jfx.client.model.DataTable;
import org.jfx.client.ui.auxiliary.CreateContext;
import org.jfx.client.ui.auxiliary.ScannData;
import org.jfx.client.ui.auxiliary.SwitchSql;

import java.io.IOException;
import java.sql.SQLException;

public class WinMain {

    @FXML
    private ChoiceBox dataType;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private TextField databases;
    @FXML
    private TableView datatable;
    @FXML
    private TableColumn<DataTable, CheckBox> create;
    @FXML
    private TableColumn<DataTable, String> tableName;
    @FXML
    private TableColumn<DataTable, String> tableRemark;
    @FXML
    private TableColumn<DataTable, CheckBox> tableViews;
    @FXML
    private TableColumn<DataTable, VBox> tableAslis;
    @FXML
    private Button allT;
    @FXML
    private Button buttonCreate;
    @FXML
    private VBox createConfigure;

    private ScannData scannData;
    private SwitchSql switchSql;

    private Stage stage;


    /**
     * 描述：初始化数据 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 10:26 <br>
     */
    public void initData() throws SQLException, ClassNotFoundException, IOException {
        dataType.setItems(FXCollections.observableArrayList("MySql", "Oracle"));
        dataType.setValue("MySql");
        port.setText("3306");
        scannData = new ScannData();
        switchSql = new SwitchSql();
//        buttonCreate.setDisable(true);
        getRenderingCreateConfigure();

    }

    /**
     * 描述：渲染配置服务控制器 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/19 9:05 <br>
     */
    public void getRenderingCreateConfigure() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/fxml/win_configure.fxml"));
        AnchorPane pane = loader.load();
        WinConfigure winConfigure = loader.getController();
        winConfigure.setStage(stage);
        double hg = createConfigure.getPrefHeight();
        pane.setMinHeight(hg);
        createConfigure.getChildren().add(pane);
    }

    private WInMain_InitTableViews inMain_initTableViews;

    /**
     * 描述：扫描数据库 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 10:26 <br>
     */
    public void searchData() throws SQLException, ClassNotFoundException {
        inMain_initTableViews = new WInMain_InitTableViews(this);
        inMain_initTableViews.paintingTabel();
    }

    /**
     * 描述：生成代码 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/19 8:43 <br>
     */
    public void createCode() {
        CreateContext createContext = new CreateContext();
        createContext.init();
    }

    /**
     * 描述：全选表格内 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/18 15:15 <br>
     */
    public void selectAll() {
        inMain_initTableViews.selectAll();
    }


    public ChoiceBox getDataType() {
        return dataType;
    }

    public TextField getUsername() {
        return username;
    }

    public TextField getPassword() {
        return password;
    }

    public TextField getHost() {
        return host;
    }

    public TextField getPort() {
        return port;
    }

    public TextField getDatabases() {
        return databases;
    }

    public TableView getDatatable() {
        return datatable;
    }

    public TableColumn<DataTable, CheckBox> getCreate() {
        return create;
    }

    public TableColumn<DataTable, String> getTableName() {
        return tableName;
    }

    public TableColumn<DataTable, String> getTableRemark() {
        return tableRemark;
    }

    public ScannData getScannData() {
        return scannData;
    }

    public SwitchSql getSwitchSql() {
        return switchSql;
    }

    public TableColumn<DataTable, CheckBox> getTableViews() {
        return tableViews;
    }

    public TableColumn<DataTable, VBox> getTableAslis() {
        return tableAslis;
    }

    public Button getAllT() {
        return allT;
    }

    public Button getButtonCreate() {
        return buttonCreate;
    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
