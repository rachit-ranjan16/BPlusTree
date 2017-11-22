/**
 * <h1>Base Node Interface for pointing both to a Key Node and DictPairNode in a B+Tree</h1>
 * @author Rachit Ranjan
 * @version 1.0
 *
 */
public interface BaseNode {
    //Methods common to both DictPairNode and KeyNode
    boolean isEmpty();
    boolean isFull();
    boolean isDictPairNode();
    int getKeyCount();
    KeyValPair [] getKeys();
    void setKeys(KeyValPair[] keys);
    void setKeys(int pos, KeyValPair key);
    BaseNode getParent();
    void setParent(BaseNode parent);

    //Methods specific to DictPairNode
    BaseNode getNextNode();
    BaseNode getPrevNode();
    void setPrevNode(BaseNode prevNode);
    void setNextNode(BaseNode nextNode);

    //Methods specific to KeyNodes
    int getChildCount();
    BaseNode[] getKeyPointers();
    void setKeyPointers(BaseNode[] keyPointers);





}
