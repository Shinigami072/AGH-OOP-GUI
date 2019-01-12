package gui.display;

import gui.model.ObservableDataframe;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lab0.dataframe.DataFrame;
import lab0.dataframe.values.Value;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DataFrameView extends TableView<Value[]> {

    class DataFrameValueFactory implements Callback<TableColumn.CellDataFeatures<Value[], String>, ObservableValue<String>>{
        int finalI;
        DataFrameValueFactory(int i){
            this.finalI=i;
        }
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Value[], String> param) {
            return new SimpleStringProperty(param.getValue()[finalI].toString());
        }
    }

    Hashtable<Integer,DataFrameValueFactory> factories;
    public void setDataFrame(ObservableDataframe df){
        getColumns().setAll(getColumns(df));
        setItems(df);

    }

    private Collection<TableColumn<Value[],String>> getColumns(ObservableDataframe df){
        List<TableColumn<Value[],String>> columns= new LinkedList<>();
        if(factories==null)
            factories=new Hashtable<>();

        String[] names = df.getNames();
        for (int i = 0; i < names.length; i++) {
            TableColumn<Value[],String> column = new TableColumn<>(names[i]);
            column.setSortable(false);
            DataFrameValueFactory factory = factories.get(i);
            if(factory==null){
                factory= new DataFrameValueFactory(i);
                factories.put(i,factory);
            }
            column.setCellValueFactory(factory);
            columns.add(column);
        }

        return columns;
    }
}
