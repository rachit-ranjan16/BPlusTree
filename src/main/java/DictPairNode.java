public class DictPairNode implements BaseNode {
    private int order;
    private KeyValPair [] keys;
    private BaseNode prevNode;
    private BaseNode nextNode;
    private BaseNode parent;


    public DictPairNode(int order, double key) {
        setup(order);
        keys[0].setKey(key);
    }

    public DictPairNode(int order) {
        setup(order);
    }

    private void setup(int order) {
        this.order = order;
        keys = new KeyValPair[order - 1 ];
        for (int i = 0; i < keys.length; i++)
            keys[i] = new KeyValPair();
        prevNode = null;
        nextNode = null;

    }

    @Override
    public void setPrevNode(BaseNode prevNode) {
        this.prevNode = prevNode;
    }
    @Override
    public void setNextNode(BaseNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public void setKeys(KeyValPair[] keys) {
        this.keys = keys;
    }

    @Override
    public void setKeys(int pos, KeyValPair key) {
        keys[pos] = key;
    }

    @Override
    public KeyValPair[] getKeys() {
        return keys;
    }

    @Override
    public boolean isEmpty() {
        for(KeyValPair i: keys)
            if (i.getKey() != Double.POSITIVE_INFINITY)
                return false;
        return true;
    }

    @Override
    public boolean isFull() {
        for (KeyValPair k : keys)
            if (k.getKey() == Double.POSITIVE_INFINITY)
                return false;
        return true;
    }
    /**
     * @return BaseNode - Next DictPairNode in the doubly linked list of all DictPairNodes at a particular level
     */
    @Override
    public BaseNode getPrevNode() {
        return prevNode;
    }
    /**
     * @return BaseNode - Prev DictPairNode in the doubly linked list of all DictPairNodes at a particular level
     */
    @Override
    public BaseNode getNextNode() {
        return nextNode;
    }

    @Override
    public boolean isDictPairNode() {
        return true;
    }

    @Override
    public BaseNode getParent() {
        return parent;
    }

    @Override
    public void setParent(BaseNode parent) {
        this.parent = parent;
    }
    //Empty Interface Implementations unnecessary for DictPair Leaf Nodes

    @Override
    public BaseNode[] getKeyPointers() {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public void setKeyPointers(BaseNode[] keyPointers) {

    }
    @Override
    public int getKeyCount() {
        int c = 0;
        for(int i = 0; i < keys.length; i++)
            if(keys[i].getKey() != Double.POSITIVE_INFINITY)
                c++;
        return c;
    }

    public String toString() {
        String str = "";
        for(int k = 0; k < keys.length; k++)
            if(keys[k] != null && keys[k].getKey()!= Double.POSITIVE_INFINITY)
                str += keys[k].toString() + "|";
        return str;
    }
}
