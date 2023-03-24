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
    public void testUsernameValidation() {
        // Test for a valid username
        assertEquals("", validation.usernameValidation("username"));
    
        // Test for an existing username
        Mockito.when(mockLoginService.checkIfUsernameExists("existing")).thenReturn(true);
        assertEquals("Username is taken", validation.usernameValidation("existing"));
    
        // Test for username shorter than 8 characters
        assertEquals("Username must be exactly 8 characters in length", validation.usernameValidation("user123"));
    
        // Test for username longer than 8 characters
        assertEquals("Username must be exactly 8 characters in length", validation.usernameValidation("user12345"));
    
        // Test for username with non-alphabetic characters
        assertEquals("Username must contain alphabetic characters only", validation.usernameValidation("user1234"));
    
        // Test for username with SQL injection keywords
        assertEquals("Username contains a forbidden keyword", validation.usernameValidation("DROPuser"));
    }

    @Test
    public void testPasswordValidation() {
        assertEquals("", validation.passwordValidation("password1", "password1"));
        assertEquals("Password must be between 8 and 20 characters long", validation.passwordValidation("pass", "pass"));
        assertEquals("Password must contain at least one letter and one number", validation.passwordValidation("password", "password"));
        assertEquals("Passwords do not match, please try again", validation.passwordValidation("password1", "password2"));
        assertEquals("Password must contain at least one letter and one number", validation.passwordValidation("password@", "password@"));
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
    public void testUsernameValidationWithExistingUsername() {
        Mockito.when(mockLoginService.checkIfUsernameExists("user1234")).thenReturn(true);
        assertEquals("Username is taken", validation.usernameValidation("user1234"));
    }
}