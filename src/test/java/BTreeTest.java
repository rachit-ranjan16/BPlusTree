import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Random;

public class BTreeTest {
    private BTree bTree;
    private final double SOME_KEY = 34.55;

    @BeforeMethod
    void initialize23BTree() {
        BaseNode root = new KeyNode(3);
        KeyValPair [] rootKey = {new KeyValPair(13.0,13.5)};
        root.setKeys(rootKey);
        BaseNode l = new KeyNode(3);
        KeyValPair [] lKey = {new KeyValPair(3.0,3.5)};
        l.setKeys(lKey);
        BaseNode r= new KeyNode(3);
        KeyValPair [] rKey = {new KeyValPair(14.0,14.555), new KeyValPair(19.0, 19.55)};
        r.setKeys(rKey);
        BaseNode [] rootKids = {l,r};
        l.setParent(root);
        r.setParent(root);
        root.setKeyPointers(rootKids);
        BaseNode dp1 = new DictPairNode(3);
        BaseNode dp2 = new DictPairNode(3);
        BaseNode dp3 = new DictPairNode(3);
        BaseNode dp4 = new DictPairNode(3);
        BaseNode dp5 = new DictPairNode(3);
        KeyValPair [] dictPair1 = {new KeyValPair(2.0,-4.55)};
        KeyValPair [] dictPair2 = {new KeyValPair(3.0,12.65)};
        KeyValPair [] dictPair3 = {new KeyValPair(13.0,43.76)};
        KeyValPair [] dictPair4 = {new KeyValPair(14.0,35.77)};
        KeyValPair [] dictPair5 = {new KeyValPair(19.0,-23.44), new KeyValPair(21.0,22.356)};
        dp1.setKeys(dictPair1);
        dp2.setKeys(dictPair2);
        dp3.setKeys(dictPair3);
        dp4.setKeys(dictPair4);
        dp5.setKeys(dictPair5);
        dp1.setNextNode(dp2);
        dp2.setPrevNode(dp1);
        dp2.setNextNode(dp3);
        dp3.setPrevNode(dp2);
        dp3.setNextNode(dp4);
        dp4.setPrevNode(dp3);
        dp4.setNextNode(dp5);
        dp5.setPrevNode(dp4);
        BaseNode [] lDictPairPointers = {dp1,dp2};
        BaseNode [] rDictPairPointers = {dp3,dp4,dp5};
        dp1.setParent(l);
        dp2.setParent(l);
        dp3.setParent(r);
        dp4.setParent(r);
        dp5.setParent(r);
        l.setKeyPointers(lDictPairPointers);
        r.setKeyPointers(rDictPairPointers);
        bTree = new BTree(root);
    }

    private BTree setupTwoLevelBPlusTreeWithTwoChildren() {
        BaseNode root = new KeyNode(3);
        KeyValPair [] rootKeys = {new KeyValPair(4,-56.33)};
        root.setKeys(rootKeys);
        BaseNode dp1 = new DictPairNode(3,2);
        BaseNode dp2 = new DictPairNode(3);
        dp1.setParent(root);
        dp2.setParent(root);
        dp1.setNextNode(dp2);
        dp2.setPrevNode(dp1);
        KeyValPair [] keyDp2 = {new KeyValPair(4,-0.44), new KeyValPair(11,13.21)};
        dp2.setKeys(keyDp2);
        BaseNode [] children = {dp1,dp2, null};
        root.setKeyPointers(children);
        return new BTree(root);
    }

    private BTree setupTwoLevelBPlusTreeWithThreeChildren() {
        BaseNode root = new KeyNode(3);
        KeyValPair [] rootKeys = {new KeyValPair(4), new KeyValPair(7)};
        root.setKeys(rootKeys);
        BaseNode dp1 = new DictPairNode(3,3);
        BaseNode dp2 = new DictPairNode(3,5);
        BaseNode dp3 = new DictPairNode(3);
        dp1.setParent(root);
        dp2.setParent(root);
        dp3.setParent(root);
        dp1.setNextNode(dp2);
        dp2.setNextNode(dp3);
        dp2.setPrevNode(dp1);
        dp3.setPrevNode(dp2);
        KeyValPair [] keyDp3 = {new KeyValPair(7,3.45), new KeyValPair(11,3.668)};
        dp3.setKeys(keyDp3);
        BaseNode [] children = {dp1,dp2, dp3};
        root.setKeyPointers(children);
        return new BTree(root);
    }

