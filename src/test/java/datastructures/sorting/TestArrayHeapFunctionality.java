package datastructures.sorting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    

    protected IPriorityQueue<Integer> makeBasic() {
        IPriorityQueue<Integer> basic = this.makeInstance();
        basic.insert(1);
        basic.insert(2);
        basic.insert(3);
        basic.insert(4);
        basic.insert(5);
        return basic;
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testForGaps() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 0; i < 50; i++) {
            test.insert(i);
        }
        
        for (int i = 0; i < 50; i++) {
            assertEquals(i, test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyContainerException() {
        IPriorityQueue<Integer> empty = this.makeInstance();
        try {
            empty.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testSingleRemove() {
        IPriorityQueue<Integer> test = this.makeBasic();
        assertEquals(1, test.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMany() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 0; i < 250; i++) {
            test.insert(i);
        }
        
        for (int i = 0; i < 250; i++) {
            assertEquals(i, test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveFromHeapWithSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        assertEquals(1, heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveEqualElements() {
        IPriorityQueue<Integer> heap = this.makeBasic();
        heap.insert(1);
        heap.insert(1);        
        
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinEmptyContainerException() {            
        IPriorityQueue<Integer> empty = this.makeInstance();
        try {
            empty.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinHeapSizeOne() {
        IPriorityQueue<Integer> test = this.makeInstance();
        test.insert(5);
        assertEquals(5, test.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinMany() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 0; i < 250; i++) {
            test.insert(i);
        }
        
        for (int i = 0; i < 250; i++) {
            assertEquals(i, test.peekMin());
            test.removeMin();
        }
    }
    
    @Test(timeout=SECOND) 
    public void testPeekMinTie() {
        IPriorityQueue<Integer> heap = this.makeBasic();
        heap.insert(1);
        heap.insert(1); 
        
        assertEquals(1, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertNullException() {
        IPriorityQueue<Integer> test = this.makeInstance();
        try {
            test.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND) 
    public void testInsertToEmptyHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        assertEquals(2, heap.peekMin());
        assertEquals(2, heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertNewMin() {
        IPriorityQueue<Integer> test = this.makeBasic();
        test.insert(0);
        assertEquals(0, test.peekMin());
        
        test.insert(-5);
        assertEquals(-5, test.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertMany() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 0; i < 250; i++) {
            test.insert(i);
        }
        
        for (int i = 0; i < 250; i++) {
            assertEquals(i, test.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertAnEqualElement() {
        IPriorityQueue<Integer> heap = this.makeBasic();
        heap.insert(1);
        heap.insert(1);
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
    }
    public void testInsertManyNewMins() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 250; i >= 0; i--) {
            test.insert(i);
        }
        
        for (int i = 0; i <= 250; i++) {
            assertEquals(i, test.removeMin());
        }
    }
    
    
    @Test(timeout=SECOND)
    public void testSizeZero() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testSizeOne() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND) 
    public void testSizeMany() {
        IPriorityQueue<Integer> test = this.makeInstance();
        for (int i = 0; i < 250; i++) {
            test.insert(i);
            assertEquals(i + 1, test.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testIsEmptyTrue() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testIsEmptyFalse() {
        IPriorityQueue<Integer> heap = this.makeBasic();
        assertFalse(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testSimpleRemove() {
        IPriorityQueue<Integer> heap = this.makeBasic();
        IPriorityQueue<Integer> heapTest = this.makeInstance();
        heapTest.insert(1);
        //heapTest.insert(2);
        //heapTest.insert(3);
        //heapTest.insert(4);
        heapTest.insert(5);
        heap.remove(4);
        heap.remove(3);
        heap.remove(2);

        while (!heap.isEmpty()) {
            assertEquals(heap.removeMin(), heapTest.removeMin());
        }
    }
}
