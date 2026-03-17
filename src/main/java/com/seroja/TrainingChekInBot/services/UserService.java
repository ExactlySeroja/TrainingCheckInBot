package com.seroja.TrainingChekInBot.services;

import com.seroja.TrainingChekInBot.dtos.UserRegistrationDTO;
import com.seroja.TrainingChekInBot.enums.Role;
import com.seroja.TrainingChekInBot.mappers.UserMapper;
import com.seroja.TrainingChekInBot.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public boolean checkUserByTelegramId(Long telegramId){
        return (userRepository.existsAppUsersByTelegramId(telegramId));
    }


    public void addUser(User user, Role role, Long chatId) {
        UserRegistrationDTO newStudent = new UserRegistrationDTO(user.getFirstName(), user.getLastName(), role, chatId);
        userRepository.save(userMapper.toEntity(newStudent));
    }

    public void addApplication(User user, Long chatId){

    }

}
