import java.util.ArrayList;

public class BTree {
    //Initializes BTree
    private int order;
    private BaseNode root;

    public BTree(int order) {
        this.order = order;
        // First node is initially a DictPair Node
        root = new DictPairNode(this.order);
    }
    //For Test Cases
    public BTree(BaseNode keyNode) {
        root = keyNode;
    }

    public void insertKey(KeyValPair key) {
        /**
         * Inserts key into the root node if it's empty
         * Delegates insert operation otherwise
         * @param key to be inserted in the B+Tree
         */
        //Search the B+Tree for the key that's being inserted
        KeyValPair search = searchKey(key.getKey());
        // Case A BTree is empty => First node is a DictPair Node
        if (root.isEmpty())
            root.getKeys()[0] = key;
        // Case B BTree is non empty and there exists a Key for the new KeyValue Pair being inserted
        else if(search!= null) {
            search.addToValues(key.getValue().get(0));
            return;
        }
        // Case B BTree has only one Node - DictPair- which is not full
        else if (root.getChildCount() == 0 && !root.isFull())
            //Insert key into the DictPairRoot
            insertIntoKeysWithShiftBackwards(root.getKeys(), key);
        // Case C BTree has only one node - DictPair - which is full
        else if(root.getChildCount() == 0 && root.isFull()) {
            //Initiate Split
            //Case C.1 Key is not the greatest among existing keys in root
            if(getInsertPosition(root.getKeys(), key) != root.getKeys().length) {
                root = split(root, insertIntoKeysWithShiftBackwards(root.getKeys(), key));
            }
            //Case C.2 Key is the greatest among existing keys in root
            else
                root = split(root, key);
        }
        //Case D Root is a Key Node with children - Traverse to the left/right leaf node for insert
        else if (!root.isDictPairNode() && !root.isEmpty() && root.getChildCount() != 0)
            insertKey(root, key);
    }

    private BaseNode split(BaseNode node, KeyValPair key) {
        /**
         * Handles the case when an insertion into a DictPair node causes overflow and calls for split
         * @param node DictPair node where insertion of key causes overflow
         * @param key key to be inserted into the left DictPair node
         * @return Parent of the split nodes
         */
        // Case A: Full Dict Pair node needs to be spit after insertion
        if(node.isDictPairNode() && node.isFull()) {
            BaseNode l = new DictPairNode(order);
            BaseNode r = new DictPairNode(order);
            int i,j;
            int splitIndex = (int) Math.ceil(order / 2.0);
            KeyValPair [] temp = new KeyValPair[order];
            for (int k = 0; k < order - 1; k++)
                temp[k] = node.getKeys()[k];
            temp[order-1] = new KeyValPair();
            insertIntoKeysWithShiftBackwards(temp,key);
            for(i=0; i< splitIndex - 1; i++)
                l.getKeys()[i] = temp[i];
            for(i = splitIndex - 1, j=0; i<order; i++,j++)
                r.getKeys()[j] = temp[i];
            linkNodes(l, r);
            //Case A.1 Node does NOT have a parent KeyNode
            if(node.getParent() == null) {
                return getNewRoot(l, r, splitIndex, temp[splitIndex - 1]);
            }
            // Case A.2 Node has a parent KeyNode
            else {
                int k;
                BaseNode parent = node.getParent();
                for (k = 0; k < order; k++)
                    if (node == parent.getKeyPointers()[k])
                        break;
                node.getParent().getKeyPointers()[k] = l;
                if (k != 0) {
                    linkNodes(parent.getKeyPointers()[k - 1], l);
                }
                l.setParent(parent);
                return getNewParentAfterKeyNodeInsert(node, r);
            }
        }
        return null;
    }

    private void linkNodes(BaseNode l, BaseNode r) {
        /**
         * Links Input DictPair Nodes backward and forward
         */
        l.setNextNode(r);
        r.setPrevNode(l);
    }

