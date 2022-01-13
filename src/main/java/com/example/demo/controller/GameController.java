package com.example.demo.controller;
import com.example.demo.converter.Converter;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.GameDto;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private Converter converter;

    @Autowired
    SimpMessagingTemplate template;

    @RequestMapping(method = RequestMethod.POST,value = "/createGame")
    public GameDto createGame(@RequestBody GameDto gameDto){
        return gameService.createGame(gameDto);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/joinGame/{gameId}/{userId}")
    public boolean joinGame(@PathVariable Long userId, @PathVariable Long gameId){
        if(gameService.joinGame(userId, gameId)){
            template.convertAndSend("/topic/message/" + gameId, "User2 Join the game");
            return true;
        }
        return false;
    }

    @RequestMapping("/getGames/{userId}")
    public List<GameDto> getGames(@PathVariable Long userId){
         return converter.entitiesToGameDtos(gameService.getGames(userId));
    }

    @GetMapping("game/{gameId}")
    public BoardDto loadBoard(@PathVariable Long gameId){
        return gameService.loadBoard(gameId);
    }
}
