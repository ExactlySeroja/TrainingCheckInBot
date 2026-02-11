/*
package com.seroja.TrainingChekInBot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "training_session")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_session_id", nullable = false)
    private Long trainingSessionId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "starting_at")
    private LocalTime startingAt;

    @NotNull
    @Column(name = "finishing_at")
    private LocalTime finishingAt;

}
*/
