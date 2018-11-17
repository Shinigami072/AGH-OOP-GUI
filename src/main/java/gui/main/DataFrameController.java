package gui.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataFrameController {

    @FXML
    public Label debug;
    public Stage stage;
    public File last;


    private void config(){

        fileChooser.setInitialDirectory(last);
    }
    final FileChooser fileChooser= new FileChooser();

    @FXML
    public void openFile(Event e){
        config();
        File open =null;
        do{
        open=fileChooser.showOpenDialog(stage);

        }while(open==null || !open.isFile() || !open.exists() || !open.canRead() );

        last=open.getParentFile();
        System.out.println(open);
        System.out.println(open.getParent());
        debug.setText(open.toString());
        System.out.println("stage1?");

//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/DataFrameCreateDialog.fxml"));
//            Stage stage1 = new Stage();
//            stage1.initOwner(stage);
//            stage1.setAlwaysOnTop(true);
//            stage1.initModality(Modality.APPLICATION_MODAL);
//            stage1.setScene(new Scene(loader.load()));
//            stage1.setOnCloseRequest(Event::consume);
//            stage1.showAndWait();
        CreationDialog cr = new CreationDialog(open);
        cr.show(stage);
        System.out.println("stage1.");
    }

    @FXML
    protected void initialize() {
        this.last = new File(System.getProperty("user.home"));
        fileChooser.setTitle("Choose DataFrame");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("DataFrame", "*.csv");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
    }

    public void init(Stage s){
        stage=s;
    }
}
