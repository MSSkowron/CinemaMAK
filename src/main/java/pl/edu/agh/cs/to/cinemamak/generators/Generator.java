package pl.edu.agh.cs.to.cinemamak.generators;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.models.Genre;
import pl.edu.agh.cs.to.cinemamak.models.Role;
import pl.edu.agh.cs.to.cinemamak.models.User;
import pl.edu.agh.cs.to.cinemamak.repositories.GenreRepository;
import pl.edu.agh.cs.to.cinemamak.repositories.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Generator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PasswordEncoder passwordEncoder;


    public Generator(RoleRepository roleRepository, UserRepository userRepository,GenreRepository genreRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void fillDatabase() {
        if (roleRepository.count() == 0) {
            List<Role> roles = generateRoles();
            roleRepository.saveAll(roles);
        }

        if(genreRepository.count() == 0) {
            List<Genre> genres = generateGenres();
            genreRepository.saveAll(genres);
        }

        if (userRepository.count() == 0){
            User user = generateAdmin();
            if(user != null) {
                userRepository.save(user);
            }
        }
    }

    public List<Role> generateRoles() {
        List<Role> roles = new ArrayList<>();

        try (var scanner = new Scanner(Objects.requireNonNull(Generator.class.getResourceAsStream("roles.txt")))) {
            while (scanner.hasNext()) {
                var name = scanner.nextLine();
                var role = new Role(name);
                roles.add(role);
            }
        }
        return roles;
    }

    public List<Genre> generateGenres() {
        List<Genre> genres = new ArrayList<>();

        try (var scanner = new Scanner(Objects.requireNonNull(Generator.class.getResourceAsStream("genres.txt")))) {
            while (scanner.hasNext()) {
                var name = scanner.nextLine();
                var genre = new Genre(name);
                genres.add(genre);
            }
        }
        return genres;
    }

    public User generateAdmin() {
        User user = new User("admin","admin", "admin@gmail.com", "admin-123@");
        Optional<Role> role = roleRepository.findByRoleName("Admin");
        if (role.isPresent()){
            user.setRole(role.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return user;
        }

        return null;
    }


}
