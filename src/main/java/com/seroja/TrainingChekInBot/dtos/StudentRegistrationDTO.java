package com.seroja.TrainingChekInBot.dtos;

import com.seroja.TrainingChekInBot.enums.Role;

public record StudentRegistrationDTO (String firstName, String secondName, Role role, Long telegramId){
}
