package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *Class that implements Event Handler.
 * @param <T> Type of event.
 */
public class AutoCompleteComboBoxListener<T> implements EventHandler<KeyEvent> {

    private ComboBox<T> comboBox;
    private ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    /**
     *Constructor for AutoCompleteComboBoxListener.
     * @param comboBox combobox that has to be handled.
     */
    public AutoCompleteComboBoxListener(final ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
    }

    /**
     *Method that handles the key event and filters the combobox items to match the key.
     * @param event key pressed event.
     */
    @Override
    public void handle(KeyEvent event) {
        if(comboBox.getEditor().getText().length() == 1 ){
            String letter = comboBox.getEditor().getText();
            comboBox.setPromptText("");
            comboBox.getEditor().setText(letter);
            moveCaret(1);
        }else if(comboBox.getEditor().getText().length() == 0){
            comboBox.getSelectionModel().clearSelection();
            comboBox.getEditor().setPromptText("City");
        }

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing())
                comboBox.show();
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        }
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }
        comboBox.hide();
        deletePressed(event);
        ObservableList<T> list = getSuitableItems();
        String t = comboBox.getEditor().getText();
        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    /**
     *Method that moves the caret to the end of typed text.
     * @param textLength lenght of the text as Integer.
     */
    private void moveCaret(int textLength) {
        if(caretPos == -1)
            comboBox.getEditor().positionCaret(textLength);
        else
            comboBox.getEditor().positionCaret(caretPos);

        moveCaretToPos = false;
    }

    /**
     * Method that populates the list with suitable entries.
     * @return observable list of strings.
     */
    public ObservableList<T> getSuitableItems(){
        ObservableList<T> list = FXCollections.observableArrayList();
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
                    AutoCompleteComboBoxListener.this.comboBox
                            .getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
            }
        }

        return list;
    }

    /**
     * Method that check if delete or backspace buttons were pressed.
     * @param event key pressed event.
     */
    public void deletePressed(KeyEvent event){
        if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

    }

    public void resetAutoComplete(){
        comboBox.getEditor().clear();
        comboBox.getEditor().setPromptText("City");
    }

}
