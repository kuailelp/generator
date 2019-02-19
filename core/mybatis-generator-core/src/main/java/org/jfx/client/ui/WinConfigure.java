package org.jfx.client.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * 文件名：WinConfigure.java
 * 描述：配置页面控制器
 * 创建人：廖鹏
 * 创建日期：2019/2/19 9:08
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class WinConfigure implements Initializable {

    private Stage stage;


    @FXML
    private CheckBox selectModel;
    @FXML
    private AnchorPane panModel;
    @FXML
    private CheckBox selectMapper;
    @FXML
    private AnchorPane panMapper;
    @FXML
    private CheckBox selectServices;
    @FXML
    private AnchorPane panServices;
    @FXML
    private CheckBox selectController;
    @FXML
    private AnchorPane panController;
    @FXML
    private CheckBox selectUIMIni;
    @FXML
    private AnchorPane panMini;


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectModel.selectedProperty().addListener(new selectListener());
        selectMapper.selectedProperty().addListener(new selectListener());
        selectServices.selectedProperty().addListener(new selectListener());
        selectController.selectedProperty().addListener(new selectListener());
        selectUIMIni.selectedProperty().addListener(new selectListener());
    }


    /**
     * 描述：checkBox 值改变监听 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/19 10:04 <br>
     */
    class selectListener implements ChangeListener<Boolean> {


        /**
         * This method needs to be provided by an implementation of
         * {@code ChangeListener}. It is called if the value of an
         * {@link ObservableValue} changes.
         * <p>
         * In general is is considered bad practice to modify the observed value in
         * this method.
         *
         * @param observable The {@code ObservableValue} which value changed
         * @param oldValue   The old value
         * @param newValue
         */
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            BooleanProperty booleanProperty = (BooleanProperty) observable;
            CheckBox o = (CheckBox) booleanProperty.getBean();
            switch (o.getId()) {
                case "selectModel":
                    panModel.setDisable(!newValue);
                    break;
                case "selectMapper":
                    panMapper.setDisable(!newValue);
                    break;
                case "selectServices":
                    panServices.setDisable(!newValue);
                    break;
                case "selectController":
                    panController.setDisable(!newValue);
                    break;
                case "selectUIMIni":
                    panMini.setDisable(!newValue);
                    break;

            }
        }
    }

    /**
     * 描述：目录选择方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/19 11:48 <br>
     */
    public void onSelectDir(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择生成的目录");
        String path = directoryChooser.showDialog(stage).getAbsolutePath();
        System.out.println(path);

    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
