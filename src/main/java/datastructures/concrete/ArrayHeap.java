package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int INIT_ARRAY_SIZING = 100000;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int length;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(INIT_ARRAY_SIZING);
        length = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (this.length == 0) {
            throw new EmptyContainerException("EmptyContainerException");
        }
        
        T min = this.heap[0];
        
        if (this.length > 1) {
            this.heap[0] = this.heap[this.length - 1];
            this.heap = removeMinHelper(0);
        }
        
        this.length--;
        return min;
    }
    
    private T[] removeMinHelper(int index) {
        int count = 1;
        T min = this.heap[index];
        int minIndex = index;
        int baseIndex = NUM_CHILDREN * index;
        while (baseIndex + count < this.length && this.heap[baseIndex + count] != null && count <= NUM_CHILDREN) {
            int current = baseIndex + count;
            if (leq(this.heap[current], min)) {
                min = this.heap[current];
                minIndex = current;
            }
            count++;
        }
        
        if (index != minIndex) {
            T temp = heap[minIndex];
            this.heap[minIndex] = this.heap[index];
            this.heap[index] = temp;
            this.heap = removeMinHelper(minIndex);
        }
        
        return this.heap;
    }

    @Override
    public T peekMin() {
        if (this.length == 0) {
            throw new EmptyContainerException("EmptyContainerException");
        }
        
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException("IllegalArgumentException: null item");
        }
        
        this.length++;
        
        if (this.length % INIT_ARRAY_SIZING == 0) {
            T[] newHeap = makeArrayOfT(this.length * 2);
            for (int i = 0; i < this.length; i++) {
                newHeap[i] = this.heap[i];
            }
            this.heap = newHeap;
        }
        
        this.heap[this.length - 1] = item;
        
        if (this.length > 1) {
            this.heap = insertHelper(this.length - 1);
        }
    }
    
    private T[] insertHelper(int index) {
        int parentIndex = (index - 1) / NUM_CHILDREN;
        if (leq(this.heap[index], this.heap[parentIndex])) {
            T temp = this.heap[index];
            this.heap[index] = this.heap[parentIndex];
            this.heap[parentIndex] = temp;
            this.heap = insertHelper(parentIndex);
        }
        
        return this.heap;
    }
    
    @Override
    public void remove(T item) {
        int targetIndex = 0;
        for (int i = 0; i < this.length; i++) {
            if (this.heap[i] == item || this.heap[i].equals(item)) {
                targetIndex = i;
                i = this.length;
            }
        }
        
        this.length--;
        this.heap[targetIndex] = this.heap[this.length];
        this.heap = insertHelper(targetIndex);
        this.heap = removeMinHelper(targetIndex);
    }

    @Override
    public int size() {
        return this.length;
    }
    
    private boolean leq(T a, T b) {
        return a.compareTo(b) < 0;
    }
}
