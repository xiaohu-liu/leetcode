package com.mlamp;

import java.util.HashMap;
import java.util.Map;

public class 最长连续序列 {

    /**
     * 输入: [100, 4, 200, 1, 3, 2]
     * 输出: 4
     *
     * @param args
     */
    public static void main(String[] args) {
        最长连续序列 instance = new 最长连续序列();
        int i = instance.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2});
        System.out.println(i);

    }


    /**
     * 并查集
     *
     * @param nums
     * @return
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null) return 0;
        if (nums.length <= 1) return nums.length;
        Map<Integer, Integer> dict = new HashMap<>();
        for (int number : nums) {
            if (!dict.containsKey(number)) {
                dict.put(number, number);
                if (dict.containsKey(number - 1)) dict.put(number - 1, number);
                if (dict.containsKey(number + 1)) dict.put(number, number + 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : dict.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }

        int result = 1;
        for (int num : nums) {
            int len = parent(dict, num) - num + 1;
            System.out.println(String.format("[%s, %s]", num, len));
            result = result > len ? result : len;

        }
        return result;
    }

    private int parent(Map<Integer, Integer> map, int num) {
        if (map.get(num) == num) return num;
        return parent(map, map.get(num));
    }


}
