package org.example.domain;

public class Node<T> {
    private T data;
    public Node<?> next;
    public Node<?> previous;

    public Node(T data, Node<?> next, Node<?> previous) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isNotLastNode() {
        return data != null;
    }
}
