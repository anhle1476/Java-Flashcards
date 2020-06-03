package flashcards;

import java.util.ArrayList;
import java.util.HashMap;

public class Stats {
    HashMap<String, Integer> stats = new HashMap<>();

    public void remove(String name) {
        stats.remove(name);
    }

    public void put(String name, Integer times) {
        stats.put(name, times);
    }

    public int get(String name) {
        return stats.get(name);
    }
    public void clear() {
        stats.clear();
    }

    public void addOrAppend(String name) {
        if (stats.containsKey(name)) {
            put(name, stats.get(name) + 1);
        } else {
            put(name, 1);
        }
    }

    public int getValueOrZero(String name) {
        return this.stats.getOrDefault(name, 0);
    }

    public ArrayList<String> getArray() {
        ArrayList<String> array = new ArrayList<>();
        for (String error :  stats.keySet()) {
            if (stats.get(error) > 0) {
                array.add(error);
            }
        }
        return array;
    }
}
