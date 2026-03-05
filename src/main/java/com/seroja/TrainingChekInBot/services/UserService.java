package com.seroja.TrainingChekInBot.services;

import com.seroja.TrainingChekInBot.dtos.StudentRegistrationDTO;
import com.seroja.TrainingChekInBot.mappers.UserMapper;
import com.seroja.TrainingChekInBot.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public boolean findUserByTelegramId(Long telegramId){
        return (userRepository.findUserByTelegramId(telegramId));
    }


    public void addUser(StudentRegistrationDTO newStudent) {
        userRepository.save(userMapper.toEntity(newStudent));
    }
}
