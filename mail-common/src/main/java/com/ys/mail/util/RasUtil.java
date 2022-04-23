package com.ys.mail.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.ys.mail.constant.FigureConstant;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ghdhj
 */
public class RasUtil {

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDc0aT3HZi8W2O36tG2cQotb+p4KymgfFMSWF7fTyGMGFqiNKK6rrdhj9NVwg1sOJ8lSdFxMnB5j7ov7JEpoWHfajWzRF+qVBozDZuBFYqEriFQDvFQgZycgkACmpIgSX8Kgo2HJNfRKt2HodppMFFAPMY3uLhKXPj+finSWOYmKQIDAQAB";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANzRpPcdmLxbY7fq0bZxCi1v6ngrKaB8UxJYXt9PIYwYWqI0orqut2GP01XCDWw4nyVJ0XEycHmPui/skSmhYd9qNbNEX6pUGjMNm4EVioSuIVAO8VCBnJyCQAKakiBJfwqCjYck19Eq3Yeh2mkwUUA8xje4uEpc+P5+KdJY5iYpAgMBAAECgYEAxuQZVu8UhAs4vQ5HB7G3PgfAw63013VDiZGJGk/24m2JLPKF4+m8iKZ37pVTa7dwOLDNcczBm+xGAiZs9XdDY8Cz4qtX7OFXhe4RA9bE0OXEtNPYEwmBLw2JszqvpQh3y6mCCg5PxB9srMmKxjiZxJEkM0h2Vc3wt2/woe/T8xUCQQDxwU8BdpDMp2z/ad6AzHgY/w9OmYtSERFyWBtqh3B/rDTuY7ia5+29ddXb+5/4z6qDEcqemZDzvpTCA4wDOv0TAkEA6dSHg7+MIRV5SmX2xNgJeY63Cnkbr6DyjtaBMAVGhzXUvqlSHyfn1k0ly7fePYEM+wTO0S0DVfhlVaS1HzGjUwJAVhrRtuWx4RJowffFJf6tb8m4e/g4JPyrSByVdzXSHccJ+zZNU+7zQB2uE/HlIktkodY+Yoqzk/z3Irr8rVHfPQJBAL3vJox74OVWvgkSvdmy6i9VH5VgPn7wq1YfIxzQjDN0JHUisFQiZA0M6XCKz/kBhHu5sgzzFUyV0io4+e52sYUCQQDHm5hgOFkxE/DYkFGpa7VwljR2V67I7l8h2DRmkUM3CMZ+iSMJy5elmweCtnnBkgjLtTPQ9REgIcQWMc9U/ZfC";

    /**
     * 编码类型
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 签名
     */
    private static final String SIGN_TYPE = "RSA";

    /**
     * 秘钥大小
     */
    private static final Integer KEY_SIZE = 1024;

    public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        keyPairGen = KeyPairGenerator.getInstance(SIGN_TYPE);
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        Map<Integer, String> keyMap = new HashMap<>(FigureConstant.MAP_INIT_SIX_EEN);
        // 将公钥和私钥保存到Map 0表示公钥 1表示私钥
        keyMap.put(NumberUtils.INTEGER_ZERO, publicKeyString);
        keyMap.put(NumberUtils.INTEGER_ONE, privateKeyString);
        return keyMap;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        String outStr = null;
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(SIGN_TYPE).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(SIGN_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(CHARSET)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        if (privateKey == null) {
            privateKey = PRIVATE_KEY;
        }
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(CHARSET));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(SIGN_TYPE).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(SIGN_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static void main(String[] args) throws Exception {

        String encrypt1 = null;
        try{
            // 签名如何判断只使用了一次,用json
            // TODO 一重判断,加密和解密,这里直接
            // 加密
            JSONObject result = new JSONObject();
            result.put("userId",1222);
            result.put("name","你好");
            result.put("name",19);
            encrypt1 = RasUtil.encrypt(result.toString(), PUBLIC_KEY);
            System.out.println(encrypt1);
            String decrypt = RasUtil.decrypt(encrypt1, PRIVATE_KEY);
            JSONObject jsonObject = JSONObject.parseObject(decrypt);
            System.out.println(jsonObject.get("userId"));
            System.out.println(jsonObject.get("name"));
            System.out.println(jsonObject.get("name"));

            System.out.println(decrypt);
        }catch (BadPaddingException e){
            e.printStackTrace();
        }finally {
            System.out.println("异常继续走");
        }
        if(BlankUtil.isNotEmpty(encrypt1)){
            System.out.println("来了");
        }
        System.out.println("这里有返回值返回");
    }
}
