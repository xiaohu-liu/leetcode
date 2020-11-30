package com.mlamp;

import java.util.HashMap;

public class LRUCache {

    private DLinkNode head, tail;
    private int capacity;
    private int count;
    private HashMap<Integer, DLinkNode> cache = new HashMap<>();

    private static class DLinkNode {
        int key;
        int value;
        DLinkNode pre;
        DLinkNode next;
    }

    private void addNode(DLinkNode dLinkNode) {
        dLinkNode.pre = head;
        dLinkNode.next = head.next;
        head.next.pre = dLinkNode;
        head.next = dLinkNode;
    }

    private void removeNode(DLinkNode node) {
        DLinkNode pre = node.pre;
        DLinkNode next = node.next;
        pre.next = next;
        next.pre = pre;
    }

    private void moveToHead(DLinkNode node) {
        this.removeNode(node);
        this.addNode(node);
    }

    private DLinkNode popTail() {
        DLinkNode res = tail.pre;
        this.removeNode(res);
        return res;
    }
}