    private BaseNode split(BaseNode node, BaseNode keyNode) {
        /**
         * Handles the case where a split occurs due to addition of a KeyNode to a full KeyNode
         * @return New root node OR null if if node has a parent and a grandparent
         **/
            BaseNode l = new KeyNode(order);
            BaseNode r = new KeyNode(order);
            int i,j;
            int  splitIndex = (int) Math.ceil(order / 2.0);
            KeyValPair [] temp = new KeyValPair[order];
            for (int k = 0; k < order - 1; k++)
                temp[k] = node.getKeys()[k];
            temp[order-1] =  new KeyValPair();
            int position = getInsertPosition(temp, keyNode.getKeys()[0]);
            insertIntoKeysWithShiftBackwards(temp,keyNode.getKeys()[0]);
            //Set Keys for the split children
            for(i=0; i< splitIndex - 1; i++)
                l.getKeys()[i] = temp[i];
            for(i = splitIndex - 1, j=0; i<order; i++,j++)
                r.getKeys()[j] = temp[i];
            //Set KeyPointers to DictPairNodes for the new children
            //Case A KeyNode node is the root node - has no parent
            distributeKeyPointers(node,l,r,keyNode.getKeyPointers()[0],splitIndex,position);
            if(node.getParent() == null)
                return getNewRoot(l, r, splitIndex, temp[splitIndex - 1]);
        //Case B KeyNode node has a Parent - Insert is carried upward
            else {
                l.setParent(node.getParent());
                BaseNode parent = node.getParent();
                l.setParent(parent);
                int pos;
                for(pos = 0; pos < order; pos++)
                    if(parent.getKeyPointers()[pos] == node)
                        break;
                linkDictPairNodes(l);
                parent.getKeyPointers()[pos] = l;
                if (pos == 0)
                    linkChildrenOfTwoNodes(l, parent.getKeyPointers()[pos+1]);
                else
                    linkChildrenOfTwoNodes(parent.getKeyPointers()[pos - 1], l);

                return getNewParentAfterKeyNodeInsert(node, r);
            }
    }

    private BaseNode getNewParentAfterKeyNodeInsert(BaseNode node, BaseNode r) {
        /**
         * Returns new parent after insert of KeyNode r to node's parent
         */
        if (node.getParent() == null) return null;
        BaseNode newKeyNode = new KeyNode(order);
        newKeyNode.getKeys()[0] = r.getKeys()[0];
        if(!r.isDictPairNode())
            banishRedundantKey(r);
        r.setParent(newKeyNode);
        newKeyNode.getKeyPointers()[0] = r;
        node.setParent(insertKey(node.getParent(), newKeyNode));
        return node.getParent();
    }

    private BaseNode getNewRoot(BaseNode l, BaseNode r, int splitIndex, KeyValPair key) {
        /**
         * Creates new Root node after split
         * @param l Left Child of the new root
         * @param r Right Child of the new root
         * @param splitIndex Index at which split happened in an overflowing node
         * @param key Key of the new root KeyNode
         * @return New KeyNode Root after split with l and r as children
         * */
        if(l.isDictPairNode() && r.isDictPairNode()) {
            linkNodes(l, r);
        }
        else if (!l.isDictPairNode() && !r.isDictPairNode())
            if (key.getKey() == r.getKeys()[0].getKey())
                banishRedundantKey(r);

        BaseNode newRoot = new KeyNode(order);
        newRoot.setKeys(0, key);
        l.setParent(newRoot);
        r.setParent(newRoot);
        newRoot.getKeyPointers()[0] = l;
        newRoot.getKeyPointers()[1] = r;
        linkDictPairNodes(newRoot);
        linkChildrenOfTwoNodes(l, r);
        return newRoot;
    }

    private void  banishRedundantKey(BaseNode r) {
        /**
         * Removes Redundant Key while Merging a KeyNode with an upper level KeyNode
         * @param r - KeyNode who's redundant key value needs to be removed
         */
        for (int i = 0; i < order - 2; i++)
            r.getKeys()[i] = new KeyValPair(r.getKeys()[i + 1].getKey());
        r.getKeys()[order - 2] = new KeyValPair();
    }

