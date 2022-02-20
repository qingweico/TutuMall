package cn.qingweico.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author 周庆伟
 * @date 2020/10/21
 */
public class SignUtil {
    /**
     *
     */
    private final static String TOKEN = "qiming";

    /**
     * 验证签名
     *
     * @param signature 微信加密签名    token + timestamp + nonce
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return true or false
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] array = new String[]{TOKEN, timestamp, nonce};
        //将token, timestamp, nonce三个参数进行字典排序
        Arrays.sort(array);
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : array) {
            stringBuilder.append(s);
        }
        MessageDigest messageDigest;
        String tmp = null;
        try {
            //将三个参数拼接为一个字符串进行SHA1加密
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = messageDigest.digest(stringBuilder.toString().getBytes());
            tmp = byteToStr(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmp != null && tmp.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String byteToStr(byte[] bytes) {
        StringBuilder res = new StringBuilder();
        for (byte bs : bytes) {
            res.append(byteToHexStr(bs));
        }
        return res.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param b 字节
     * @return 十六进制字符串
     */
    public static String byteToHexStr(byte b) {
        char[] ch = {
                '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E',
                'F'
        };
        char[] tmp = new char[2];
        tmp[0] = ch[b >>> 4 & 0x0F];
        tmp[1] = ch[b & 0x0F];
        return new String(tmp);
    }
}
