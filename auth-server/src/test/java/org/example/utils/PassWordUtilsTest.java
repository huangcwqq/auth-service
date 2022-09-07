package org.example.utils;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PassWordUtilsTest {

    @Test
    void testGenerateSalt() {
        final String salt = PassWordUtils.generateSalt();
        Assertions.assertTrue(StrUtil.isNotBlank(salt));
    }

    @Test
    void testEncodePassWord() {
        String password = "123456";
        String errorPassword = "654321";
        String salt;
        salt = PassWordUtils.generateSalt();
        final String encodePassword = PassWordUtils.encodePassword(password, salt);
        Assertions.assertEquals(encodePassword, PassWordUtils.encodePassword(password, salt));
        Assertions.assertNotEquals(encodePassword, PassWordUtils.encodePassword(errorPassword, salt));

    }
}