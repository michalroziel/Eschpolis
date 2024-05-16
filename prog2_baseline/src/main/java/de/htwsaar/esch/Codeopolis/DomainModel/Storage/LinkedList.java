package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import org.w3c.dom.Node;

public class LinkedList <T> {
    // First Elm. of the List
    private Node<T> head;
    private int size;

    // Innerclass Node
    public static class Node<T>{
        T content;
        Node<T> next;
        public Node(T content){
            this.content = content;
            this.next = null;
        }
    }

    //Constructor initialize with empty list head = null
    public LinkedList(){
        this.head = null;
        this.size = 0;
    }

    public void addLast(T content){
        Node<T> node = new Node<>(content);
        if(this.head == null){
            this.head = node;
        }else {
            Node<T> temp = this.head;
            while(temp.next != null ){
                temp = temp.next;
            }
            temp.next = node;
        }
        size++;
    }

    public void removeFirst(){
        if(this.head != null){
            this.head = this.head.next;
            size--;
        }
    }

    public boolean isEmpty(){
        return this.head == null;
    }

    public int size(){
        return this.size;
    }

    public T get (int index){
        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }else {
            Node<T> temp = this.head;
            for (int i = 0; i < index; i++) {
                temp= temp.next;
            }
            return temp.content;
        }
    }

    public T set(T content,int index){
        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }else {

            //TODO checken ob es funktioniert
            //idee ist: wen wir 2 ersetzten wollen gehen wir bis 1 speichen uns den content von 2 ab. +
            //Dann erstellen wir einen neuen Knoten der anstelle von 2 sein wird.
            //Darauf sagen wir den neuen Knoten das next = zwei weiter von 1 ist also 3.
            // Nun müssen wir nur noch 1 sagen, dass newNode der next Knoten ist.
            // Es folgt 1 NewNode 3 als Rheinfolge.
            Node<T> temp = this.head;
            for (int i = 0; i < index-1; i++) {
                temp= temp.next;
            }
            T result = temp.next.content;
            Node<T> newNode = new Node<>(content);
            newNode.next = temp.next.next;
            temp.next = newNode;
            return result;
        }
    }

    public void clear(){
        this.head = null;
    }

    public T remove(int index){
        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException();
        }else {

            //TODO checken ob es funktioniert vielleicht ist case beim head remove krittisch
            Node<T> temp = this.head;
            for (int i = 0; i < index-1; i++) {
                temp= temp.next;
            }
            T result = temp.next.content;
            temp.next = temp.next.next;
            return result;
        }
    }

    public int test(){
        int count = 0;
        if (isEmpty()){
            return count;
        }
        Node<T> temp = head;
        while (true){
            if (temp.next == null){
                break;
            }
            temp = temp.next;
            count ++;
        }
        return count;
    }

}
