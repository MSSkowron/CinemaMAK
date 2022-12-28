package pl.edu.agh.cs.to.cinemamak.generator;

import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.cs.to.cinemamak.model.*;
import pl.edu.agh.cs.to.cinemamak.repository.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class Generator {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;

    private final PerformanceRepository performanceRepository;
    private final MovieRepository movieRepository;

    public Generator(RoleRepository roleRepository,
                     UserRepository userRepository,
                     GenreRepository genreRepository,
                     PasswordEncoder passwordEncoder,
                     RoomRepository roomRepository,
                     SeatRepository seatRepository,
                     PerformanceRepository performanceRepository,
                     MovieRepository movieRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.passwordEncoder = passwordEncoder;
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
        this.performanceRepository = performanceRepository;
        this.movieRepository = movieRepository;
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

        if(roomRepository.count() == 0) {
            generateRooms();
        }

        if(this.movieRepository.count() == 0){
            Movie movie = generateMovie();
            if(movie != null){
                this.movieRepository.save(movie);
            }
        }

        if(performanceRepository.count() == 0) {
            generatePerformance();
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
                    for (long i = 1; i <= rows; i++) {
                        for (long j = 1; j <= cols; j++) {
                            var s = new Seat(r, i, j);
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

    public Movie generateMovie(){
        Movie movie = new Movie();
        movie.setDate(new Date(2022, 12, 12));
        movie.setDescription("description1");
        movie.setTitle("title1");
        movie.setDirector("director1");
        movie.setDuration(12);

        Optional<Genre> gen = genreRepository.findGenreByGenreName("Music");
        gen.ifPresent(movie::setGenre);

        movie.setImageURL("https://static.wikia.nocookie.net/harrypotter/images/8/8d/PromoHP7_Harry_Potter.jpg/revision/latest?cb=20210613153821&path-prefix=pl");

        return movie;
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

    public void generatePerformance(){
        Performance performance = new Performance();
        performance.setDate(LocalDateTime.of(LocalDate.of(2022, 12,12), LocalTime.of(0,0,0)));

        List<Movie> movie = movieRepository.findAll();

        performance.setMovie(movie.get(0));
        performance.setPrice(BigDecimal.valueOf(111));

        List<Room> rooms = roomRepository.findAll();
        performance.setRoom(rooms.get(0));

        userRepository.findByEmailAddress("admin@gmail.com").ifPresent(performance::setUser);

        this.performanceRepository.save(performance);
    }
}
