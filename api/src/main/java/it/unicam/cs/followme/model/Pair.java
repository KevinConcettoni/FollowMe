package it.unicam.cs.followme.model;

/**
 * Classe che associa associa due valori tra di loro (chiave - valore)
 * @param <K> la chiave
 * @param <V> il valore da associare
 */
public class Pair<K,V> {

    private final K key;
    private final V value;

    /**
     * Coppia di valori non nulli
     * @param k primo elemento (chiave)
     * @param v secondo elemento (valore)
     */
    public Pair(K k, V v){
        this.key = k;
        this.value = v;
    }

    public K getKey(){
        return this.key;
    }

    public V getValue(){
        return this.value;
    }


}
