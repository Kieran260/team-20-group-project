package com.iamin.security;

import com.iamin.data.Role;
import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.apache.tomcat.jni.User.username;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsTest {

    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private LoginRepository loginRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsService = new UserDetailsServiceImpl(loginRepository);
    }

    @Test
public void testLoadUserByUsername() {
    Login login = new Login();
    login.setUsername("myUsername");
    login.setHashedPassword("myPassword");
    Set<Role> roles = new HashSet<>();
    roles.add(Role.ADMIN);
    login.setRoles(roles);

    when(loginRepository.findByUsername("myUsername")).thenReturn(login);

    UserDetails userDetails = userDetailsService.loadUserByUsername("myUsername");

    assertEquals("myUsername", userDetails.getUsername());
    assertEquals("myPassword", userDetails.getPassword());
    assertEquals(1, userDetails.getAuthorities().size());
    assertEquals("ROLE_ADMIN", userDetails.getAuthorities().iterator().next().getAuthority());
}

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameWithInvalidUsername() {
        String username = "testuser";
        when(loginRepository.findByUsername(username)).thenReturn(null);
        userDetailsService.loadUserByUsername(username);
    }

}
