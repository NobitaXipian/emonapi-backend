package com.xipian.emonapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 生成签名工具
 * @author <a href="https://github.com/nobitaxipian">XIPIAN</a>
 * @date 2023/10/5
 */
public class SignUtils {

    /**
     * 生成签名
     *
     * @param body
     * @param secretKey
     * @return
     */
    public static String getSign(String body, String secretKey){

        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }

}
