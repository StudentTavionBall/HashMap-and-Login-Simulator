/*
 * Course: CSC-1120
 * Personal Project 1 - Hashmaps
 * ballt.Hashmap
 * Name: Tavion Ball
 * Last Updated: 03/03/2026
 */
package ballt;


import java.util.Objects;

/**
 * class that creates an open addressing hashmap
 * with a single array with size 1000
 * @param <K> the type of keys maintained by the map
 * @param <V> the type mapped values
 */
public class Hashmap<K, V> {
    private static final int CAPACITY = 1000;
    private final Entry<K, V>[] data;
    private int size;

    /**
     * creates a new hashmap that holds
     * K type keys and
     * V type values
     */
    public Hashmap(){
        data = (Entry<K, V>[]) new Entry[CAPACITY];
        size = 0;
    }

    /**
     * Method to add the key and value entered into a spot in the array
     * @param key that creates the index to set the value in
     * @param value to be stored
     * @return what was stored at the index where value was placed
     * @throws Exception if there is no more space in data
     */
    public V put(K key, V value) throws Exception {
        //Initializes 3 variables val to hold the value to be returned
        V val = null;
        //The key that is generated from the hash function by the Objects class
        int newKey = Math.abs(Objects.hash(key));
        //The index that is created from using the mod function on how big our array is
        //It can only span from 0-999
        int startingIndex = newKey % CAPACITY;
        int currentIndex = startingIndex;
        boolean placed = false;
        //starts a while loop that can wrap around from the end to the start of the array
        do{
            if(data[currentIndex]==null || data[currentIndex].key()==null){
                data[currentIndex] = new Entry<>(key, value);
                placed = true;
            } else if(data[currentIndex].key().equals(key)){
                val = data[currentIndex].value();
                data[currentIndex] = new Entry<>(key, value);
                placed = true;
            }

            currentIndex = (currentIndex+1) % CAPACITY;
        } while(currentIndex!=startingIndex && !placed);
        if(!placed){
            throw new Exception("No more space in hashmap!");
        } else if(val==null){
            size++;
        }
        return val;
    }

    /**
     * Gets the value corresponding to the specific key
     * @param key that corresponds to what value you want
     * @return the value if key is in array else null
     */
    public V get(K key){
        V val = null;
        int newKey = Math.abs(Objects.hash(key));
        int startingIndex = newKey % CAPACITY;
        int currentIndex = startingIndex;
        boolean found = false;
        boolean lost = false;
        do{
            if(data[currentIndex]!=null && data[currentIndex].key()!=null
                    && data[currentIndex].key().equals(key)){
                val = data[currentIndex].value();
                found = true;
            } else if (data[currentIndex]==null) {
                lost = true;
            }
            currentIndex = (currentIndex+1) % CAPACITY;
        } while(currentIndex != startingIndex && !found && !lost);
        return val;
    }

    /**
     * Removes the element specified by the key from data
     * @param key that corresponds to the Entry you want to remove
     * @return value removed at the index corresponding to key
     */
    public V remove(K key){
        V val = null;
        if(get(key)!=null){
            int newKey = Math.abs(Objects.hash(key));
            int startingIndex = newKey % CAPACITY;
            int currentIndex = startingIndex;
            boolean found = false;
            boolean lost = false;
            do{
                if(data[currentIndex]!=null && data[currentIndex].key()!=null
                        && data[currentIndex].key().equals(key)){
                    val = data[currentIndex].value();
                    data[currentIndex] = new Entry<>(null, null);
                    found = true;
                } else if (data[currentIndex]==null) {
                    lost = true;
                }
                currentIndex = (currentIndex+1) % CAPACITY;
            } while(currentIndex != startingIndex && !found && !lost);
            size--;
        }
        return val;
    }

    /**
     * Empties data
     */
    public void clear(){
        for (int i = 0; i < CAPACITY; i++) {
            data[i] = null;
        }
        size = 0;
    }

    /**
     * Gets how many items are in hashmap
     * @return how many items are in hashmap
     */
    public int size(){
        return size;
    }

    private record Entry<K, V>(K key, V value) {
    }
}
