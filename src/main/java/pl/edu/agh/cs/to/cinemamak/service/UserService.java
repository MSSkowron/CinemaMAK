package pl.edu.agh.cs.to.cinemamak.service;

import org.hibernate.AssertionFailure;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.cs.to.cinemamak.model.Role;
import pl.edu.agh.cs.to.cinemamak.model.RoleName;
import pl.edu.agh.cs.to.cinemamak.model.User;
import pl.edu.agh.cs.to.cinemamak.repository.RoleRepository;
import pl.edu.agh.cs.to.cinemamak.repository.UserRepository;

import java.util.List;
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

    public boolean authenticate(String email, String password) {
        var user = getUserByEmail(email);
//        System.out.println("Authenticate: "+password+" "+passwordEncoder.encode(password));
//        return user.isPresent() && user.get().getPassword().equals(passwordEncoder.encode(password));
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public void addUser(User user) throws Exception{
        Optional<Role> role = roleRepository.findByRoleName(RoleName.Employee.toString());
        if(role.isPresent()){
            user.setRole(role.get());
//            String passEncode = passwordEncoder.encode(user.getPassword());
//            System.out.println("Adduser: "+user.getPassword()+" "+passEncode);
//            user.setPassword(passEncode);
            user.setPassword(user.getPassword());
            userRepository.save(user);
        }
    }
    public Optional<User> getUserByEmail(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return Optional.empty();
        }

        return userRepository.findByEmailAddress(emailAddress);
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public void updateUser(User user) {
        //    Update operation requires object user to have specified id and role.

        if(user.getRole() == null){
            throw new NullPointerException();
        }

        this.deleteUser(user);

        Optional<Role> role = roleRepository.findByRoleName(user.getRole().getRoleName().toString());
        if(role.isPresent()){
            user.setRole(role.get());
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPassword(user.getPassword());
            userRepository.save(user);
        }
    }

    public void deleteUser(User u){

        if(u.getId() == null){
            throw new NullPointerException();
        }

        if(userRepository.findById(u.getId()).isPresent()){
            userRepository.deleteById(u.getId());
        }
    }

    public Optional<List<User>> getUsers(){
        return Optional.of(userRepository.findAll());
    }
}
