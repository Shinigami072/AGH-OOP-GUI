package gui.create;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lab0.dataframe.values.Value;

import java.io.IOException;

import static gui.ValueTypeUtils.getConverter;
import static gui.ValueTypeUtils.getTypeList;

public class NamedTypeEditor {

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<Class<? extends Value>> type;
    @FXML
    private HBox root;

    static NamedTypeEditor getInstance() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NamedTypeEditor.class.getResource("/NamedType.fxml"));
            fxmlLoader.load();
            NamedTypeEditor instance = fxmlLoader.getController();
            instance.type.setItems(FXCollections.observableList(getTypeList()));
            instance.type.getSelectionModel().selectFirst();
            instance.type.setConverter(getConverter());
            return instance;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setName(String n) {
        name.setText(n);
    }

    String getName() {
        return name.getText();
    }

    void setEditableName(boolean edit) {
        name.setEditable(edit);
        name.setDisable(!edit);
    }

    Class<? extends Value> getSelected() {
        return type.getSelectionModel().getSelectedItem();
    }

    void setSelected(Class<? extends Value> i) {
//        header.getSelectionModel().clearSelection();
        type.getSelectionModel().select(i);

    }

    HBox getBox() {
        return root;
    }

    void setCommit(EventHandler<ActionEvent> eventHandler) {
        name.setOnAction(eventHandler);
        type.setOnAction(eventHandler);
    }

    boolean isEditable() {
        return name.isEditable();
    }

    NamedType getNamedType(String loaded) {
        return new NamedType(getName(), getSelected(), loaded);
    }
}
