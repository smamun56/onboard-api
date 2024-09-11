package com.hr.onboard.service.impl;

import com.hr.onboard.dto.UserDto;
import com.hr.onboard.entity.User;
import com.hr.onboard.exception.ResourceNotFoundException;
import com.hr.onboard.mapper.UserMapper;
import com.hr.onboard.repository.UserRepository;
import com.hr.onboard.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

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

        user.setName(updatedUsers.getName());
        user.setPassword(updatedUsers.getPassword());
        user.setEmail(updatedUsers.getEmail());

        User updatedUsersObject = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUsersObject);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User is not found with id" + userId));
        userRepository.deleteById(userId);
    }
}
