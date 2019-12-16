package xyz.haohao.sso.core.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class HMACSHA1 {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String getSignature(String data, String secret) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result = toHex(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HmacSHA1: " + e.getMessage());
        }

        return result;
    }

    private static String toHex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }
}
