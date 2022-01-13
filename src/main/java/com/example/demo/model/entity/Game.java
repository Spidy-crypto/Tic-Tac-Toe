package com.example.demo.model.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gameId;

    private Long user1Id;

    private Long user2Id;

    private String status;
    private int size;

    private Long winnerId;
}
