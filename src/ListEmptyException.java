public class ListEmptyException extends RuntimeException {
    public ListEmptyException() {
        super();
    }

    public ListEmptyException(String message) {
        super(message);
    }
}
