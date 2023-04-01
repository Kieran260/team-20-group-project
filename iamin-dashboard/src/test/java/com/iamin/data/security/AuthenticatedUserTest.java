package com.iamin.data.security;

import com.iamin.data.entity.Login;

import com.iamin.data.service.LoginRepository;
import com.iamin.security.AuthenticatedUser;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticatedUserTest {

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    @Mock
    private WebAuthenticationDetails webAuthenticationDetails;

    private AuthenticatedUser authenticatedUser;

    @Before
public void setUp() {
    authenticatedUser = new AuthenticatedUser(authenticationContext, loginRepository);
    SecurityContextHolder.setContext(securityContext);
}


    @Test
    public void testGetAuthenticatedUser() {
        // Mock the authentication context to return the user details
        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(Optional.of(userDetails));
        when(userDetails.getUsername()).thenReturn("testuser");

        // Mock the login repository to return a login object
        Login login = new Login();
        when(loginRepository.findByUsername("testuser")).thenReturn(login);

        // Call the method being tested
        Optional<Login> result = authenticatedUser.get();

        // Verify that the correct login object is returned
        assertEquals(login, result.get());
    }

    @Test
    public void testLogout() {
        // Call the method being tested
        authenticatedUser.logout();

        // Verify that the authentication context's logout method was called
        Mockito.verify(authenticationContext, Mockito.times(1)).logout();
    }
}
