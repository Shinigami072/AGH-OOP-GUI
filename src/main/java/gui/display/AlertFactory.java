package gui.display;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class AlertFactory {
    Stage parent;
    static AlertFactory default_factory;

    AlertFactory(Stage parent) {
        this.parent = parent;
    }
    public static AlertFactory getInstance(){
        if(default_factory==null)
            default_factory = new AlertFactory(new Stage());

        return default_factory;
    }
    public static AlertFactory getInstance(Stage parent) {
        if (default_factory == null || default_factory.parent != parent)
            default_factory = new AlertFactory(parent);
        return default_factory;
    }

    public Optional<ButtonType> showConfirm(String confirm,String content) {
        return getConfirm(confirm,content).showAndWait();
    }

    private Alert getConfirm(String confirm,String content){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(confirm);
        a.setContentText(content);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        a.getButtonTypes().setAll(yesButton, noButton);
        return a;
    }
    public void showWarning(String Warning){
        getWarning(Warning).showAndWait();
    }

    public Alert getWarning(String warning) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(warning);
        return a;
    }

    public void showError(Exception e){
        getError(e).showAndWait();
    }
    public Alert getError(Exception e){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(e.getMessage());

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(label, textArea);

        // Set content for Dialog Pane
        a.getDialogPane().setExpandableContent(dialogPaneContent);
        return a;
    }
}
