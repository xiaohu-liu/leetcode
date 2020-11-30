package com.mlamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 括号生成22 {

    public static void main(String[] args) {

        括号生成22 括号生成22 = new 括号生成22();
        List<String> strings = 括号生成22.generateParenthesis(2);
        for (String item : strings) System.out.println(item);

    }

    private static int index = 0;

    public List<String> generateParenthesis(int n) {

        if (n < 1) return Arrays.asList("");
        if (n < 2) return Arrays.asList("()");

        //说明至少3个左右括号可以提供自由组合
        List<String> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dfs(n, 0, 0, results, sb);


        return results;
    }

    private void dfs(int n, int leftCnt, int rightCnt, List<String> results, StringBuilder sb) {
        //终止条件
        if (leftCnt == n && rightCnt == n) {
            results.add(sb.toString());
            return;
        }

        //需要添加左括号
        if (leftCnt < n) {
            sb.append("(");
            //System.out.println(String.format("[%s] dfs1 ->   %s, %s, %s, %s, %s ", index++, n, leftCnt + 1, rightCnt, Arrays.toString(results.toArray()), sb));
            dfs(n, leftCnt + 1, rightCnt, results, sb);
            sb.setLength(sb.length() - 1);
            System.out.println(index ++ +  " -> " + sb.toString());
        }

        //需要添加右括号
        if (rightCnt < leftCnt) {
            sb.append(")");
            //System.out.println(String.format("[%s] dfs2 ->   %s, %s, %s, %s, %s ", index++, n, leftCnt, rightCnt + 1, Arrays.toString(results.toArray()), sb));
            dfs(n, leftCnt, rightCnt + 1, results, sb);
            sb.setLength(sb.length() - 1);
            System.out.println(index ++ +  " -> " + sb.toString());
        }
    }
}
