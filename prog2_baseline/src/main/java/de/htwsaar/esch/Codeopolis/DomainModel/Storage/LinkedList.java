package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LinkedList<T extends Comparable> {
    // First Elm. of the List
    private Node<T> head;
    private int size;

    // Inner class Node
    public static class Node<T> {
        T content;
        Node<T> next;

        public Node(T content) {
            this.content = content;
            this.next = null;
        }
    }

    public interface Iterator<T> {
        /**
         * Checks if there are further objects available for iteration.
         *
         * @return {@code true} if more objects are available; {@code false} otherwise.
         */
        boolean hasNext();

        /**
         * Returns the next {@link Silo.Status} object in the iteration.
         * This method should only be called if {@code hasNext()} returns {@code true}.
         *
         * @return The next {@link Silo.Status} object.
         * @throws NoSuchElementException if no more elements are available.
         */
        T next();

        void remove();

    }

    public class LinkedIterator<T> implements Iterator {
        private Node<T> current;
        private Node<T> previous;
        private Node<T> beforePrevious; // Keeps track of the node before 'previous' to update the link when removing

        public LinkedIterator(Node<T> head) {
            this.current = head;
            this.previous = null;
            this.beforePrevious = null;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Move the pointers forward
            T content = current.content;
            beforePrevious = previous;
            previous = current;
            current = current.next;
            return content;
        }

        @Override
        public void remove() {
            if (previous == null) {
                throw new IllegalStateException("next() has not been called or remove() already called after the last next()");
            }

            if (beforePrevious == null) {
                // If `previous` was the first node
                // Update the head of the list to skip the removed node
                previous = null; // Reset previous to avoid illegal state on subsequent calls
            } else {
                // Update the link to skip the removed node
                beforePrevious.next = current;
                previous = null; // Reset previous to avoid illegal state on subsequent calls
            }
        }
    }


    //Constructor initialize with empty list head = null
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void addLast(T content) {
        Node<T> node = new Node<>(content);
        if (this.head == null) {
            this.head = node;
        } else {
            Node<T> temp = this.head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
        }
        size++;
    }

    public void removeFirst() {
        if (this.head != null) {
            this.head = this.head.next;
            size--;
        }
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public int size() {
        return this.size;
    }

    public T get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> temp = this.head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.content;
        }
    }

    public T set(T content, int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                T result = head.content;
                head.content = content;
                return result;
            }
            Node<T> temp = this.head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            T result = temp.content;
            temp.content = content;
            return result;
        }
    }

    public void clear() {
        this.head = null;
    }

    public T remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index == 0) {
                T result = head.content;
                size--;
                head = head.next;
                return result;
            }
            Node<T> temp = this.head;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.next;
            }
            T result = temp.next.content;
            temp.next = temp.next.next;
            this.size--;
            return result;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //////////// BubbleSort ////////////////////////////////////////////////////
    ////////////

    /**
     * BubbleSort (Sorting through swapping elements)
     *
     * @param a array to be sorted
     * @return sorted array
     */

    static int[] bubbleSort(int[] a) {
        // sorted area a[i] ... a[n-1]
        for (int i = a.length; i > 0; i--) {
            // unsorted area a[0] ... a[i-1]
            for (int j = 0; j < i - 1; j++) {
                // swap a[j] and a[j+1] if a[j] > a[j+1] (ascending order)
                if (a[j] > a[j + 1]) {
                    // swap a[j] and a[j+1]
                    int t = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = t;
                }
            }
        }
        return a;
    }

    public LinkedIterator makeIterator() {
        return new LinkedIterator(this.head);
    }

    public void sort() {
        if (size > 1) {
            boolean wasChanged;

            do {
                Node<T> current = head;
                Node<T> previous = null;
                Node<T> next = current.next;
                wasChanged = false;

                while (next != null) {

                    // compare if coompareTo yields 1 which means that the current is greater than the next
                    if (current.content.compareTo(next.content) > 0) {
                        wasChanged = true;

                        if (previous != null) {
                            Node<T> sig = next.next;

                            previous.next = next;
                            next.next = current;
                            current.next = sig;
                        } else {
                            Node<T> sig = next.next;

                            head = next;
                            next.next = current;
                            current.next = sig;
                        }

                        previous = next;
                        next = current.next;
                    } else {
                        previous = current;
                        current = next;
                        next = next.next;
                    }
                }
            } while (wasChanged);
        }
    }

    public LinkedList<T> filter(Predicate<? super T> predicate) {

        // create a new list to save elements that have been filtered
        LinkedList<T> afterFilterList = new LinkedList<T>();

        // create an iterator to iterate over the list
        LinkedIterator<T> iterator = this.makeIterator();
        // while not reached the end
        while (iterator.hasNext()) {
            // get the next element as type T
            T content = iterator.next();
            // check if the predicate returns true
            if (predicate.test(content)) {
                // add element to the list
                afterFilterList.addLast(content);
            }
        }

        return afterFilterList;
    }


    public void forEach(Consumer<? super T> consumer) {

        // create a new LinkedList to be able to
        // LinkedList listToConsume = new LinkedList();

        // create an iterator to be able to cycle through all the elements and apply the consumer
        LinkedIterator<T> consumerIterator = this.makeIterator();

        while (consumerIterator.hasNext()) {

            // set content to represent the current element
            T content = consumerIterator.next();

            //perform the given operation on the element
            consumer.accept(content);

        }

    }

    public void removeIf(Predicate<? super T> predicate) {

        LinkedIterator<T> myIterator = this.makeIterator();

        while (myIterator.hasNext()) {

            T content = myIterator.next();

            if (predicate.test(content)) myIterator.remove();

        }

    }

    public void addIf(Predicate<? super T> predicate, T newElement) {

        if (predicate.test(newElement)) {

            this.addLast(newElement);

        }
    }

    public void sort(Comparator<? super T> comparator) {

        if (size > 1) {
            boolean wasChanged;

            do {
                Node<T> current = head;
                Node<T> previous = null;
                Node<T> next = current.next;
                wasChanged = false;

                while (next != null) {

                    // compare if coompareTo yields 1 which means that the current is greater than the next

                    // if (current.content.compareTo(next.content) > 0) {


                    // TODO: now using the comparator !
                    if (comparator.compare(current.content, next.content) > 0) {

                        wasChanged = true;

                        if (previous != null) {
                            Node<T> sig = next.next;

                            previous.next = next;
                            next.next = current;
                            current.next = sig;
                        } else {
                            Node<T> sig = next.next;

                            head = next;
                            next.next = current;
                            current.next = sig;
                        }

                        previous = next;
                        next = current.next;
                    } else {
                        previous = current;
                        current = next;
                        next = next.next;
                    }
                }
            } while (wasChanged);
        }


    }


}
