package ch.epfl.lpd.store;


import java.util.HashMap;


public class StoreMap
{
    private HashMap<String, String> map;


    public StoreMap() {
        this.map = new HashMap<String, String>();
    }

    public String get(String key) {
        return map.get(key);
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public String toString() {
        return map.toString();
    }
}
