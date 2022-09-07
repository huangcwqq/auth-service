package org.example.utils;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

class PassWordUtilsTest {

    @Test
    void testGenerateSalt() {
        try {
            final String salt = PassWordUtils.generateSalt();
            Assertions.assertTrue(StrUtil.isNotBlank(salt));
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testEncodePassWord(){
        String password = "123456";
        String errorPassword = "654321";
        String salt;
        try {
            salt = PassWordUtils.generateSalt();
            final String encodePassword = PassWordUtils.encodePassword(password, salt);
            Assertions.assertEquals(encodePassword,PassWordUtils.encodePassword(password,salt));
            Assertions.assertNotEquals(encodePassword,PassWordUtils.encodePassword(errorPassword,salt));
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}