    private BaseNode insertKey(BaseNode node, BaseNode keyNode) {
        /** Takes care of the case where keyNode with children has to be added to node
         * @return Node with keyNode as one of its children
         * */

        //Case A node can accommodate keyNode
        if(!node.isFull() && node.getChildCount()!= order) {
            insertIntoKeysWithShiftBackwards(node.getKeys(),keyNode.getKeys()[0]);
            int pos = getInsertPosition(node.getKeys(),keyNode.getKeys()[0]);
            int nullPointerPosition = getNullPointerPosition(node.getKeyPointers());
            for(int i = order - 1; i > pos; i--)
                node.getKeyPointers()[i] = node.getKeyPointers()[i - 1];
            node.getKeyPointers()[pos] = keyNode.getKeyPointers()[0];
            if(pos != nullPointerPosition)
                linkNodes(node.getKeyPointers()[pos], node.getKeyPointers()[pos + 1]);
            else
                linkNodes(node.getKeyPointers()[pos - 1], node.getKeyPointers()[pos]);
            keyNode.getKeyPointers()[0].setParent(node);
            return node;
        }
        //Case B node is full
        else
            // Initiate Split
            return split(node,keyNode);
    }
    private void insertKey(BaseNode node, KeyValPair key) {
        /**
         * Finds the appropriate child DictPair Node of KeyNode recursively node to insert key
         * @param node may be a keyNode/DictPairNode
         * @param key is the key pair to be inserted */
        int pos = getInsertPosition(node.getKeys(), key);
            //Case A if the child at pos is a DictPair Node, proceed with insertion in the key field
            if(node.getKeyPointers()[pos].isDictPairNode()) {
                //Case A.1 if the DictPair Leaf Node is full initiate a split
                if (node.getKeyPointers()[pos].isFull()) {
                        //Case A.1.a node is the root node
                        if(node.getParent() == null)
                            root = split(node.getKeyPointers()[pos], key);
                        //Case A.1.n node is not the root
                        else {
                            split(node.getKeyPointers()[pos], key);
                            root = getRootNode(node);
                        }
                }
                //Case A.2 If the DictPair LeafNode is not full, insert the key with split
                else
                insertIntoKeysWithShiftBackwards(node.getKeyPointers()[pos].getKeys(), key);
            }
            //Case B if the child at pos is a KeyNode, go down recursively to find the right DictPair leaf node
            else
                insertKey(node.getKeyPointers()[pos], key);
    }

    private KeyValPair insertIntoKeysWithShiftBackwards(KeyValPair[] arr, KeyValPair key) {
        /**
         * Inserts key in the right position in the input array if space is available else shifts elements one backwards to accomodate key
         * @param arr input array - may or may not be full
         * @param key value to be inserted
         * @return 0.0 if arr can accomodate key OR displaced element on inserting key into arr due to right shift
         * */
        int pos = getInsertPosition(arr, key);

        //Case A arr[pos] is empty
        if(arr[pos].getKey() == Double.POSITIVE_INFINITY) {
            //Fill up arr[pos] with the key
            arr[pos] = key;
            return new KeyValPair(0.0,0.0);
        }
        else {
            //Case B pos is occupied in the keys array => Shift keys backwards
            int end = arr.length - 1;
            while (arr[end].getKey() == Double.POSITIVE_INFINITY)
                end--;
            //Case B.1 Input array is full
            if(end == arr.length - 1) {
                KeyValPair last = arr[end];
                for (int i = end; i > pos; i--)
                    arr[i] = arr[i - 1];
                arr[pos] = key;
                return last;
            }
            //Case B.2 Input Array is not full
            else {
                for (int i = end + 1; i > pos; i--)
                    arr[i] = arr[i - 1];
                //Fill up key in the vacated position
                arr[pos] = key;
            return new KeyValPair(0.0,0.0);
            }
        }
    }

