public class HuffNode extends Node<Character> {
    // char instance: data
    // Node<T> instacne: next
    private int freq;
    private HuffNode left;
    private HuffNode right;
    private Boolean direction; // left = false , right = true

    // CONSTRUCTORS
    public HuffNode() {
    }

    // Constructor for counting the frequencies of a file
    public HuffNode(char data, int freq) {
        setData(data);
        this.freq = freq;
    }

    // Constructor for tree node wtih combined freq
    public HuffNode(HuffNode left, HuffNode right) {
        this.left = left;
        this.right = right;

        // Combine frequencies
        this.freq = left.getFreq() + right.getFreq();
    }

    // GETTERS
    public int getFreq() {
        return freq;
    }

    public HuffNode getLeft() {
        return left;
    }

    public HuffNode getRight() {
        return right;
    }

    public Boolean getDirection() {
        return direction;
    }

    // SETTERS
    public void setFreq(int freq) {
        this.freq = freq;
    }

    public void setLeft(HuffNode left) {
        this.left = left;
    }

    public void setRight(HuffNode right) {
        this.right = right;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }

    // METHODS
    public void incrementFreq() {
        this.freq++;
    }

}
