/*
package com.seroja.TrainingChekInBot.entities;

import com.seroja.TrainingChekInBot.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "second_name", nullable = false)
    private String secondName;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

}
*/
