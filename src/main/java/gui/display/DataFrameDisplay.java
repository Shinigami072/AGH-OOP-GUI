package gui.display;

import gui.create.CreationDialog;
import gui.model.ObservableDataframe;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import lab0.dataframe.DataFrame;
import lab0.dataframe.values.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DataFrameDisplay {

    public DataFrameView dataframe;
    private Scene scene;

    public void show(Stage s, ObservableDataframe df,String title){
        Stage s1= new Stage();
        s1.initOwner(s);
        s1.setScene(scene);
        s1.setTitle(title);
        dataframe.setDataFrame(df);
        s1.show();

    }
    public static DataFrameDisplay getInstance() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DataFrameDisplay.class.getResource("/DataFrameDialog.fxml"));

            Scene scene = new Scene(loader.load());
            DataFrameDisplay c = loader.getController();
            c.scene=scene;
            return c;
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

}


