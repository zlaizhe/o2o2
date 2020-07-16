package com.imooc.o2o.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

//Jasypt对数据库用户名密码进行加密
public class JasyptTest {
    @Test
    public void test() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        /*配置文件中配置如下的算法*/
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        /*配置文件中配置的密钥password*/
        encryptor.setPassword("MY");
        /*要加密的文本*/
        String name = encryptor.encrypt("root");
        String password = encryptor.encrypt("root");
        String password2 = encryptor.encrypt("zhu123456");
        /*将加密的文本写到配置文件中*/
        System.out.println("name=" + name);
        System.out.println("password=" + password);
        System.out.println("passowrd2=" + password2);
    }
}
