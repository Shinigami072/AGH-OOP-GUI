package gui.display;

import gui.model.ObservableDataframe;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lab0.dataframe.DataFrame;

import java.io.IOException;

public class ChartViewer {
    public Chart chart;
    public ComboBox<ChartTypes> type;
    public ComboBox<String> xcolname;
    public ListView<String> ycolname;
    public ScrollPane chartPane;
    public Button button;
    private Scene scene;
    private ObservableDataframe dataFrame;

    enum ChartTypes{

        LINE(true,true,"Line"),
        AREA(true,true,"Area"),
        BAR(false,false,"Bar"),
        SCATTER(true,true,"Scatter");

        ChartTypes(boolean x,boolean y,String name){
            canUseXNumber=x;
            canUseYCategory=y;
            this.name=name;
        }

        @Override
        public String toString() {
            return name;
        }

        boolean canUseXNumber;
        boolean canUseYCategory;

        String name;
    }

    public static ChartViewer getInstance() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DataFrameDisplay.class.getResource("/ChartViewer.fxml"));

            Scene scene = new Scene(loader.load());
            ChartViewer c = loader.getController();
            c.scene=scene;
            return c;
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void initialize(){
        type.setItems(FXCollections.observableArrayList(ChartTypes.values()));
        ycolname.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void show(Stage stage, ObservableDataframe loaded) {
        Stage nstage = new Stage();
        nstage.initOwner(stage);
        nstage.setScene(scene);
        nstage.show();
        xcolname.getItems().setAll(loaded.getNames());
        xcolname.getSelectionModel().selectFirst();
        ycolname.getItems().setAll(loaded.getNames());
        ycolname.getSelectionModel().selectLast();
        dataFrame=loaded;
    }
    ChartTypes getType(){
        return type.getValue();
    }
    private String getXName(){
        return xcolname.getSelectionModel().getSelectedItem();
    }

    private boolean isXNumeric(){
        return getType().canUseXNumber && dataFrame.isNumeric(getXName());
    }
    private boolean isYNumeric(){
        for (String colname:ycolname.getSelectionModel().getSelectedItems()) {
            if(!dataFrame.isNumeric(colname))
                return false;
        }
        return true;
    }
    private Axis getYAxis(){
        if(isYNumeric())
            return new NumberAxis();
        else
            return new CategoryAxis();
    }
    private Axis getXAxis(){

        if(isXNumeric()&&getType().canUseXNumber)
        {
            return new NumberAxis();
        }
        else
            return new CategoryAxis();
    }
    private boolean valid(){
        if(type.getSelectionModel().isEmpty()||ycolname.getSelectionModel().getSelectedItems().size()<=0 ||
                xcolname.getSelectionModel().isEmpty()||
                (!getType().canUseYCategory && !isYNumeric()) ||
                (!getType().canUseXNumber && isXNumeric())
        ){
            return false;
        }
        else
            return true;
    }

    @FXML
    private void updateControls(){
        button.setDisable(!valid());
    }
    public void chart(ActionEvent actionEvent) {
        Chart c;
        if(!valid()) {
            AlertFactory.getInstance().getWarning("Incorrect Data Types for Selected Chart");
            return;
        }
        switch (getType()){

            default:
                AlertFactory.getInstance().getWarning("Select Chart type");
                return;
            case BAR:
                c= new BarChart<>(getXAxis(), getYAxis(),dataFrame.getSeries(!isXNumeric(),!isYNumeric(),xcolname.getSelectionModel().getSelectedItem(),ycolname.getSelectionModel().getSelectedItems()));
            break;
            case LINE:
                c= new LineChart<>(getXAxis(), getYAxis(),dataFrame.getSeries(!isXNumeric(),!isYNumeric(),xcolname.getSelectionModel().getSelectedItem(),ycolname.getSelectionModel().getSelectedItems()));
            break;
            case AREA:
                c= new AreaChart<>(getXAxis(), getYAxis(),dataFrame.getSeries(!isXNumeric(),!isYNumeric(),xcolname.getSelectionModel().getSelectedItem(),ycolname.getSelectionModel().getSelectedItems()));
            break;
            case SCATTER:
                c= new ScatterChart<>(getXAxis(), getYAxis(),dataFrame.getSeries(!isXNumeric(),!isYNumeric(),xcolname.getSelectionModel().getSelectedItem(),ycolname.getSelectionModel().getSelectedItems()));
            break;
        }

        chartPane.setContent(c);
    }
}
