package com.iamin.data.entity;

import com.iamin.data.Role;
import com.iamin.data.service.LoginRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LoginTest {

    @Autowired
    private LoginRepository loginRepository;

    @Test
    public void testPersistLogin() {
        // given
        SamplePerson person = new SamplePerson();
        person.setFirstName("John Doe");
        person.setEmail("johndoe@example.com");

        Login login = new Login();
        login.setUsername("johndoe");
        login.setHashedPassword("password123");
        login.setPerson(person);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        login.setRoles(roles);

        // when
        Login savedLogin = loginRepository.save(login);

        // then
        // assertThat(savedLogin.getId()).isNotNull();
 
        assertThat(savedLogin.getUsername()).isEqualTo("johndoe");
        assertThat(savedLogin.getHashedPassword()).isEqualTo("password123");
        assertThat(savedLogin.getPerson().getFirstName()).isEqualTo("John Doe");
        assertThat(savedLogin.getPerson().getEmail()).isEqualTo("johndoe@example.com");
        assertThat(savedLogin.getRoles()).contains(Role.USER);
    }
}
