/* A class that represents a dictionary */

import java.util.Set;
import java.util.TreeMap;

public class Dictionary {

    private static TreeMap<String, String> dict; // This is how the items will actually be saved

    /* Creates a new dictionary */
    public Dictionary() {
        dict = new TreeMap<>();
    }

    /* Puts an item in the dictionary. an existing item will be replaced */
    public void put(String key, String value) {
        dict.put(key, value);
    }

    /* Returns a value of a key */
    public String get(String word) {
        return dict.get(word);
    }

    /* Returns a set of all the keys in the dictionary */
    public Set<String> keySet() {
        return dict.keySet();
    }

    /* Removes a key from the dictionary (if it exists) */
    public void remove(String key) {
        dict.remove(key);
    }
}
