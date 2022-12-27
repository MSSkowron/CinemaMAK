package pl.edu.agh.cs.to.cinemamak.generator;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.*;
import pl.edu.agh.cs.to.cinemamak.repository.*;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Generator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    public Generator(RoleRepository roleRepository, UserRepository userRepository, GenreRepository genreRepository, PasswordEncoder passwordEncoder, RoomRepository roomRepository, SeatRepository seatRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.passwordEncoder = passwordEncoder;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
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

        if (roomRepository.count() == 0) {
            generateRooms();
        }
    }

    private void generateRooms() {
        var parser = JsonParserFactory.getJsonParser();

        try (var jsonStream = new Scanner(Objects.requireNonNull(Generator.class.getResourceAsStream("rooms.json"))) ) {
            StringBuilder json = new StringBuilder();

            while (jsonStream.hasNext()) {
                json.append(jsonStream.nextLine()).append("\n");
            }

            var list = parser.parseList(json.toString());

            var rooms = list.stream().map(o -> {
                if (o instanceof Map oAsMap) {
                    var name = (String) oAsMap.get("name");
                    var rows = (Integer) oAsMap.get("seatRows");
                    var cols = (Integer) oAsMap.get("seatsPerRow");

                    var r = new Room(name);
                    var seats = new HashSet<Seat>();
                    for (int i = 0; i < rows; i++) {
                        var rowTag = (char) ('A' + i);
                        for (int j = 1; j <= cols; j++) {
                            var s = new Seat("%c%d".formatted(rowTag, j), r);
                            seats.add(s);
                        }
                    }
                    roomRepository.save(r);
                    seatRepository.saveAll(seats);
                    r.setSeats(seats);
                    return r;
                }
                throw new IllegalArgumentException("Wrong shape of input data for Rooms generator");
            }).toList();

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
                String name = scanner.nextLine();

                Genre genre = new Genre(name);

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
