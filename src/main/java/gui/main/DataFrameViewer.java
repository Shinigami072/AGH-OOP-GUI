package gui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab0.dataframe.DataFrame;
import lab0.dataframe.exceptions.DFColumnTypeException;
import lab0.dataframe.values.IntegerValue;

import java.io.IOException;

public class DataFrameViewer extends Application {

    public static void main(String[] args) {
        DataFrame df = new DataFrame(new String[]{"a", "b", "c"}, new Class[]{IntegerValue.class, IntegerValue.class, IntegerValue.class});
        try {
            df.addRecord(new IntegerValue(1), new IntegerValue(1), new IntegerValue(1));
            System.out.println(df);
        } catch (DFColumnTypeException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = null;
        DataFrameController ctrl = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/DataFrameViewer.fxml"));
//            loader.setController(new gui.main.DataFrameController(stage));
        root = loader.load();
        ctrl = loader.getController();

        ctrl.init(stage);

        Scene scene = new Scene(root);
        stage.setTitle("DataFrame Viewer");
        stage.setScene(scene);
        stage.show();
    }
}
