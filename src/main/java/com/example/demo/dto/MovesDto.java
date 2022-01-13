package com.example.demo.dto;

import lombok.Data;

@Data
public class MovesDto {

    private Long gameId;
    private Long userId;
    private int location;
    private char symbol;
    private boolean result;
    private boolean error;
    private Long winnerId;

}
