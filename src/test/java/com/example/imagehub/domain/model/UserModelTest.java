package com.example.imagehub.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserModelTest {

    @Test
    void createUserTest() {
        UserModel userModel = new UserModel("test1", "testUser", "password123", "USER");
        assertEquals("testUser", userModel.getUsername());
        assertEquals("password123", userModel.getPassword());
        assertEquals("USER", userModel.getRole());
    }
}
