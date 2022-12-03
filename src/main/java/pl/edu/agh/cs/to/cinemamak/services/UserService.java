package pl.edu.agh.cs.to.cinemamak.services;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.dto.UserDto;
import pl.edu.agh.cs.to.cinemamak.models.Role;
import pl.edu.agh.cs.to.cinemamak.models.User;
import pl.edu.agh.cs.to.cinemamak.repositories.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,ModelMapper modelMapper,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        Optional<Role> role = roleRepository.findByRoleName("Employee");
        if(role.isPresent()){
            user.setRole(role.get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }
    public UserDto getUserByUsername(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return null;
        }

        Optional<User> user = userRepository.findByEmailAddress(emailAddress);
        return user.map(this::convertToDto).orElse(null);
    }

    public User convertToEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
