package com.example.demo.dto;

import lombok.Data;

@Data
public class GameDto {

    private Long gameId;
    private Long user1Id;
    private Long user2Id;
    private int size;
    private String status;

}
