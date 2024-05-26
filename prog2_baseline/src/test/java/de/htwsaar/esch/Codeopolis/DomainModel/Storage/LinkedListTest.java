package de.htwsaar.esch.codeopolis.DomainModel.Storage;

import de.htwsaar.esch.Codeopolis.DomainModel.Storage.LinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTest {

    @Test
    void addLastAddingAtEnd() {
        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(1);
        myList.addLast(2);
        myList.addLast(3);

        // Check if the size of the list is 3
        assertEquals(3, myList.size());
    }

    @Test
    void removeFirst() {
    }

    @Test
    void isEmpty() {

        LinkedList<Integer> myList = new LinkedList<>();
        assertTrue(myList.isEmpty());

        myList.addLast(1);
        assertFalse(myList.isEmpty());
    }

    @Test
    void size() {

        LinkedList<Integer> myList = new LinkedList<>();
        assertEquals(0, myList.size());

        myList.addLast(1);
        assertEquals(1, myList.size());

        myList.addLast(2);
        assertEquals(2, myList.size());
    }

    @Test
    void get() {

        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(1);
        myList.addLast(2);
        myList.addLast(3);

        assertEquals(1, myList.get(0));
        assertEquals(2, myList.get(1));
        assertEquals(3, myList.get(2));

        assertThrows(IndexOutOfBoundsException.class, () -> myList.get(3));

    }

    @Test
    void set() {

        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(1);
        myList.addLast(2);
        myList.addLast(3);

        //Set the first element to 4
        assertEquals(1, myList.set(4, 0));
        assertEquals(4, myList.get(0));

        assertEquals(2, myList.set(5, 1));
        assertEquals(5, myList.get(1));

        assertEquals(3, myList.set(6, 2));
        assertEquals(6, myList.get(2));

        assertThrows(IndexOutOfBoundsException.class, () -> myList.set(7, 3));
    }

    @Test
    void clear() {

        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(1);
        myList.addLast(2);
        myList.addLast(3);

        myList.clear();
        assertTrue(myList.isEmpty());
    }

    @Test
    void remove() {

        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(1);
        myList.addLast(2);
        myList.addLast(3);

        assertEquals(2, myList.remove(1));
        assertEquals(2, myList.size());
        assertEquals(3, myList.get(1));

        assertEquals(1, myList.remove(0));
        assertEquals(1, myList.size());
        assertEquals(3, myList.get(0));

        assertEquals(3, myList.remove(0));
        assertEquals(0, myList.size());
        assertTrue(myList.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> myList.remove(0));
    }

    @Test
    void test1() {
    }
}