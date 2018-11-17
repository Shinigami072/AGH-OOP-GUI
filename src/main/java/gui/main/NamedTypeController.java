package gui.main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import lab0.dataframe.values.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NamedTypeController {

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<Class<? extends Value>> type;


    public HBox root;
    NamedTypeController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/NamedType.fxml"));
        fxmlLoader.setController(this);
        try
        {
            List<Class<? extends Value>> types = new LinkedList<>();
            for (int i = 0; i < ValueTypeUtils.getTypeCount(); i++) {
                types.add(ValueTypeUtils.getType(i));
            }
            fxmlLoader.load();
            type.setItems(FXCollections.observableList(types));
            type.getSelectionModel().selectFirst();
            type.setConverter(new StringConverter<Class<? extends Value>>() {

                @Override
                public String toString(Class<? extends Value> object) {
                    return ValueTypeUtils.classToName(object);
                }

                @Override
                public Class<? extends Value> fromString(String string) {
                    throw new UnsupportedOperationException();
                }
            });

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void setName(String n){
        name.setText(n);
    }
    public String getName(){
        return name.getText();
    }
    public void setEditableName(boolean edit){
        name.setEditable(edit);
        name.setDisable(!edit);
    }
    public Class<? extends Value> getSelected(){
        return (Class<? extends Value>) type.getSelectionModel().getSelectedItem();
    }
    public void setSelected( Class<? extends Value> i){
//        type.getSelectionModel().clearSelection();
        type.getSelectionModel().select(i);

    }
    public HBox getBox() {
        return root;
    }

    public void setCommit(EventHandler<ActionEvent> eventHandler) {
        name.setOnAction(eventHandler);
        type.setOnAction(eventHandler);
    }

    public boolean isEditable() {
        return name.isEditable();
    }
}
