package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private Long gameId;
    private String status;
    private int size;
    private List<MovesDto> movesDtoList;
}
