package pl.edu.agh.cs.to.cinemamak;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class Generator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Generator(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void fillDatabase() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("Admin"));
            roleRepository.save(new Role("Manager"));
            roleRepository.save(new Role("Employee"));
        }

        if (userRepository.count() == 0){
            User user = new User("email", "password");
            Optional<Role> role = roleRepository.findByRoleName("Admin");
            if (role.isPresent()){
                user.setRole(role.get());
                userRepository.save(user);
            }
        }
    }
}
