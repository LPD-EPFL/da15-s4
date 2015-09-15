package ch.epfl.lpd.store;


import java.util.Map;
import java.util.HashMap;


public class StoreMap
{
    private Map<Integer, Integer> map;


    public StoreMap() {
        this.map = new HashMap<Integer, Integer>();
    }

    public Integer get(Integer key) {
        return map.get(key);
    }

    public void put(Integer key, Integer value) {
        map.put(key, value);
    }

    public void remove(Integer key) {
        map.remove(key);
    }

    public String toString() {
        return map.toString();
    }
}