    private int getInsertPosition(KeyValPair[] arr, KeyValPair key) {
        /**
         * Returns Insert Position of key in increasingly sorted input array
         * @param arr input array
         * @param key to be inserted
         * @return Position at which key should be inserted into arr
         */
        int pos = 0;
        while (pos != arr.length && key.getKey() >= arr[pos].getKey())
            pos++;
        return pos;
    }

    public void linkDictPairNodes(BaseNode node) {
        /**
         * Links DictPair nodes of a KeyNode in forward and backward direction
         */
        if(node == null) return;
        if(node.isDictPairNode() && node.getParent() !=null) {
            int l = node.getParent().getKeyCount() + 1;
            if(node.getKeys()[0].getKey() == node.getParent().getKeys()[0].getKey()) l -= 1;
            for(int i = 1; i < l; i++) {
                if(i == getNullPointerPosition(node.getParent().getKeyPointers())) break;
                linkNodes(node.getParent().getKeyPointers()[i - 1], node.getParent().getKeyPointers()[i]);
            }
            return;
        }
        else  {
            int l = node.getKeyCount() + 1;
            for(int i = 0; i < l; i++)
                if(node.getKeyPointers()[i] != null)
                    linkDictPairNodes(node.getKeyPointers()[i]);
        }
    }

    private void linkChildrenOfTwoNodes(BaseNode n1, BaseNode n2){
        /**
         * Establishes links between the rightmost DictPair leaf node of KeyNode n1 and the leftmost DictPair leaf node of Keynode n2
         */
        if(n1 == null || n2 == null) return;
        if(n1.isDictPairNode() || n2.isDictPairNode()) return;
        int l1 = n1.getKeyCount();
        int l2 = n2.getKeyCount();
        if(l1 == 0 || l2 == 0) return;
            if (n1.getKeyPointers()[l1].isDictPairNode() && n2.getKeyPointers()[0].isDictPairNode())
                linkNodes(n1.getKeyPointers()[l1], n2.getKeyPointers()[0]);

    }


    private KeyValPair getLeafForDictPair(BaseNode k, double key) {
        /**
         * Recursively finds the DictPair containing input key
         * @param k root of the B+ Tree
         * @param key with which the DictPair node needs to be searched
         * @return DictPairNode containing key
         */
        int i = getInsertPosition(k.getKeys(), new KeyValPair(key));
        if (k.getKeyPointers()[i] != null) {
            if (k.getKeyPointers()[i].isDictPairNode()) {
                for (KeyValPair j : k.getKeyPointers()[i].getKeys())
                    if (j.getKey() == key)
                        return j;
                return null;
            }
            else
                return getLeafForDictPair(k.getKeyPointers()[i], key);
        }
        return null;
    }

    public BaseNode getLeftMostLeaf() {
        /**
         * All DictPair lead Nodes in a B+ Tree are at the same level and form a doubly linked list
         *  Returning the first node of this doubly linked list would serve printing elements in the given range
         *  The first node is the leftmost DictPair node of the doubly linked list
         *
         *  @return null if root is empty else redirects to getLeftMostLeaf
         */
        if (root.isEmpty())
            return null;
        return getLeftMostLeaf(root);
    }

    private BaseNode getLeftMostLeaf(BaseNode node) {
        /**
         * Recursively finds the left most DictPair Leaf Node
         * @param node BaseNode Interface pointing to a KeyNode
         * @return Leftmost leaf node
         */
        if(node.getKeyPointers()[0].isDictPairNode())
            return node.getKeyPointers()[0];
        else
            return getLeftMostLeaf(node.getKeyPointers()[0]);
    }

    public KeyValPair searchKey(double key) {
        /**
         * Searches for a key in the bTree recursively
         * @para key to be searched
         * @return Reference to the KeyValPair containing key or null
         */
        if (root.isEmpty() || root.getChildCount() == 0) return null;
        KeyValPair search = getLeafForDictPair(root, key);
        if (search != null)
            return search;
        else
            return null;
    }

