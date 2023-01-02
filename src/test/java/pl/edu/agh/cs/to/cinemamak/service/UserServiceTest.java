package pl.edu.agh.cs.to.cinemamak.service;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.cs.to.cinemamak.model.Role;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.repository.UserRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getUsersTest() throws Exception {

        User u = new User("user1", "last1", "user1@gmail.com", "pass1");
        User u2 = new User("user2", "last2", "user2@gmail.com", "pass2");

        userService.addUser(u);
        userService.addUser(u2);

        Long len = userService.getUsers().get().stream().count();

        assertFalse(userService.getUsers().get().isEmpty());
        assertEquals(Long.valueOf(4), len);

        userService.deleteUser(u);
        userService.deleteUser(u2);
    }

    @Test
    public void updateUserTest() throws Exception {

        User u = new User("user1", "last1", "user1@gmail.com", "pass1");
        userService.addUser(u);

        User u2 = new User("user1", "last1", "user1@gmail.com", "pass1");
        long userUId = userService.getUserByEmail(u.getEmailAddress()).get().getId();
        u2.setId(userUId);
        u2.setRole(new Role(RoleName.Manager));

        userService.updateUser(u2);

        Optional<User> u3 = userService.getUserByEmail("user1@gmail.com");
        u3.ifPresent(user -> assertEquals(user, u2));

        u3.ifPresent(user -> userService.deleteUser(user));
    }

    @Test
    public void testRole() throws Exception {
        Optional<User> u = userService.getUserByEmail("admin@gmail.com");
        u.ifPresent(user -> assertEquals(user.getRole().getRoleName(), RoleName.Admin));
    }

    @Test
    public void testAuthenticate() throws Exception{
        User u = new User("user1", "last1", "user1@gmail.com", "pass1");
        userService.addUser(u);

        assertTrue(userService.authenticate("user1@gmail.com", "pass1"));
        assertTrue(userService.authenticate("admin@gmail.com", "admin"));

        userService.deleteUser(u);
    }

}
