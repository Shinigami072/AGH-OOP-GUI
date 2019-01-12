package gui.create;

import gui.ValueTypeUtils;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;

import java.util.function.Predicate;

class NamedTypeCell extends ListCell<NamedType> {
    private NamedTypeEditor typeEditor;
    private Predicate<Object> isHeaderInFile;


    NamedTypeCell(Predicate<Object> isHeaderInFile) {
        this.isHeaderInFile = isHeaderInFile;
        typeEditor = NamedTypeEditor.getInstance();
        setGraphic(typeEditor.getBox());
        typeEditor.setCommit(e -> commitEdit(typeEditor.getNamedType(getItem().getLoadedName())));
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        typeEditor.setEditableName(!isHeaderInFile.test(null));

        typeEditor.setName(getItem().getName(!isHeaderInFile.test(null)));
        typeEditor.setSelected(getItem().getType());
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        boolean empty = getItem() == null;
        updateText(getItem(), empty);

    }

    private void updateText(NamedType item, boolean empty) {
        setContentDisplay(ContentDisplay.TEXT_ONLY);
        if (!empty) {
            setText(item.getName(!isHeaderInFile.test(null)) + " : " + ValueTypeUtils.classToName(item.getType()));
        } else {
            setText("");
        }
    }

    @Override
    protected void updateItem(NamedType item, boolean empty) {
        super.updateItem(item, empty);

        if (isEditing()) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            updateText(item, empty);
        }


    }
}

