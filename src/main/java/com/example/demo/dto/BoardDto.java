package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto {
    private Long gameId;
    private int size;
    private List<MovesDto> movesDtoList;
}