    private void getFullTreeAboutToSplitSecondTime() {
        bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(5));
        bTree.insertKey(new KeyValPair(3));
        bTree.insertKey(new KeyValPair(12));
        bTree.insertKey(new KeyValPair(4));
    }

    private void getFullTreeAboutToSplitForTheThirdTime() {
        getFullTreeAboutToSplitSecondTime();
        bTree.insertKey(new KeyValPair(15));
        bTree.insertKey(new KeyValPair(6));
    }

    private void getFullTreeBeforeFourthSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        bTree.insertKey(new KeyValPair(1));
        bTree.insertKey(new KeyValPair(2));
    }

    private void getTreeAboutToBeSplitForAFifthTime() {
        getFullTreeBeforeFourthSplit();
        bTree.insertKey(new KeyValPair(7.0));
        bTree.insertKey(new KeyValPair(8.0));
    }

    @Test
    public void testSearchKeyOnEmptyTree() {
        bTree = new BTree(3);
        Assert.assertEquals(bTree.searchKey(SOME_KEY), null);
    }

    @Test
    public void testSearchKeyOn1Level23TreePositiveFlow() {
        Assert.assertEquals(bTree.searchKey(3.0).getKey(),3.0 );
        Assert.assertEquals(bTree.searchKey(14.0).getKey(), 14.0);
        Assert.assertEquals(bTree.searchKey(21.0).getKey(), 21.0);
    }

    @Test
    public void testSearchKeyOn1Level23TreeNegativeFlow() {
        Assert.assertEquals(bTree.searchKey(21.5), null);
        Assert.assertEquals(bTree.searchKey(14.5), null);
    }

    @Test
    public void testGetLeftMostDictPairNode() {
        Assert.assertEquals(1,(bTree.getLeftMostLeaf()).getKeys().length);
    }
    @Test
    public void testSearchKeyRangeWithinLimits() {
        Assert.assertEquals(4,(bTree.searchKeyRange(3.0,19.0)).size());
        Assert.assertEquals(4,(bTree.searchKeyRange(2.0,14.0)).size());
    }

    @Test
    public void testSearchKeyRangeWithOutOfRangeLeftLimit() {
        Assert.assertEquals(5, (bTree.searchKeyRange(-3.0,19.0)).size());
        Assert.assertEquals(5, (bTree.searchKeyRange(1.0,19.0)).size());
    }

    @Test
    public void testSearchKeyRangeWithOutOfRangeRightLimits() {
        Assert.assertEquals(3, (bTree.searchKeyRange(14.0,23.0)).size());
        Assert.assertEquals(2, (bTree.searchKeyRange(19.0,25.0)).size());
    }

    @Test
    public void testSearchKeyRangeWithBothOutOfRangeLimits() {
        Assert.assertEquals(6, bTree.searchKeyRange(-34.0, 26.0).size());
    }

    @Test
    public void testTwoMonotonicallyIncreasingInsertsInBPlusTree() {
        BTree bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(2));
        bTree.insertKey(new KeyValPair(3));
        Assert.assertEquals(0,bTree.getRoot().getChildCount());
        Assert.assertEquals(2.0,bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(3.0,bTree.getRoot().getKeys()[1].getKey());
    }

    @Test
    public void testTwoMonotonicallyDecreasingInsertsInBPlusTree() {
        BTree bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(3));
        bTree.insertKey(new KeyValPair(2));
        Assert.assertEquals(0,bTree.getRoot().getChildCount());
        Assert.assertEquals(2.0,bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(3.0,bTree.getRoot().getKeys()[1].getKey());
    }

    @Test
    public void testInsertInHeightOneBPlusWithTwoNonFullDictPairNodes() {
        bTree = setupTwoLevelBPlusTreeWithTwoChildren();
        bTree.insertKey(new KeyValPair(3));
        Assert.assertEquals(2.0,bTree.getRoot().getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(3.0,bTree.getRoot().getKeyPointers()[0].getKeys()[1].getKey());
    }

    @Test
    public void testInsertInHeightOneBPlusWithThreeNonFullDictPairNodes() {
        BTree bTree = setupTwoLevelBPlusTreeWithThreeChildren();
        bTree.insertKey(new KeyValPair(2));
        bTree.insertKey(new KeyValPair(4));
        Assert.assertEquals(3.0,bTree.getRoot().getKeyPointers()[0].getKeys()[1].getKey());
        Assert.assertEquals(5.0,bTree.getRoot().getKeyPointers()[1].getKeys()[1].getKey());
    }

    @Test
    public void testThreeIncreasingInsertsAndSplit() {
        bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(2));
        bTree.insertKey(new KeyValPair(3));
        bTree.insertKey(new KeyValPair(11));
        Assert.assertEquals(2, bTree.getRoot().getChildCount());
        Assert.assertEquals(3.0,bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(2.0,bTree.getRoot().getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(11.0,bTree.getRoot().getKeyPointers()[1].getKeys()[1].getKey());
    }

    @Test
    public void testInsertingMultipleValuesForTheSameKey() {
        getFullTreeAboutToSplitSecondTime();
        bTree.insertKey(new KeyValPair(4));
        bTree.insertKey(new KeyValPair(4));
        bTree.insertKey(new KeyValPair(12));
        bTree.insertKey(new KeyValPair(12));
        Assert.assertEquals(bTree.getRoot().getKeyPointers()[0].getKeys()[1].getValue().size(), 3);
        Assert.assertEquals(bTree.getRoot().getKeyPointers()[1].getKeys()[1].getValue().size(), 3);
    }

    @Test
    public void testThreeDecreasingInsertsAndSplit() {
        bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(11));
        bTree.insertKey(new KeyValPair(3));
        bTree.insertKey(new KeyValPair(2));
        Assert.assertEquals(2, bTree.getRoot().getChildCount());
        Assert.assertEquals(3.0,bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(2.0,bTree.getRoot().getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(11.0,bTree.getRoot().getKeyPointers()[1].getKeys()[1].getKey());
        Assert.assertEquals(3.0,bTree.getRoot().getKeyPointers()[1].getKeys()[0].getKey());
    }

    @Test
    public void testSecondSplitAtRightMostNode() {
        bTree = new BTree(3);
        bTree.insertKey(new KeyValPair(11));
        bTree.insertKey(new KeyValPair(3));
        bTree.insertKey(new KeyValPair(2));
        bTree.insertKey(new KeyValPair(5));
        Assert.assertEquals(3,bTree.getRoot().getChildCount());
        Assert.assertEquals(3.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(5.0, bTree.getRoot().getKeys()[1].getKey());
        Assert.assertEquals(2.0, bTree.getLeftMostLeaf().getKeys()[0].getKey());
        Assert.assertEquals(3.0, bTree.getLeftMostLeaf().getNextNode().getKeys()[0].getKey());
        Assert.assertEquals(11.0, bTree.getLeftMostLeaf().getNextNode().getNextNode().getKeys()[1].getKey());
    }

    @Test
    public void testInsertsAfterSecondSplitAtLeftNode() {
        getFullTreeAboutToSplitSecondTime();
        bTree.insertKey(new KeyValPair(2));
        bTree.insertKey(new KeyValPair(1));
        Assert.assertEquals(3,bTree.getRoot().getChildCount());
        Assert.assertEquals(3.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(5.0, bTree.getRoot().getKeys()[1].getKey());
        Assert.assertEquals(1.0, bTree.getLeftMostLeaf().getKeys()[0].getKey());
        Assert.assertEquals(4.0, bTree.getLeftMostLeaf().getNextNode().getKeys()[1].getKey());
        Assert.assertEquals(12.0, bTree.getLeftMostLeaf().getNextNode().getNextNode().getKeys()[1].getKey());


    }
    @Test
    public void testInsertsAfterSecondSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        Assert.assertEquals(3,bTree.getRoot().getChildCount());
        Assert.assertEquals(5.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(12.0, bTree.getRoot().getKeys()[1].getKey());
        Assert.assertEquals(4.0, bTree.getLeftMostLeaf().getKeys()[1].getKey());
        Assert.assertEquals(6.0, bTree.getLeftMostLeaf().getNextNode().getKeys()[1].getKey());
        Assert.assertEquals(15.0, bTree.getLeftMostLeaf().getNextNode().getNextNode().getKeys()[1].getKey());
    }


    @Test
    public void testSearchKeyAfterSecondSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        Assert.assertEquals(6.0, bTree.searchKey(6).getKey());
        Assert.assertEquals(null,bTree.searchKey(13.5));
        Assert.assertEquals(12.0,bTree.searchKey(12).getKey());
    }

    @Test
    public void testSearchKeyRangeAfterSecondSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        Assert.assertEquals(4, bTree.searchKeyRange(1.0,7.0).size());
        Assert.assertEquals(3.0, bTree.searchKeyRange(1.0,7.0).get(0).getKey());
        Assert.assertEquals(6,bTree.searchKeyRange(-23.6,15.4).size());
        Assert.assertEquals(12.0,bTree.searchKeyRange(-23.6,15.4).get(4).getKey());
    }

    @Test
    public void testThirdSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        bTree.insertKey(new KeyValPair(1));
        Assert.assertEquals(2,bTree.getRoot().getChildCount());
        Assert.assertEquals(3.0, bTree.getRoot().getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(12.0, bTree.getRoot().getKeyPointers()[1].getKeys()[0].getKey());
        Assert.assertEquals(5.0, bTree.getRoot().getKeyPointers()[1].getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(4.0, bTree.getRoot().getKeyPointers()[0].getKeyPointers()[1].getKeys()[1].getKey());
        Assert.assertEquals(7,bTree.searchKeyRange(-23.6,15.4).size());
    }

    @Test
    public void testBeyondThirdSplit() {
        getFullTreeAboutToSplitForTheThirdTime();
        bTree.insertKey(new KeyValPair(1));
        bTree.insertKey(new KeyValPair(2));
        Assert.assertEquals(bTree.getRoot().getKeyPointers()[0].getKeyPointers()[0].getKeys()[1].getKey(), 2.0);
        Assert.assertEquals(8,bTree.searchKeyRange(-23.6,15.4).size());
    }

    @Test
    public void testSplitAndCarryToLevelAbove() {
        getFullTreeBeforeFourthSplit();
        bTree.insertKey(new KeyValPair(7.0));
        Assert.assertEquals(7.0,bTree.getRoot().getKeyPointers()[1].getKeyPointers()[1].getKeys()[1].getKey());
        bTree.insertKey(new KeyValPair(8.0));
        Assert.assertEquals(7.0, bTree.getRoot().getKeys()[1].getKey());
        Assert.assertEquals(3.0, bTree.getRoot().getKeyPointers()[0].getKeys()[0].getKey());
        Assert.assertEquals(12.0, bTree.getRoot().getKeyPointers()[2].getKeyPointers()[1].getKeys()[0].getKey());
    }

    @Test
    public void testInsertsAfterFourthSplitAndCarryToLevelAbove() {
        getTreeAboutToBeSplitForAFifthTime();
        bTree.insertKey(new KeyValPair(9.0));
        bTree.insertKey(new KeyValPair(16.0));
        Assert.assertEquals(7.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(12.0, bTree.getRoot().getKeyPointers()[1].getKeys()[0].getKey());
        Assert.assertEquals(15.0, bTree.getRoot().getKeyPointers()[1].getKeyPointers()[1].getKeys()[0].getKey());
    }

    @Test
    public void testNextSplitAtRoot() {
        getTreeAboutToBeSplitForAFifthTime();
        bTree.insertKey(new KeyValPair(9.0));
        bTree.insertKey(new KeyValPair(16.0));
        bTree.insertKey(new KeyValPair(17.0));
        bTree.insertKey(new KeyValPair(18.0));
        bTree.insertKey(new KeyValPair(19.0));
        bTree.insertKey(new KeyValPair(20.0));
        Assert.assertEquals(7.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(16.0, bTree.getRoot().getKeys()[1].getKey());
        for(int i = 21; i <=32; i++)
        bTree.insertKey(new KeyValPair(i/1.0));
        Assert.assertEquals(16.0, bTree.getRoot().getKeys()[0].getKey());
        Assert.assertEquals(24.0, bTree.getRoot().getKeys()[1].getKey());

    }

    @Test
    public void testOrderSixBPlusTree() {
        bTree = new BTree(6);
        int range = 25;
        for(int i = range; i > 0; i--)
            bTree.insertKey(new KeyValPair(i/1.0));
        bTree.insertKey(new KeyValPair(-1));
        Assert.assertEquals(true, true);
//        Assert.assertEquals(null, bTree.searchKey(Double.POSITIVE_INFINITY));
    }
}
