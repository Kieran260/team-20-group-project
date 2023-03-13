package com.iamin.security;

import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final LoginRepository loginRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        this.authenticationContext = authenticationContext;
    }

    public Optional<Login> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> loginRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
