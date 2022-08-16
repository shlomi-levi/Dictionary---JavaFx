import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class applicationLogic {

    private static Dictionary dict; // A variable that will hold an instance of a dictionary

    // HashMaps from key label to value label / value label to key label (this is for selecting and deleting keys functionality in the gui)
    private static HashMap<Label, Label> keyToValue;
    private static HashMap<Label, Label> valueToKey;

    // keys and values boxes
    private static VBox keysVbox;
    private static VBox valuesVbox;

    private static Label currentKeyChosen = null; // For deleting keys functionality
    private static String searchText = ""; // For searching keys in the dictionary
    private static Stage appStage; // For being able to open the save and load dialogs

    /* Initializing the logic of the program */
    public static void initializeLogic(VBox keysContainer, VBox valuesContainer) {
        dict = new Dictionary(); // creating a new dictionary

        // Creating two hashmaps for the labels in the GUI, so when I'll press one label (key/value label) I could get the object of the matching label
        keyToValue = new HashMap<>();
        valueToKey = new HashMap<>();

        // Saving the keys and values containers in variables, so I'll be able to edit those boxes when needed
        keysVbox = keysContainer;
        valuesVbox = valuesContainer;
    }

    /* Visual stuff. mostly updating keys-values in the boxes */
    public static void initializeVisual() {
        // Clear words and values from the GUI
        keysVbox.getChildren().clear();
        valuesVbox.getChildren().clear();

        // These hashmaps are for the label elements in the GUI, so when I'm clearing the boxes I need to clear them as well
        keyToValue = new HashMap<>();
        valueToKey = new HashMap<>();

        currentKeyChosen = null; // For the ability to delete keys (and their values) from the dictionary

        Set<String> dictSet = dict.keySet();

        for(String word : dictSet) { // The iterator will iterate lexicographic oder
            // Creating two labels to place them in the boxes
            Label wordLabel = new Label(word);
            Label valueLabel = new Label(dict.get(word));

            if (!wordLabel.getText().startsWith(searchText)) // For when the search text is not empty. if it is, It won't affect anything
                continue;

            /* Clicking on words/values to mark them so the client would be able to delete them */
            wordLabel.addEventHandler(MOUSE_CLICKED, applicationLogic::mouseClickedOnKey);
            keysVbox.getChildren().add(wordLabel);

            valueLabel.addEventHandler(MOUSE_CLICKED, applicationLogic::mouseClickedOnValue);
            valuesVbox.getChildren().add(valueLabel);

            /* So the client will have some space to click the labels */
            wordLabel.setPrefWidth(keysVbox.getPrefWidth());
            valueLabel.setPrefWidth(valuesVbox.getPrefWidth());

            /* */

            // HashMap stuff for marking functionality
            keyToValue.put(wordLabel, valueLabel);
            valueToKey.put(valueLabel, wordLabel);

        }
    }

    /* For adding/editing a word. changes the word's value if the word already exists in the dictionary */
    public static void addWord(String word, String value) {
        dict.put(word, value);
    }

    /* For when clicking on a label that represents a key in the dictionary */
    public static void mouseClickedOnKey(Event e) {
        Label key = (Label) e.getSource();
        mouseClicked(key); // Instead of having two separate functions that do the same, I made one that takes a key label and handles that event.
    }

    /* For when clicking on a label that represents a value in the dictionary */
    public static void mouseClickedOnValue(Event e) {
        Label valueLabel = (Label) e.getSource();

        /* Getting the relevant key label since 'mouseClicked' function works with the key label
           This is one of the places in the code where the hashmaps come in to place */

        Label keyLabel = valueToKey.get(valueLabel);
        mouseClicked(keyLabel); // Instead of having two separate functions that do the same, I made one that takes a key label and handles that event.
    }

    /* This is the function that actually handles the click on a label
       This function takes a key label as a parameter */

    public static void mouseClicked(Label keyLabel) {
        /* If a key was already chosen we need to remove its labels backgrounds */
        if (currentKeyChosen != null) {
            currentKeyChosen.setStyle("");
            keyToValue.get(currentKeyChosen).setStyle(""); // Retrieving the value label's object and changing its background.
        }

        final String CSS_STYLE_STRING = "-fx-background-color:rgba(93, 93, 254, 0.5)"; // For changing the labels backgrounds */

        keyLabel.setStyle(CSS_STYLE_STRING); // Changing the key label's background
        keyToValue.get(keyLabel).setStyle(CSS_STYLE_STRING); // Retrieving the value label's object and changing its background
        currentKeyChosen = keyLabel; // Changing the current key chosen (for deletion functionality)
    }

    /* When adding an item to the dictionary. this function won't let the user add an empty key or an empty value */
    public static void onAddItem(ActionEvent event, TextField keyTextField, TextField valueTextField) {

        String key = keyTextField.getText(), value = valueTextField.getText();
        if(key.equals("") || value.equals(""))
            return;

        dict.put(key.toLowerCase(), value); // Put the item in the dictionary. I am putting the keys in lower case to avoid duplicate keys
        initializeVisual(); // Rearrange the items in the boxes

        /* Clearing the text fields */
        keyTextField.clear();
        valueTextField.clear();
    }

    /* When removing an item */
    public static void onRemoveItem(ActionEvent event) {
        if (currentKeyChosen == null) // If no item was selected, ignore
            return;

        String key = currentKeyChosen.getText(); // get the key's string
        dict.remove(key); // remove the key from the dictionary
        initializeVisual(); // Rearrange boxes
    }

    /* When the search text changes */
    public static void onSearchTextChanged(String newText) {
        searchText = newText;
        initializeVisual(); // To show relevant items
    }

    /* Clearing the dictionary */
    public static void onClear() {
        dict = new Dictionary(); // Creating a new empty instance of a dictionary
        initializeVisual(); // For clearing the boxes in this case
    }

    /* For saving the current dictionary */
    public static void onSaveDictionary() {
        /* File Chooser stuff */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt")); // only allow txt files
        File selectedFile = fileChooser.showSaveDialog(appStage);

        // After the dialog window was closed

        if(selectedFile == null) // If no file was selected, return
            return;

        /* Saving to the txt file. I am saving the items in the dictionary as 'DictionaryValue' objects */
        try {
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(selectedFile));

            Set<String> dictSet = dict.keySet();

            for (String word : dictSet) {
                // creating a new dictionary value
                DictionaryValue dictValue = new DictionaryValue(word, dict.get(word));
                writer.writeObject(dictValue);
            }

            writer.close(); // Closing the writer after I'm done saving

        }
        catch(Exception e) { }

    }

    /* For loading a dictionary */
    public static void onLoadDictionary() {
        /* File Chooser stuff */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(appStage);

        if(selectedFile == null) // If no file was selected, return
            return;

        dict = new Dictionary(); // Creating a new dictionary

        /* Trying to load items from the file that was choosen */
        try {
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream(selectedFile)); // stream for reading objects from a file

            while(true) {
                Object obj = reader.readObject();

                if(obj == null) // when we have no more items, stop the loop
                    break;

                DictionaryValue dictValue = (DictionaryValue) obj; // Retrieving an item from the saved file
                dict.put(dictValue.getWord(), dictValue.getValue()); // Saving it in our dictionary
            }

            reader.close(); // close the stream
        }

        catch(Exception e) { }

        initializeVisual(); // Show the items in the boxes
    }

    public static void setStage (Stage stage) { // I need the stage for the save/load dialog functionality
        appStage = stage;
    }
}











