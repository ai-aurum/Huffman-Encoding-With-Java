public class HuffList extends LinkedList<Character> {
    /*
     * Inherited instances:
     * Node<Character> = list;
     */

    public HuffList sort() {
        // Populate an array with the nodes of the list
        HuffNode[] nodeArray = new HuffNode[this.length()];
        Node<Character> node = getList();
        for (int i = 0; i < nodeArray.length; i++) {
            nodeArray[i] = (HuffNode) node;
            node = node.getNext();
        }

        // Sort the array by freq
        for (int i = 0; i < nodeArray.length - 1; i++) {
            for (int j = i + 1; j < nodeArray.length; j++) {
                if (nodeArray[i].getFreq() > nodeArray[j].getFreq()) {
                    HuffNode temp = nodeArray[i];
                    nodeArray[i] = nodeArray[j];
                    nodeArray[j] = temp;
                }
            }
        }

        // Connect the node within the array sequentially
        for (int i = 0; i < nodeArray.length - 1; i++) {
            nodeArray[i].setNext(nodeArray[i + 1]);
        }
        // Assign null to last node in the linked list
        nodeArray[nodeArray.length - 1].setNext(null);

        // Update list
        setList(getList());
        return this;
    }

    public void insertSorted(HuffNode node) {
        HuffNode lead = (HuffNode) getList();
        HuffNode lag = lead;

        while (lead != null) {
            if (node.getFreq() > lead.getFreq()) {
                lag = lead;
                lead = (HuffNode) lead.getNext();
            } else {
                break;
            }
        }
        node.setNext(lead);
        if (lag == lead) {
            HuffNode listNode = node;
            setList(listNode);
        } else {
            lag.setNext(node);
        }

    }

    // Creates the encoding map from leafs of the encoding tree
    // Executes an inorder traversal to obtain leaf nodes
    public void buildEncdMap(HuffNode current, int charEncd, int depth) {
        if (current != null) {
            Boolean direction = current.getDirection();
            if (direction != null) {
                // Extend charEncd based on the direction of the node (true = right, false is
                // left) ;
                charEncd <<= 1;
                if (direction == true) {
                    charEncd |= 1;
                }
            }

            // Call function with left node of current
            buildEncdMap(current.getLeft(), charEncd, depth + 1);

            // Check if node is a leaf
            if (current.getRight() == null) { // left is null by deduction
                this.insertFirst(new MapNode(current.getData(), charEncd, depth));
            } else {
                buildEncdMap(current.getRight(), charEncd, depth + 1);
            }
        }
    }
}
