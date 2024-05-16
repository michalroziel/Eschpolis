package de.htwsaar.esch.Codeopolis.DomainModel.Storage;

import org.w3c.dom.Node;

public class LinkedList <T> {
    // First Elm. of the List
    private Node<T> head;

    // Innerclass Node
    private class Node<T>{
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
    }

    public boolean isEmpty(){
        if(head == null){
            return true;
        }else {
            return false;
        }
    }

    public void addLast(){

    }

}
