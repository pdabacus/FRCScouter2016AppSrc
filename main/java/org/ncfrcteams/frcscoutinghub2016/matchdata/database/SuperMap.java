package org.ncfrcteams.frcscoutinghub2016.matchdata.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2/26/2016.
 */
public class SuperMap<K,V> implements Serializable {
    private Map<K,V> map;
    private List<Pair<K,V>> pairList;

    public SuperMap() {
        this.map = new HashMap<>();
        this.pairList = new ArrayList<>();
    }

    public SuperMap(Map<K, V> map) {
        this.map = map;
        this.pairList = new ArrayList<>();
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    public synchronized void put(K key, V value) {
        pairList.add(new Pair<>(key, map.get(key)));
        map.put(key,value);
    }

    public synchronized String undo() {
        if(pairList.size() == 0)
            return "Nothing To Undo";

        int last = pairList.size()-1;
        Pair<K,V> lastState = pairList.get(last);
        pairList.remove(last);
        map.put(lastState.key, lastState.value);
        return "Reverted " + lastState.key + " To " + lastState.value;
    }

    public synchronized void clearHistory(){
        pairList.clear();
    }

    public synchronized SuperMap getCleanClone() {
        return new SuperMap<>(new HashMap<>(map));
    }

    public class Pair<K1,V1> {
        public K1 key;
        public V1 value;

        public Pair(K1 key, V1 value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key.toString() + ", " + value.toString();
        }
    }
}
