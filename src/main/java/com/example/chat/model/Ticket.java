package com.example.chat.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ticket {

    public enum Status {
        OPEN,
        IN_PROGRESS,
        RESOLVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userName;
    private String issue;

    @Enumerated(EnumType.STRING)
    private Status status;
}