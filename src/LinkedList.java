public class LinkedList<T> {
    private Node<T> list;

    // CONSTRUCTOR
    public LinkedList() {
        list = null;
    }

    public LinkedList(Node<T> node) {
        list = node;
    }

    // GETTER
    public Node<T> getList() {
        return list;
    }

    // SETTER
    public void setList(Node<T> firstNode) {
        list = firstNode;
    }

    // METHODS
    public boolean isEmpty() {
        return list == null;
    }

    public void insertFirst(Node<T> node) {
        node.setNext(list);
        list = node;
    }

    public T removeFirst() throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("In removeFirst() - list is empty");
        }
        Node<T> prevFirst = list;
        T prevChar = prevFirst.getData();
        list = prevFirst.getNext();
        return prevChar;
    }

    // Returns the length of the linked list
    public int length() {
        int count = 0;
        Node<T> node = list;
        while (node != null) {
            count++;
            node = node.getNext();
        }
        return count;
    }

    // Finds the node containing the character argument
    public Node<T> find(T dataToFind) throws ListEmptyException {
        if (isEmpty()) {
            throw new ListEmptyException("In find() - list is empty");
        }
        Node<T> node = list;
        while (node != null && node.getData() != dataToFind) {
            node = node.getNext();
        }
        return node;
    }
}
