package com.example.demo.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long moveId;

    @ManyToOne
    @JoinColumn(name = "gameId", referencedColumnName = "gameId")
    private Game game;

    private Long userId;

    @Column(nullable = false)
    private int location;

    @Column(nullable = false)
    private char symbol;
}
