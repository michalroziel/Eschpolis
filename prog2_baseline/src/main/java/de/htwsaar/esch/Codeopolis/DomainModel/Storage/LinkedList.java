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

    public boolean isEmpty(){
        if(head == null){
            return true;
        }else {
            return false;
        }
    }

    public void addLast(){
        if(!isEmpty()){

        }
    }

    public int size(){
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
