package com.example.demo.controller;

import com.example.demo.converter.Converter;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Move;
import com.example.demo.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class MoveController {

    @Autowired
    private MoveService moveService;

    @Autowired
    private Converter converter;

    @Autowired
    SimpMessagingTemplate template;

    @RequestMapping(method = RequestMethod.POST, value = "/addMove")
    public boolean addMove(@RequestBody MovesDto moveDto){
        try {
            Move move = converter.moveDtoToEntity(moveDto);
            moveDto = moveService.addMove(move);

            if(!moveDto.isError()){
                template.convertAndSend("/topic/move", moveDto);
            }
            if(moveDto.isResult()){
                return true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @SendTo("/topic/move")
    public MovesDto message(MovesDto moveDto){
        return moveDto;
    }

    @GetMapping("/getMoves/{gameId}")
    public List<Move> getMoves(@PathVariable Long gameId){
        return moveService.getMoves(gameId);
    }
}
