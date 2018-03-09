package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKLessThanZeroException() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        
        try {
            IList<Integer> top = Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testKBiggerThanList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(10, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(list.get(i), top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testWhenKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        
        IList<Integer> top = Searcher.topKSort(0, list);
        assertTrue(top.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testTopKSortMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 249; i >= 0; i--) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(250, list);
        for (int i = 0; i < 250; i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testTopKSortHalfOfMany() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 249; i >= 0; i--) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(100, list);
        for (int i = 0; i < top.size(); i++) {
            assertEquals(150 + i, top.get(i));
        }
    }
}
