public class KeyNode implements BaseNode{
    private int order;
    private KeyValPair [] keys;
    private BaseNode [] keyPointers;
    private BaseNode parent;

    public KeyNode(int order) {
        this.order = order;
        keys = new KeyValPair[order - 1];
        for (int i = 0; i < keys.length; i++)
            keys[i] = new KeyValPair();
        keyPointers = new BaseNode[order];
    }

    public int getChildCount() {
        /**
         * @return Child count of the invoking Keynode
         */
        int c = 0;
        for(BaseNode b: keyPointers)
            if (b != null)
                c++;
        return c;
    }
    @Override
    public boolean isEmpty() {
        /**
         * @return True if KeyNode is Empty, False otherwise
         */
        for(KeyValPair i: keys)
            if (i.getKey() != Double.POSITIVE_INFINITY)
                return false;
        return true;
    }

    @Override
    public boolean isFull() {
        /**
         * @return True if KeyNode is Full, False otherwise
         */
        for (KeyValPair k : keys)
            if (k.getKey() == Double.POSITIVE_INFINITY)
                return false;
        return true;
    }

    @Override
    public int getKeyCount() {
        /**
         * @return number of keys stored in the KeyNode
         */
        int c = 0;
        for(int i = 0; i < keys.length; i++)
            if(keys[i].getKey() != Double.POSITIVE_INFINITY)
                c++;
        return c;
    }

    @Override
    public KeyValPair[] getKeys() {
        return keys;
    }

    @Override
    public boolean isDictPairNode() {
        return false;
    }

    @Override
    public BaseNode[] getKeyPointers() {
        return keyPointers;
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
    public void setKeyPointers(BaseNode[] keyPointers) {
        this.keyPointers = keyPointers;
    }

    @Override
    public BaseNode getParent() {
        return parent;
    }

    @Override
    public void setParent(BaseNode parent) {
        this.parent = parent;
    }

    public String toString() {
        String str = "";
        for(int k = 0; k < keys.length; k++)
            if(keys[k] != null && keys[k].getKey()!= Double.POSITIVE_INFINITY)
                str += keys[k].toString() + "|";
        return str;
    }

    // Empty Interface Implementations unnecessary for Key Pointer Nodes
    @Override
    public BaseNode getPrevNode() {
        return null;
    }

    @Override
    public BaseNode getNextNode() {
        return null;
    }

    @Override
    public void setPrevNode(BaseNode prevNode) {

    }

    @Override
    public void setNextNode(BaseNode nextNode) {

    }

}
