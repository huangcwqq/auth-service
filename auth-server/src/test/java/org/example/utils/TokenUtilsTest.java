package org.example.utils;

import org.example.data.DataStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class TokenUtilsTest {

    @BeforeEach
    void setUp() {
        DataStore.init();
    }

    /**
     * generate 10000 token and verify that each is unique
     */
    @Test
    void testGenerateToken() {
        Set<String> tokenSet = new HashSet<>();
        final int count = 10000;
        for (int i = 0; i < count; i++) {
            final String generateToken = TokenUtils.generateToken();
            tokenSet.add(generateToken);
        }
        Assertions.assertEquals(tokenSet.size(), count);
    }
}