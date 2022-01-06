package com.example.demo.controller;

import com.example.demo.converter.Converter;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Move;
import com.example.demo.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MoveController {

    @Autowired
    private MoveService moveService;

    @Autowired
    private Converter converter;

    @RequestMapping(method = RequestMethod.POST, value = "/addMove")
//    @SendTo("/topic/move")
    public boolean addMove(@RequestBody MovesDto moveDto){
        try {
            Move move = converter.moveDtoToEntity(moveDto);
            return moveService.addMove(move);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping("/getMoves/{gameId}")
    public List<Move> getMoves(@PathVariable Long gameId){
        return moveService.getMoves(gameId);
    }
}
