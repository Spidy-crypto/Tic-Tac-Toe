package com.example.demo.controller;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.GameDto;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(method = RequestMethod.POST,value = "/createGame")
    public GameDto createGame(@RequestBody GameDto gameDto){
        return gameService.createGame(gameDto);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/joinGame/{userId}/{gameId}")
    public boolean joinGame(@PathVariable Long userId, @PathVariable Long gameId){
        return gameService.joinGame(userId, gameId);
    }

    @GetMapping("game/{userId}")
    public BoardDto loadBoard(@PathVariable Long userId){
        return gameService.loadBoard(userId);
    }
}
