import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class applicationController {

    @FXML
    private VBox valuesContainer;

    @FXML
    private VBox keysContainer;

    @FXML
    private TextField keyTextField;

    @FXML
    private TextField valueTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private ScrollPane keysScrollPane;

    @FXML
    private ScrollPane valuesScrollPane;


    public void initialize() {
        /* Adding a listener for text search box */
        searchTextField.textProperty().addListener((observable, oldText, newText) ->
                applicationLogic.onSearchTextChanged(newText));

        /* Making both Scrollbars scroll together */
        keysScrollPane.vvalueProperty().addListener( (a, b, c) -> {
            valuesScrollPane.setVvalue(keysScrollPane.getVvalue());
        });

        valuesScrollPane.vvalueProperty().addListener( (a, b, c) -> {
            keysScrollPane.setVvalue(valuesScrollPane.getVvalue());
        });
        /* */

        applicationLogic.initializeLogic(keysContainer, valuesContainer); // Initialize the program logic
        applicationLogic.initializeVisual(); // Initialize the visual stuff (showing the keys and values in the gui)
    }

    /* When trying to add an item to the dictionary */
    @FXML
    public void onAddItem(ActionEvent event) {
        applicationLogic.onAddItem(event, keyTextField, valueTextField);
    }

    /* When trying to remove an item from the dictionary */
    @FXML
    public void onRemoveItem(ActionEvent event) {
        applicationLogic.onRemoveItem(event);
    }

    /* When trying to save the dictionary */
    @FXML
    public void onSaveDictionary(ActionEvent event) {
        applicationLogic.onSaveDictionary();
    }

    /* When trying to load a dictionary */
    @FXML
    public void onLoadDictionary(ActionEvent event) {
        applicationLogic.onLoadDictionary();
    }

    /* Clearing the program from keys and values */
    @FXML
    public void onClear(ActionEvent event) {
        applicationLogic.onClear();
    }
}
