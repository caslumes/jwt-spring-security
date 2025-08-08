package com.caslumes.securityservice.repository;

import com.caslumes.securityservice.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepository_Save_ReturnsSavedUser(){
        User user = new User(null, "Lucas", "caslumes", "1234", new ArrayList<>());

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test void userRepository_FindAll_ReturnsMoreThanOneUser(){
        User user1 = new User(null,"Maria Silva","silvineia","123",new ArrayList<>());
        User user2 = new User(null,"Lucas Marques","caslumes","123",new ArrayList<>());

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> listOfUsers = userRepository.findAll();

        Assertions.assertThat(listOfUsers).isNotNull();
        Assertions.assertThat(listOfUsers).size().isEqualTo(2);
    }
}
