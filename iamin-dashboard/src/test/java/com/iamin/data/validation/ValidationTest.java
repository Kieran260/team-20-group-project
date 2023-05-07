package com.iamin.data.validation;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;

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
        assertEquals("Username must be exactly 8 characters in length", validation.usernameValidation("usernam"));
    
        // Test for username longer than 8 characters
        assertEquals("Username must be exactly 8 characters in length", validation.usernameValidation("usernamea"));
    
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
    public void testIsAlpha() {
        assertTrue(Validation.isAlpha("alphabet"));
        assertFalse(Validation.isAlpha("alpha123"));
        assertFalse(Validation.isAlpha("123456"));
        assertFalse(Validation.isAlpha(" "));
        assertFalse(Validation.isAlpha(""));
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

    @Test
    public void testAddressValidation() {
        assertFalse(Validation.addressValidation("123 Main St", "Apt 4B", "New York", "NY10001"));
        assertFalse(Validation.addressValidation("123 Main St", "Apt 4B", "New York", " "));
        assertFalse(Validation.addressValidation("123 Main St", "Apt 4B", "New York9", "NY 10001"));
        assertFalse(Validation.addressValidation("123 Main St", "Apt 4B", " ", "NY 10001"));
    }

    @Test
    public void testPhoneValidation() {
        assertTrue(Validation.phoneValidation("(123) 456-7890"));
        assertTrue(Validation.phoneValidation("(123)-456-7890"));
        assertTrue(Validation.phoneValidation("123-456-7890"));
        assertTrue(Validation.phoneValidation("123.456.7890"));
        assertTrue(Validation.phoneValidation("1234567890"));
        assertTrue(Validation.phoneValidation("07123456789"));
    }

    @Test
    public void testIsSqlInjection() {
        assertTrue(Validation.isSqlInjection("SELECT * FROM users WHERE username = 'john'"));
        assertTrue(Validation.isSqlInjection("DROP TABLE users;"));
        assertTrue(Validation.isSqlInjection("1; DROP TABLE users;"));
        assertFalse(Validation.isSqlInjection("john"));
        assertFalse(Validation.isSqlInjection("john.doe"));
        
    }

    @Test
    public void testIsAfterCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate pastDate = currentDate.minusDays(1);
        LocalDate futureDate = currentDate.plusDays(1);

        assertFalse(Validation.isAfterCurrentDate(currentDate));
        assertFalse(Validation.isAfterCurrentDate(pastDate));
        assertTrue(Validation.isAfterCurrentDate(futureDate));
    }
}