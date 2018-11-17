package gui.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import lab0.dataframe.values.*;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class CreationDialogController {
    public CheckBox type;
    public ContextMenu test;
    public TextField colname;
    @FXML
    ListView<NamedType> list;
    Stage s;
    File f;

    class NamedType {
        String name;
        String loadedName;
        Class<? extends Value> type;

        NamedType(String name) {
            this.name = name;
            this.loadedName =name;
            this.type = ValueTypeUtils.getType(0);
        }

        @Override
        public String toString() {
            return (getState()?name:loadedName) + " : " + ValueTypeUtils.classToName(type);
        }
    }

//    class TypeHandler implements EventHandler<ActionEvent> {
//        int type;
//
//        TypeHandler(int i) {
//            type = i;
//        }
//
//        @Override
//        public void handle(ActionEvent event) {
//            ObservableList selected = list.getSelectionModel().getSelectedItems();
//            for (int i = 0; i < selected.size(); i++) {
//                ((NamedType) selected.get(i)).type = getType(type);
//            }
//            list.refresh();
//        }
//    }

    ObservableList<NamedType> colTypes;
    LinkedList<NamedType> listColTypes;

    public void init(Stage stage1, File open) {
        s = stage1;
        f = open;
        try (BufferedReader bf = new BufferedReader(new FileReader(open))) {
            String[] first;
            first = bf.readLine().split(",");
            listColTypes = new LinkedList<NamedType>();
            colTypes = FXCollections.observableList(listColTypes);
            for (int i = 0; i < first.length; i++) {
                colTypes.add(new NamedType(first[i]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < 3; i++) {
//            MenuItem menuIt = new MenuItem(classToName(getType(i)));
//            menuIt.setOnAction(new TypeHandler(i));
//            test.getItems().add(menuIt);
//        }
        list.setCellFactory((Callback<ListView<NamedType>, ListCell<NamedType>>) param -> new NamedTypeCell(e -> getState()));
        list.setItems(colTypes);
        list.getSelectionModel().selectFirst();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public void close(ActionEvent actionEvent) {
        if(valid())
            s.close();
        actionEvent.consume();
    }

    private boolean valid() {
        HashSet<String> names = new HashSet<>();
        for(NamedType i:colTypes){
            if(!names.add(i.name))
                return false;
        }
        return true;
    }

    public boolean getState(){
        return type.isSelected();
    }
    public void changeState(ActionEvent actionEvent) {
        colname.setEditable(!type.isSelected());
//        list.setEditable(!type.isSelected());
        actionEvent.consume();
    }

    private class NamedTypeCell extends ListCell<NamedType> {
        ListView<NamedType> root;
        NamedTypeController n;
        Predicate<Object> isHeader;


        public NamedTypeCell(Predicate isHeader) {
            this.isHeader =isHeader;
            n = new NamedTypeController();
            setGraphic(n.getBox());
            n.setCommit(e -> commitEdit(getItem()) );
        }

        @Override
        public void startEdit() {
            super.startEdit();
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            n.setEditableName(isHeader.test(null));
            n.setName(n.isEditable()?getItem().name:getItem().loadedName);
            n.setSelected(getItem().type);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().toString());
            setContentDisplay(ContentDisplay.TEXT_ONLY);

        }

        @Override
        public void commitEdit(NamedType newValue) {
            newValue.name=n.getName();
            newValue.type=n.getSelected();
            super.commitEdit(newValue);
        }

        @Override
        protected void updateItem(NamedType item, boolean empty) {
            super.updateItem(item, empty);

            if (isEditing()) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
                if (!empty) {
                    setText(item.toString());
                } else {
                    setText("");
                }
            }


        }
    }
}
