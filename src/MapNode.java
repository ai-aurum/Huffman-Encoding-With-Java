public class MapNode extends Node<Character> {
    private int encd;
    private int length;

    // CONSTRUCTORS
    public MapNode() {
    }

    public MapNode(char data, int encd, int length) {
        setData(data);
        this.encd = encd;
        this.length = length;
    }

    // GETTERS
    public int getEncd() {
        return encd;
    }

    public int getLength() {
        return length;
    }

    // SETTERS
    public void setEncd(int encd) {
        this.encd = encd;
    }

    public void setLength(int length) {
        this.length = length;
    }

    // METHODS

}
