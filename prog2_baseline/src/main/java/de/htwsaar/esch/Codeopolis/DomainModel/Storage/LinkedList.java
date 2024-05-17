package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import java.util.NoSuchElementException;

public class LinkedList<T> {
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
    }

    private class LinkedIterator implements Iterator {
        private Node<T> current;


        private LinkedIterator(Node<T> head) {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T content = current.content;
                current = current.next;
                return content;
            } else {
                throw new NoSuchElementException("No next element there!");
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
            if(index == 0){
                T result = head.content;
                head.content= content;
                return result;
            }
            Node<T> temp = this.head;
            for (int i = 0; i < index ; i++) {
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
            if(index == 0){
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

    public int test() {
        int count = 0;
        if (isEmpty()) {
            return count;
        }
        Node<T> temp = head;
        while (true) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
            count++;
        }
        return count;
    }

}
