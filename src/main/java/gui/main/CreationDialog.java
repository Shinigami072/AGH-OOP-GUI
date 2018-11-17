package gui.main;


import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CreationDialog {

    File open;
    public CreationDialog(File open) {
        this.open=open;
    }

    public void show(Stage stage){
            try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/DataFrameCreateDialog.fxml"));
            Stage stage1 = new Stage();
            stage1.initOwner(stage);
            stage1.setAlwaysOnTop(true);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.setScene(new Scene(loader.load()));
            CreationDialogController di = loader.getController();
            di.init(stage1,open);
            stage1.setOnCloseRequest(Event::consume);
            stage1.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
