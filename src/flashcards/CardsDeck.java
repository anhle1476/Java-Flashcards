package flashcards;

import java.util.HashMap;

public class CardsDeck {
    private HashMap<String, String> deck = new HashMap<>();

    public HashMap<String, String> getDeck() {
        return deck;
    }

    public void addCardToDeck(String name, String definition) {
        deck.put(name, definition);
    }

    public void removeCard(String name) {
        deck.remove(name);
    }

    public String getDefinition(String name) {
        return deck.get(name);
    }

    public boolean isNameExist(String name) {
        return deck.containsKey(name);
    }

    public boolean isDefinitionExist(String definition) {
        return deck.containsValue(definition);
    }

    public String[] getArray() {
        return deck.keySet().toArray(new String[deck.size()]);
    }

    public String findNameFromDefinition(String def) {
        for (String name : deck.keySet()) {
            if (deck.get(name).equals(def)) {
                return name;
            }
        }
        return null;
    }
}
