package pl.edu.agh.cs.to.cinemamak.generators;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.models.Role;
import pl.edu.agh.cs.to.cinemamak.models.User;
import pl.edu.agh.cs.to.cinemamak.repositories.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class Generator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Generator(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void fillDatabase() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("Admin"));
            roleRepository.save(new Role("Manager"));
            roleRepository.save(new Role("Employee"));
        }

        if (userRepository.count() == 0){
            User user = new User("firstname","lastname", "emailaddress", "password");
            Optional<Role> role = roleRepository.findByRoleName("Admin");
            if (role.isPresent()){
                user.setRole(role.get());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
    }
}
