package pl.edu.agh.cs.to.cinemamak.mapper;

import pl.edu.agh.cs.to.cinemamak.dto.UserDto;
import pl.edu.agh.cs.to.cinemamak.model.User;

public class UserMapper {
    public UserMapper(){

    }

    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setEmailAddress(userDto.getEmailAddress());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmailAddress(user.getEmailAddress());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }
}
