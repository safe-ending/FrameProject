package common.util;


import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.UUID;

public class LockerUtils {

    private static final String charset = "utf-8";

    /**
     * 32位随机数
     */
    public static String get32Random() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

    }

    /**
     * 4位随机数
     */
    public static String get4Random() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 1000));

    }



    /**
     * 解密
     *
     * @param data
     */
    public static String Base64Decode(String data) {
        try {
            if (null == data) {
                return null;
            }

            return new String(Base64.decode(data.getBytes(charset), Base64.DEFAULT), charset);
        } catch (UnsupportedEncodingException e) {
            Log.e(String.format("字符串：%s，解密异常", data), e.getMessage());
        }

        return null;
    }

    /**
     * 加密
     *
     * @param data
     */
    public static String Base64encode(String data) {
        try {
            if (null == data) {
                return null;
            }
            return new String(Base64.encode(data.getBytes(charset), Base64.DEFAULT), charset);
        } catch (UnsupportedEncodingException e) {
            Log.e(String.format("字符串：%s，加密异常", data), e.getMessage());
        }

        return null;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 解析
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 将指定byte数组以16进制的形式打印到控制台
     *
     * @param b
     */
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }

    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String MD5encode(String str) {
        String strDigest = "";
        try {
            // 此 MessageDigest 类为应用程序提供信息摘要算法的功能，必须用try,catch捕获
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] data;
            data = md5.digest(str.getBytes("utf-8"));// 转换为MD5码
            strDigest = bytesToHexString(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return strDigest;
    }


}