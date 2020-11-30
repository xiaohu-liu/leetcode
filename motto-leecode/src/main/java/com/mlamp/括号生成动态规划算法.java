package com.mlamp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class 括号生成动态规划算法 {

    public static void main(String[] args) {

        括号生成动态规划算法 括号生成22 = new 括号生成动态规划算法();
        List<String> strings = 括号生成22.generateParenthesis(3);
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


        //第一步，定义状态dp[i]
        //第二步，状态转移方程
        // i 对括号的一个组合，在i-1对括号的基础上得到
        // i 对括号的一个组合，一定以左括号开始
        // dp[i] = "(" + dp[可能的括号对数] + ")" + dp[剩余的括号对数]
        //可能的括号对数与剩余的括号对数之和等于i, 所以可能的括号对数可以从0开始，最多不能超过i, 也就是i-1
        //剩余的括号对数 + j = i - 1, 所以剩余的括号对数= i -j -1
        // dp[i]　＝　"(" + db[j] + ")" + dp[i-j-1]
        if (n == 0) return new ArrayList<>();

        List<List<String>> dp = new ArrayList<>(n);

        List<String> dp0 = new ArrayList<>();
        dp0.add("");
        dp.add(dp0);


        for (int i = 1; i <= n; i++) {
            List<String> cur = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                List<String> str1 = dp.get(j);
                List<String> str2 = dp.get(i - j - 1);
                System.out.println("str1: " + str1);
                System.out.println("str2: " + str2);
                for (String s1 : str1) {
                    for (String s2 : str2) {
                        cur.add("(" + s1 + ")" + s2);
                    }
                }
            }
            dp.add(cur);
        }
        return dp.get(n);
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
