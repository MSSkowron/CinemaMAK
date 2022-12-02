package pl.edu.agh.cs.to.cinemamak;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleGenerator {
    private final RoleRepository roleRepository;

    public RoleGenerator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void fillDatabase() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("Admin"));
            roleRepository.save(new Role("Manager"));
            roleRepository.save(new Role("Employee"));
        }
    }
}
