package com.seroja.TrainingChekInBot.dtos;

import com.seroja.TrainingChekInBot.enums.Role;

public record UserDTO(String userId, String firstName, String secondName, Role role, String telegramId) {
}
