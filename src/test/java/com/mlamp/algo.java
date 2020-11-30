package com.mlamp;

public class algo {


    public static void main(String[] args) {
        String result = DPlengthOfLongestCommonSubstring2("hello", "hell2o wor");
        String result2 = DPlengthOfLongestCommonSubstring("hello","hello wor");
        System.out.println(result2);
    }

    public  static String DPlengthOfLongestCommonSubstring(String s1, String s2){
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0){
            return "";
        }
        int start = 0;
        int maxLen = 0;
        int [][] table = new int[s1.length()][s2.length()];
        for (int i = 0; i < s1.length(); i++) {
            for (int j = 0; j < s2.length(); j++) {
                if (i == 0 || j == 0){
                    if (s1.charAt(i) == s2.charAt(j)){
                        table[i][j] = 1;
                    }
                    if (table[i][j] > maxLen){
                        maxLen = table[i][j];
                        start = i;
                    }
                }else {
                    if (s1.charAt(i) == s2.charAt(j)){
                        table[i][j] = table[i-1][j-1] + 1;
                    }
                    if (table[i][j] > maxLen){
                        maxLen = table[i][j];
                        start = i + 1 - maxLen;
                    }
                }
            }
        }
        return s1.substring(start, start + maxLen);
    }



    /**
     * 只用O(N)额外空间
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String DPlengthOfLongestCommonSubstring2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return "";
        }
        int start = 0;
        int maxLen = 0;
        int[] table = new int[s2.length()];
        for (int i = 0; i < s1.length(); i++) {
            for (int j = s2.length() - 1; j > -1; j--) {
                if (i == 0 || j == 0) {
                    if (s1.charAt(i) == s2.charAt(j)) {
                        table[j] = 1;
                    } else {
                        table[j] = 0;
                    }
                    if (table[j] > maxLen) {
                        maxLen = table[j];
                        start = i;
                    }
                } else {
                    if (s1.charAt(i) == s2.charAt(j)) {
                        table[j] = table[j - 1] + 1;
                    } else {
                        table[j] = 0;
                    }
                    if (table[j] > maxLen) {
                        maxLen = table[j];
                        start = i + 1 - maxLen;
                    }
                }
            }
        }
        return s1.substring(start, start + maxLen);
    }


}


