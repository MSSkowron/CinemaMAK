package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Role;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.repository.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repository.UserRepository;

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

    public boolean authenticate(String username, String password) {
        var user = getUserByUsername(username);
        return user.isPresent() && !user.get().getPassword().equals(passwordEncoder.encode(password));
    }

    public void addUser(User user) throws Exception{
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
