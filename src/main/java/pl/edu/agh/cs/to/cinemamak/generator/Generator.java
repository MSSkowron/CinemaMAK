package pl.edu.agh.cs.to.cinemamak.generator;

import org.springframework.data.repository.cdi.Eager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.Genre;
import pl.edu.agh.cs.to.cinemamak.model.Role;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.repository.GenreRepository;
import pl.edu.agh.cs.to.cinemamak.repository.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repository.UserRepository;

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
            User admin = generateAdmin();
            if(admin != null) {
                userRepository.save(admin);
            }

            User employee = generateEmployee();
            if(employee != null){
                userRepository.save(employee);
            }

        }
    }

    public List<Role> generateRoles() {

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleName.Admin));
        roles.add(new Role(RoleName.Manager));
        roles.add(new Role(RoleName.Employee));

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
        User user = new User("admin","admin", "admin@gmail.com", "admin");
        Optional<Role> role = roleRepository.findByRoleName(RoleName.Admin.toString());
        if (role.isPresent()){
            user.setRole(role.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return user;
        }

        return null;
    }

    public User generateEmployee() {
        User user = new User("employee","employee", "employee@gmail.com", "employee");
        Optional<Role> role = roleRepository.findByRoleName(RoleName.Employee.toString());
        if (role.isPresent()){
            user.setRole(role.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return user;
        }
        return null;
    }


}
