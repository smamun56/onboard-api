package com.hr.onboard.service.user;

import com.hr.onboard.dto.UserDto;
import com.hr.onboard.entity.auth.User;
import com.hr.onboard.exception.ResourceNotFoundException;
import com.hr.onboard.mapper.UserMapper;
import com.hr.onboard.repository.UserRepository;
import com.hr.onboard.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

//    private

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser =  userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + userId));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> employees = userRepository.findAll();
        return employees.stream().map((e) -> UserMapper.mapToUserDto(e)).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updatedUsers) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User is not exists with id " + userId));
        try {
            user.setName(updatedUsers.getName());
            user.setPassword(updatedUsers.getPassword());
            user.setEmail(updatedUsers.getEmail());

            User updatedUsersObject = userRepository.save(user);
            return UserMapper.mapToUserDto(updatedUsersObject);
        } catch (Exception e){
//            System.out.println(e.getMessage());
            return UserMapper.mapToUserDto(user);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User is not found with id" + userId));
        userRepository.deleteById(userId);
    }
}
