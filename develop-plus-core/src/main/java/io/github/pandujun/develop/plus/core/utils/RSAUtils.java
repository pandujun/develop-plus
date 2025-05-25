package io.github.pandujun.develop.plus.core.utils;

import io.github.pandujun.develop.plus.core.result.ResultEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密解密
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/11/10 10:48
 */
public class RSAUtils {
    private static final Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    public static final String KEY_ALGORITHM = "RSA";

    // 使用公钥加密数据
    public static String encrypt(String text, String publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("RSAUtils#encrypt ERROR: ", e);
            throw ResultEnums.SECURE_ERROR.getException();
        }
    }

    // 使用私钥解密数据
    public static String decrypt(String text, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(text));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("RSAUtils#decrypt ERROR: ", e);
            throw ResultEnums.SECURE_ERROR.getException();
        }
    }

    // 将公钥字符串转换为 PublicKey 对象
    private static PublicKey getPublicKey(String publicKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    // 将私钥字符串转换为 PrivateKey 对象
    private static PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

}
