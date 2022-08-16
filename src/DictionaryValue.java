/* This class represents an item in the dictionary */

import java.io.Serializable;

public class DictionaryValue implements Serializable {

    // Two instance variables, one for the key (word) and one for the value
    private String word;
    private String value;

    // Constructor
    public DictionaryValue(String _word, String _value) {
        this.word = _word;
        this.value = _value;
    }

    /* Getters and Setters */
    public String getWord() {
        return this.word;
    }

    public void setWord(String _word) {
        this.word = _word;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String _value) {
        this.value = _value;
    }
}
