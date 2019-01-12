package gui.main;

import gui.display.AlertFactory;
import gui.ApplyableUtils;
import gui.create.CreationDialog;
import gui.display.ChartViewer;
import gui.display.DataFrameDisplay;
import gui.display.DataFrameView;
import gui.model.ObservableDataframe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lab0.dataframe.DataFrame;
import lab0.dataframe.exceptions.DFApplyableException;
import lab0.dataframe.exceptions.DFColumnTypeException;
import lab0.dataframe.exceptions.DFValueBuildException;
import lab0.dataframe.exceptions.DFZeroLengthCreationException;
import lab0.dataframe.groupby.GroupBy;

import java.io.*;
import java.util.*;

public class DataFrameController {


    public ListView<String> colSelect;
    public ComboBox<ApplyableUtils.AP_OPERTATION> operation;
    public SplitPane main;
    public Menu recent;
    @FXML
    DataFrameView dataframe;

    @FXML
    Label debug;

    private ObservableDataframe loaded;
    private Stage stage;
    private File last;
    private ObservableList<MenuItem> lastStack;
    private HashMap<List<String>, GroupBy> groups;
    private DataFrameDisplay display;


    public DataFrameController() {
        display = DataFrameDisplay.getInstance();
    }

    private void config() {

        fileChooser.setInitialDirectory(last);
    }

    private final FileChooser fileChooser = new FileChooser();

    private File getFile() {
        File open = null;
        do {
            open = fileChooser.showOpenDialog(stage);
            if (open == null && ButtonType.YES == AlertFactory.getInstance(stage).showConfirm("Yre You sure you wnat to cancel", "?").get())
                return null;

        } while (open == null || !open.isFile() || !open.exists() || !open.canRead());

        return open;
    }

    private void load(File open) {
        debug.setText(open.toString());
        CreationDialog.NamedTypeHeader header = null;
        CreationDialog cr = CreationDialog.getInstance();
        String[] names;
        try (BufferedReader bf = new BufferedReader(new FileReader(open))) {
            names = bf.readLine().split(",");
        } catch (IOException e) {
            AlertFactory.getInstance(stage).showError(e);
            return;
        }

        do {
            if (header == null) {
                header = cr.show(stage, names);
            } else {
                header = cr.show(stage, header);
            }

            if (header == null && ButtonType.YES == AlertFactory.getInstance(stage).showConfirm("Yre You sure you wnat to cancel", "?").get())
                return;
            else if(header==null)
                continue;

            try {
                //todo: wrong length exc
                if (header.isInFile()) {
                    loaded = new ObservableDataframe(new DataFrame(open.getPath(), header.getTypes()));
                } else {
                    loaded = new ObservableDataframe(new DataFrame(open.getPath(), header.getTypes(), header.getNames()));
                }

            } catch (IOException | DFColumnTypeException e) {
                AlertFactory.getInstance(stage).showError(e);
                e.printStackTrace();
            } catch (DFValueBuildException e) {
                AlertFactory.getInstance(stage).showError(e);//todo: human warning
                e.printStackTrace();
            }

        } while (header == null);


        updateView();
    }

    @FXML
    public void openFile(Event event) {
        config();
        File open = getFile();
        if (open == null)
            return;

        last = open.getParentFile();
        load(open);

        MenuItem item = new MenuItem(open.toString());
        item.setOnAction(e -> load(open));
        if (lastStack.filtered(e -> e.getText().equals(open.toString())).size() == 0)
            lastStack.add(item);
    }

    private void updateView() {
        main.setDisable(false);
        String[] names = loaded.getNames();

        colSelect.setItems(FXCollections.observableList(Arrays.asList(loaded.getNames())));
        colSelect.getSelectionModel().selectFirst();

        dataframe.setDataFrame(loaded);

        if (groups == null)
            groups = new HashMap<>();
        else
            groups.clear();
    }


    @FXML
    protected void initialize() {
        this.last = new File(System.getProperty("user.home"));
        this.lastStack = recent.getItems();

        fileChooser.setTitle("Choose DataFrame:");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("DataFrame", "*.csv");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);

        operation.setItems(FXCollections.observableArrayList(ApplyableUtils.AP_OPERTATION.values()));
        operation.getSelectionModel().selectFirst();

        colSelect.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void init(Stage s) {
        stage = s;
    }

    public void execute(MouseEvent mouseEvent) {
        List<String> key = colSelect.getSelectionModel().getSelectedItems();

        if (key.size() == 0) {
            AlertFactory.getInstance(stage).showWarning("Select at least one Column");
            return;
        } else if (key.size() == loaded.colCount()) {
            AlertFactory.getInstance(stage).showWarning("Leave at least one data Column");
            return;
        }

        GroupBy group = groups.get(key);
        ApplyableUtils.AP_OPERTATION OPERATION = operation.getSelectionModel().getSelectedItem();

        try {
            if (group == null) {

                group = loaded.groupBy(key.toArray(new String[0]));
                System.out.println(Arrays.toString(key.toArray()) + group.toString());
                groups.put(key, group);

            }
            display.show(stage, new ObservableDataframe(OPERATION.apply(group)), OPERATION.toString() + ": keys: " + Arrays.toString(key.toArray()));
        } catch (CloneNotSupportedException e) {
            AlertFactory.getInstance(stage).showError(e);
        } catch (DFZeroLengthCreationException e) {
            AlertFactory.getInstance(stage).showWarning("Unable to find applicable data for " + OPERATION.toString());
        } catch (DFApplyableException e) {
            AlertFactory.getInstance(stage).showWarning(e.getMessage());
        }
    }

    public void chart(MouseEvent mouseEvent) {
        if(loaded!=null) {
            ChartViewer.getInstance().show(stage,loaded);
        }

    }
}
