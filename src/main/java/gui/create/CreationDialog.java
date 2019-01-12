package gui.create;


import gui.display.AlertFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lab0.dataframe.values.Value;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class CreationDialog {


    @FXML
    CheckBox header;
    @FXML
    ListView<NamedType> list;


    @FXML
    void changeState(ActionEvent actionEvent) {
        list.refresh();
        actionEvent.consume();
    }

    @FXML
    void sumbit(ActionEvent actionEvent) {

        if(valid()){//todo: wyświetl błąd gdy nie valid
            cancelled=false;
            current.close();
        }
        else{
            AlertFactory.getInstance().showWarning("Chose uniquecolumn names");
        }
        actionEvent.consume();
    }


    private boolean valid() {
        HashSet<String> names = new HashSet<>();
        for(NamedType i:colTypes){
            if(!names.add(i.getName(!isHeaderinFile())))
                return false;
        }
        return true;
    }


    private Scene scene;
    private Stage current;
    private ObservableList<NamedType> colTypes;
    private boolean cancelled;

    private boolean isHeaderinFile(){
        return header.isSelected();
    }



    public static CreationDialog getInstance() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CreationDialog.class.getResource("/DataFrameCreateDialog.fxml"));
            Scene scene = new Scene(loader.load());
            CreationDialog c = loader.getController();
            c.scene = scene;
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init(NamedTypeHeader header){
        String[] names = header.getNames();
        Class<? extends Value>[] types = header.getTypes();
        colTypes = FXCollections.observableList(new LinkedList<>());

        for (int i = 0; i < names.length; i++)
            colTypes.add(new NamedType(names[i],types[i],names[i]));


        init();
    }
    private void init(String[] names){
        colTypes = FXCollections.observableList(new LinkedList<>());

        for (String name : names)
            colTypes.add(new NamedType(name));

        init();

    }
    private void init(){
        list.setCellFactory(param -> new NamedTypeCell(e -> isHeaderinFile()));
        list.setItems(colTypes);
        list.getSelectionModel().selectFirst();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public NamedTypeHeader show(Stage stage,NamedTypeHeader header) {

        init(header);

        return show(stage);
    }

    public NamedTypeHeader show(Stage stage,String[] names) {

        init(names);
        return show(stage);
    }

    private NamedTypeHeader show(Stage stage){
        cancelled=true;
        //create modal stage
        current = new Stage();
        current.initOwner(stage);
        current.initModality(Modality.APPLICATION_MODAL);

        //show uncloseable dialog
        current.setScene(scene);
//        current.setOnCloseRequest(Event::consume);
        current.showAndWait();

        if(cancelled)
            return null;
        else
            return new NamedTypeHeader(colTypes.toArray(new NamedType[0]), isHeaderinFile());
        //wait until it dies
    }

    public class NamedTypeHeader {
        private NamedType[] types;
        private boolean header;
        NamedTypeHeader(NamedType[] types, boolean header) {
            this.types=types;
            this.header =header;
        }

        public boolean isInFile() {
            return header;
        }


        public Class<? extends Value>[] getTypes() {
            Class<? extends Value>[] classtypes = new Class[types.length];
            for(int i=0;i<types.length;i++)
                classtypes[i]=types[i].getType();
            return classtypes;
        }

        public String[] getNames() {
            String[] names = new String[types.length];
            for(int i=0;i<types.length;i++)
                names[i]=types[i].getName(!header);
            return names;
        }
    }
}
