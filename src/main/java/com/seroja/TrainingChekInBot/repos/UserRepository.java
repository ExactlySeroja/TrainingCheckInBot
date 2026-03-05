package com.seroja.TrainingChekInBot.repos;

import com.seroja.TrainingChekInBot.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean findUserByTelegramId(Long userTelegramId);
}
