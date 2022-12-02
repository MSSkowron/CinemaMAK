package pl.edu.agh.cs.to.cinemamak;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user) {
        Optional<Role> role = roleRepository.findByRoleName("Employee");
        if(role.isPresent()){
            user.setRole(role.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }
    public Optional<User> getUserByUsername(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return Optional.empty();
        }

        return userRepository.findByEmailAddress(emailAddress);
    }
}