    public ArrayList<KeyValPair> searchKeyRange(double left, double right) {
        /**
         * Handles following cases of search range provided as input
         * Case 1: Both left and right are in range of the dictpair values
         * Case 2: left is out of range and right is in range
         * Case 3: left is in range and right is out of range
         * Case 4: left and right are both out of range
         * @param left left limit of search range
         * @param right right limit of search range
         * @return ArrayList of elements in the key range
         */
        BaseNode cur = getLeftMostLeaf();
        if(cur == null) return null;
        ArrayList<KeyValPair> rangeKeys = new ArrayList<>();
        while (cur != null) {
            for (KeyValPair i : cur.getKeys())
                if (i.getKey() >= left && i.getKey() <= right)
                    rangeKeys.add(i);
            cur = cur.getNextNode();
        }
        return rangeKeys;
    }

    private void distributeKeyPointers(BaseNode node, BaseNode l , BaseNode r, BaseNode keyPointer, int splitIndex, int pos) {
        /**
         * Distributes the Child Keynodes as the B+Tree grows upwards coming with a new root
         * @param node Node before Insert carried from below
         * @param l left child of node
         * @param r right child of the new root
         * @param keyPointer node who's children need to be distributed between l and r
         * @param splitIndex Index at which split happened in node
         * @param pos Position of Insert before split
         */
        if(pos == splitIndex - 1) {
            for(int i = 0, j=0; i < pos + 1; i++, j++) {
                l.getKeyPointers()[j] = node.getKeyPointers()[i];
                node.getKeyPointers()[i].setParent(l);
            }
            if(order % 2 !=0) {
                r.getKeyPointers()[pos - 1] = keyPointer;
                for (int i = splitIndex, j = 1; i < order; i++, j++) {
                    r.getKeyPointers()[j] = node.getKeyPointers()[i];
                    node.getKeyPointers()[i].setParent(r);
                }
            }
            else {
                l.getKeyPointers()[pos + 1] = keyPointer;
                for (int i = splitIndex, j = 0; i < order; i++, j++) {
                    r.getKeyPointers()[j] = node.getKeyPointers()[i];
                    node.getKeyPointers()[i].setParent(r);
                }
            }
            keyPointer.setParent(r);
        }
        else if(pos < splitIndex - 1) {
            if(order % 2 != 0)
                for (int i = 0, j = 0; i < pos + 1; i++, j++) {
                    if (j == pos + 1) j++;
                    l.getKeyPointers()[j] = node.getKeyPointers()[i];
                    node.getKeyPointers()[i].setParent(l);
                }
            else
                for(int i = 0, j = 0; i < pos + 2; i++, j++) {
                    if(j == pos + 1) j++;
                    l.getKeyPointers()[j] = node.getKeyPointers()[i];
                    node.getKeyPointers()[i].setParent(l);
                }
            l.getKeyPointers()[pos + 1] = keyPointer;
            keyPointer.setParent(l);
            for(int i = splitIndex - 1, j = 0; i < order; i++, j++) {
                r.getKeyPointers()[j] = node.getKeyPointers()[i];
                node.getKeyPointers()[i].setParent(r);
            }
        }
        else if(pos > splitIndex - 1) {
            for(int i = 0, j = 0; i < splitIndex; i++, j++) {
                l.getKeyPointers()[j] = node.getKeyPointers()[i];
                node.getKeyPointers()[i].setParent(l);
            }
            for(int i = splitIndex, j = 0; i < order; i++, j++ ) {
                r.getKeyPointers()[j] = node.getKeyPointers()[i];
                node.getKeyPointers()[i].setParent(r);
            }
            r.getKeyPointers()[getNullPointerPosition(r.getKeyPointers())] = keyPointer;
            keyPointer.setParent(r);
        }

    }

    private int getNullPointerPosition(BaseNode [] arr) {
        /**
         * @return Position of the first null in arr
         */
        int i;
        for(i = 0; i < order; i++)
            if(arr[i] == null)
                break;
        return i;
    }

    private BaseNode getRootNode(BaseNode node) {
        if (node == null) return null;
        if (node.getParent() == null) return node;
        else return getRootNode(node.getParent());
    }

    public BaseNode getRoot() {
        return root;
    }
}
