package pl.edu.agh.cs.to.cinemamak;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }
    public User getUserByUsername(String emailAddress) throws Exception {
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new Exception("user is empty");
        }

        Optional<User> foundUser = userRepository.findByEmailAddress(emailAddress);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }

        throw new Exception(emailAddress + "is not found");
    }
}
