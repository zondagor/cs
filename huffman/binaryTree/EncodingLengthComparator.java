package com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree;

import java.util.Comparator;

class EncodingLengthComparator implements Comparator<TreeLeaf> {

    @Override
    public int compare(TreeLeaf treeNode1, TreeLeaf treeNode2) {
        if (treeNode1.getEncodingLength() > treeNode2.getEncodingLength())
            return 1;
        if (treeNode1.getEncodingLength() < treeNode2.getEncodingLength())
            return -1;
        return 0;
    }

}
