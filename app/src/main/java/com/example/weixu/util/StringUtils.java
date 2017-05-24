package com.example.weixu.util;

/**
 * Created by weixu on 2017/4/16.
 */

public class StringUtils {
    /**
     * 判断字符是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 破解虾米加密算法
     *
     * @param 加密后的地址
     * @return 解密后的地址
     */
    public static String decodeMusicUrl(String input) {
        int loc_10 = -1;
        int loc_2 = Integer.parseInt(String.valueOf(input.charAt(0)));
        String loc_3 = input.substring(1);
        int loc_4 = loc_3.length() / loc_2;
        int loc_5 = loc_3.length() % loc_2;
        String[] loc_6 = new String[512];
        int loc_7 = 0;
        String loc_8 = "";
        String loc_9 = "";
        while (loc_7 < loc_5) {
            int begin = (loc_4 + 1) * loc_7;
            int length = (loc_4 + 1);
            loc_6[loc_7] = new String(loc_3.substring(begin, begin + length));
            loc_7++;
        }
        loc_7 = loc_5;
        while (loc_7 < loc_2) {
            int begin = loc_4 * (loc_7 - loc_5) + (loc_4 + 1) * loc_5;
            int length = loc_4;
            loc_6[loc_7] = new String(loc_3.substring(begin, begin + length));
            loc_7++;
        }
        loc_7 = 0;
        while (loc_7 < loc_6[0].length()) {
            loc_10 = 0;
            while (loc_6[loc_10] != null) {
                if (loc_7 < loc_6[loc_10].length()) {
                    loc_8 = loc_8 + loc_6[loc_10].substring(loc_7, loc_7 + 1);
                }
                loc_10++;
            }
            loc_7++;
        }
        loc_8 = unescape(loc_8);
        loc_7 = 0;
        while (loc_7 < loc_8.length()) {
            if (loc_8.charAt(loc_7) == '^') {
                loc_9 = loc_9 + "0";
            } else {
                loc_9 = loc_9 + loc_8.substring(loc_7, loc_7 + 1);
            }
            loc_7++;
        }
        loc_9 = loc_9.replace("+", " ");
        return loc_9;
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);

        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");
                if (j < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;

        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}

