package com.mlamp;

import java.util.ArrayList;
import java.util.List;

public class 括号生成深度优先遍历递减 {

    public static void main(String[] args) {

        括号生成深度优先遍历递减 括号生成22 = new 括号生成深度优先遍历递减();
        List<String> strings = 括号生成22.generateParenthesis(2);
        for (String item : strings) System.out.println(item);

    }

    private static int index = 0;

    public List<String> generateParenthesis(int n) {

        //说明至少3个左右括号可以提供自由组合
        List<String> results = new ArrayList<>();
        dfs("", n, n, results);


        return results;
    }

    /**
     * 深度优先遍历
     *
     * @param s
     * @param left
     * @param right
     * @param results
     */

    private void dfs(String s, int left, int right, List<String> results) {
        if (left == 0 && right == 0) {
            results.add(s);
            return;
        }

        if (left > 0) {
            dfs(s + "(", left - 1, right, results);
        }

        if (right > 0 && left < right) {
            dfs(s + ")", left, right - 1, results);
        }

    }
}
