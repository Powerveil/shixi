package com.power;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class ttt {
    public static void main(String[] args) {
        String content = "yourPassword";

// 创建AES实例，使用默认密钥
        SymmetricCrypto aes = new SymmetricCrypto("AES");

// 加密
        String encryptStr = aes.encryptHex(content);
        System.out.println(encryptStr);

// 解密
        String decryptStr = aes.decryptStr(encryptStr);
        System.out.println(decryptStr);

    }
}
