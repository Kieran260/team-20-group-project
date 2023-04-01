package com.iamin.security;

import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    @Mock
    private LoginRepository loginRepository;

    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsServiceImpl(loginRepository);
    }

    @Test
    public void testLoadUserByUsername() {
        // Arrange
        String username = "testuser";
        String hashedPassword = "testpassword";
        List<String> roles = Arrays.asList("ADMIN", "USER");
        Login login = new Login();
        login.setUsername(username);
        login.setHashedPassword(hashedPassword);
        login.setRoles(roles);
        when(loginRepository.findByUsername(username)).thenReturn(login);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(hashedPassword, userDetails.getPassword());
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        assertTrue(authorities.containsAll(userDetails.getAuthorities()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameThrowsException() {
        // Arrange
        String username = "nonexistentuser";
        when(loginRepository.findByUsername(username)).thenReturn(null);

        // Act
        userDetailsService.loadUserByUsername(username);

        // Assert
        // Expected exception UsernameNotFoundException to be thrown
    }
}
