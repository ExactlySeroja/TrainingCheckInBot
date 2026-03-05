package com.seroja.TrainingChekInBot.mappers;


import com.seroja.TrainingChekInBot.dtos.StudentRegistrationDTO;
import com.seroja.TrainingChekInBot.dtos.UserDTO;
import com.seroja.TrainingChekInBot.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(AppUser appUser);

    AppUser toEntity(UserDTO userDto);

    List<UserDTO> toDtoList(List<AppUser> appAppUsers);

    List<AppUser> toEntityList(List<UserDTO> userDtos);

    @Mapping(target = "userId", ignore = true) // ID обычно не приходит из DTO при регистрации
    AppUser toEntity(StudentRegistrationDTO dto);
}
