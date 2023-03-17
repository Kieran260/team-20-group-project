package com.iamin.data.validation;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void testUserNameValidation() {
        assertTrue(Validation.userNameValidation("user1234"));
        assertFalse(Validation.userNameValidation("user123"));
        assertFalse(Validation.userNameValidation("user12345"));
        assertFalse(Validation.userNameValidation("user 1234"));
        assertFalse(Validation.userNameValidation("user_1234"));
    }

    @Test
    public void testPasswordValidation() {
        assertTrue(Validation.passwordValidation("password1", "password1"));
        assertFalse(Validation.passwordValidation("pass", "pass"));
        assertFalse(Validation.passwordValidation("password", "password"));
        assertFalse(Validation.passwordValidation("password1", "password2"));
        assertFalse(Validation.passwordValidation("password@", "password@"));
    }
}
