package pl.edu.agh.cs.to.cinemamak;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserGenerator {
    private final UserRepository userRepository;

    public UserGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void fillDatabase() {
        if (userRepository.count() == 0) {
            userRepository.save(new User("usernameTest", "passwordTest"));
        }
    }
}
