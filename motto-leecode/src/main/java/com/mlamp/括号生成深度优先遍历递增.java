package com.mlamp;

import java.util.ArrayList;
import java.util.List;

public class 括号生成深度优先遍历递增 {

    public static void main(String[] args) {

        括号生成深度优先遍历递增 括号生成22 = new 括号生成深度优先遍历递增();
        List<String> strings = 括号生成22.generateParenthesis(3);
        for (String item : strings) System.out.println(item);

    }

    private static int index = 0;

    public List<String> generateParenthesis(int n) {

        //说明至少3个左右括号可以提供自由组合
        List<String> results = new ArrayList<>();
        dfs("", n, 0, 0, results);


        return results;
    }

    /**
     * 深度优先遍历
     *
     * @param s
     * @param count
     * @param left
     * @param right
     * @param results
     */
    private void dfs(String s, int count, int left, int right, List<String> results) {
        if (left == count && right == count) {
            results.add(s);
            return;
        }

        if (left < count) {
            dfs(s + "(", count, left + 1, right, results);
        }

        if (right < left) {
            dfs(s + ")", count, left, right + 1, results);
        }

    }
}
