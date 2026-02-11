/*
package com.seroja.TrainingChekInBot.entities;

import com.seroja.TrainingChekInBot.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "attendance")
public class Attendance {

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "training_session_id", nullable = false)
    private TrainingSession trainingSession;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column (name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column (name = "reason")
    private String reason;

}
*/
