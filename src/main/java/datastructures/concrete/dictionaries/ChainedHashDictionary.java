package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int numPairs;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = makeArrayOfChains(31);
        this.numPairs = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        for (KVPair<K, V> pair : this.chains[getHashMod(key)]) {
            if (pair.getKey() == key || (pair.getKey() != null && pair.getKey().equals(key))) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        boolean exists = false;
        int hashed = getHashMod(key);
        if (this.chains[hashed] == null) {
            this.chains[hashed] = new ArrayDictionary<K, V>();
        }
        for (KVPair<K, V> pair : this.chains[getHashMod(key)]) {
            if (pair.getKey() == key || (pair.getKey() != null && pair.getKey().equals(key))) {
                this.chains[getHashMod(key)].put(key, value);
                exists = !exists;
            }
        }
        if (!exists) {
            this.numPairs++;
            if (this.numPairs / this.chains.length >= 1) {
                IDictionary<K, V>[] temp = this.chains;
                this.chains = makeArrayOfChains(this.chains.length * 2);
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i] != null) {
                        for (KVPair<K, V> pair : temp[i]) {
                            int newHashed = getHashMod(pair.getKey());
                            if (this.chains[newHashed] == null) {
                                this.chains[newHashed] = new ArrayDictionary<K, V>();
                            }
                            this.chains[getHashMod(pair.getKey())].put(pair.getKey(), pair.getValue());
                        }
                    }
                }
            }
            if (this.chains[getHashMod(key)] == null) {
                this.chains[getHashMod(key)] = new ArrayDictionary<K, V>();
            }
            this.chains[getHashMod(key)].put(key, value);
        }
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        this.numPairs--;
        return this.chains[getHashMod(key)].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        if (this.chains[getHashMod(key)] != null) {
            for (KVPair<K, V> pair : this.chains[getHashMod(key)]) {
                if (pair.getKey() == key || (pair.getKey() != null && pair.getKey().equals(key))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.numPairs;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }
    
    private int getHashMod(K key) {
        int hashed = 0;
        if (key != null) {
            hashed = Math.abs(key.hashCode()) % this.chains.length;
        }
        return hashed;
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int chainsIndex;
        private Iterator<KVPair<K, V>> bucketIter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            for (int i = 0; i < this.chains.length; i++) {
                if (this.chains[i] != null && this.chains[i].iterator().hasNext()) {
                    this.chainsIndex = i;
                    this.bucketIter = this.chains[this.chainsIndex].iterator();
                    i = this.chains.length;
                }                
            }
        }

        @Override
        public boolean hasNext() {     
            if (this.bucketIter == null || !this.bucketIter.hasNext()) {
                for (int i = this.chainsIndex + 1; i < this.chains.length; i++) {
                    if (this.chains[i] != null && this.chains[i].iterator().hasNext()) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (!this.bucketIter.hasNext()) {
                for (int i = this.chainsIndex + 1; i < this.chains.length; i++) {
                    if (this.chains[i] != null && this.chains[i].iterator().hasNext()) {
                        this.chainsIndex = i;
                        i = this.chains.length;
                    }
                }
                this.bucketIter = this.chains[this.chainsIndex].iterator();
            }
            return this.bucketIter.next();
            
        }
    }
}
