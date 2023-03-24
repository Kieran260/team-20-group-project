package com.iamin.data.validation;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.iamin.data.service.LoginService;
import org.mockito.Mockito;

public class ValidationTest {

    private Validation validation;
    private LoginService mockLoginService;

    @Before
    public void setUp() {
        mockLoginService = Mockito.mock(LoginService.class);
        Mockito.when(mockLoginService.checkIfUsernameExists(Mockito.anyString())).thenReturn(false);
        validation = new Validation(mockLoginService);
    }

    @Test
    public void testusernameValidation() {
        assertTrue(validation.usernameValidation("user1234"));
        assertFalse(validation.usernameValidation("user123"));
        assertFalse(validation.usernameValidation("user12345"));
        assertFalse(validation.usernameValidation("user 1234"));
        assertFalse(validation.usernameValidation("user_1234"));
    }

    @Test
    public void testPasswordValidation() {
        assertTrue(validation.passwordValidation("password1", "password1"));
        assertFalse(validation.passwordValidation("pass", "pass"));
        assertFalse(validation.passwordValidation("password", "password"));
        assertFalse(validation.passwordValidation("password1", "password2"));
        assertFalse(validation.passwordValidation("password@", "password@"));
    }

    @Test
    public void testIsAlphaNumeric() {
        assertTrue(validation.isAlphaNumeric('a'));
        assertTrue(validation.isAlphaNumeric('Z'));
        assertTrue(validation.isAlphaNumeric('5'));
        assertFalse(validation.isAlphaNumeric('@'));
        assertFalse(validation.isAlphaNumeric(' '));
    }

    @Test
    public void testIsSqlInjection() {
        assertFalse(validation.isSqlInjection("This is a normal text."));
        assertTrue(validation.isSqlInjection("SELECT * FROM users;"));
        assertTrue(validation.isSqlInjection("insert into users values(1, 'John');"));
        assertTrue(validation.isSqlInjection("Update users set password='newpass' where id=1;"));
    }

    @Test
    public void testSanitizeInput() {
        String input1 = "Hello, <b>World!</b>";
        String expected1 = "Hello, World!";
        assertEquals(expected1, validation.sanitizeInput(input1));

        String input2 = "Hello, <script>alert('XSS');</script>World!";
        String expected2 = "Hello, World!";
        assertEquals(expected2, validation.sanitizeInput(input2));

        String input3 = "<p>Hello, <a href=\"https://example.com\">Example</a></p>";
        String expected3 = "Hello, Example";
        assertEquals(expected3, validation.sanitizeInput(input3));
    }

    @Test
    public void testusernameValidationWithExistingUsername() {
        Mockito.when(mockLoginService.checkIfUsernameExists("user1234")).thenReturn(true);
        assertFalse(validation.usernameValidation("user1234"));
    }
}