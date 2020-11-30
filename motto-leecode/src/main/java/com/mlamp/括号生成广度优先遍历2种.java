package com.mlamp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class 括号生成广度优先遍历2种 {

    public static void main(String[] args) {

        括号生成广度优先遍历2种 括号生成22 = new 括号生成广度优先遍历2种();
        List<String> strings = 括号生成22.generateParenthesis(3);
        for (String item : strings) System.out.println(item);

        System.out.println("*********************");

        strings = 括号生成22.generateParenthesisV2(3);
        for (String item : strings) System.out.println(item);

    }


    /**
     * 广度优先遍历, 因为结果都在叶子结点上
     *
     * @param n
     * @return
     */
    public List<String> generateParenthesisV2(int n) {

        //说明至少3个左右括号可以提供自由组合
        List<String> results = new ArrayList<>();
        if (n == 0) return results;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node("", 0, 0));
        int count = n;
        n = 2 * n;
        while (n > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node curNode = queue.poll();
                if (curNode.left < count) {
                    queue.offer(new Node(curNode.res + "(", curNode.left + 1, curNode.right));
                }
                if (curNode.right < curNode.left) {
                    queue.offer(new Node(curNode.res + ")", curNode.left, curNode.right + 1));
                }
            }
            n--;
        }
        while (!queue.isEmpty()) {
            results.add(queue.poll().res);
        }
        return results;
    }


    /**
     * 广度优先遍历, 因为结果都在叶子结点上
     *
     * @param n
     * @return
     */
    public List<String> generateParenthesis(int n) {

        //说明至少3个左右括号可以提供自由组合
        List<String> results = new ArrayList<>();
        if (n == 0) return results;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node("", n, n));
        n = 2 * n;
        while (n > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node curNode = queue.poll();
                if (curNode.left > 0) {
                    queue.offer(new Node(curNode.res + "(", curNode.left - 1, curNode.right));
                }
                if (curNode.right > 0 && curNode.left < curNode.right) {
                    queue.offer(new Node(curNode.res + ")", curNode.left, curNode.right - 1));
                }
            }
            n--;
        }
        while (!queue.isEmpty()) {
            results.add(queue.poll().res);
        }
        return results;
    }

    private static final class Node {
        private String res;
        private int left;
        private int right;

        public Node(String res, int left, int right) {
            this.res = res;
            this.left = left;
            this.right = right;
        }


        @Override
        public String toString() {
            return "Node{" +
                    "res='" + res + '\'' +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }


}
