package pl.edu.agh.cs.to.cinemamak.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.dto.UserDto;
import pl.edu.agh.cs.to.cinemamak.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,UserMapper userMapper,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        UserDto userDto = getUserByUsername(username);
        return userDto != null && !userDto.getPassword().equals(passwordEncoder.encode(password));
    }

    public void addUser(UserDto userDto) throws Exception{
        User user = userMapper.toEntity(userDto);
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
        return user.map(userMapper::toDto).orElse(null);
    }
}
