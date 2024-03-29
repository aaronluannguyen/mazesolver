package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private int setIndex;
    private IDictionary<T, Integer> setItems;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        pointers = new int[100];
        setIndex = 0;
        setItems = new ChainedHashDictionary<T, Integer>();
    }

    @Override
    public void makeSet(T item) {
        if (setItems.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        if (this.setIndex >= this.pointers.length) {
            int[] newPointers = new int[this.pointers.length * 2];
            for (int i = 0; i < this.pointers.length; i++) {
                newPointers[i] = this.pointers[i];
            }
            this.pointers = newPointers;
        }
        
        this.pointers[this.setIndex] = -1;
        this.setItems.put(item, this.setIndex);
        this.setIndex++;
    }

    @Override
    public int findSet(T item) {
        if (!this.setItems.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        return findSetHelper(this.setItems.get(item));
    }
    
    private int findSetHelper(int index) {
        if (this.pointers[index] < 0) {
            return index;
        } 
        return findSetHelper(this.pointers[index]);
    }

    @Override
    public void union(T item1, T item2) {
        if (!this.setItems.containsKey(item1) || !this.setItems.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        
        int item1RootID = findSet(item1);
        int item2RootID = findSet(item2);
        
        if (item1RootID == item2RootID) {
            throw new IllegalArgumentException();
        }
        
        int rootRank1 = this.pointers[item1RootID];
        int rootRank2 = this.pointers[item2RootID];
        if (rootRank1 <= rootRank2) {
            this.pointers[item2RootID] = item1RootID;
        } else {
            this.pointers[item1RootID] = item2RootID;
        }
    }
}
