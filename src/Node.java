public class Node<T> {
    private T data;
    private Node<T> next;

    // GETTERS
    public T getData() {
        return data;
    }

    public Node<T> getNext() {
        return next;
    }

    // SETTERS
    public void setData(T data) {
        this.data = data;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
