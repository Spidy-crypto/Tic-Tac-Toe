package com.example.demo.converter;

import com.example.demo.dto.GameDto;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Game;
import com.example.demo.model.entity.Move;
import com.example.demo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Converter {

    @Autowired
    private GameRepository gameRepository;


    public Move moveDtoToEntity(MovesDto moveDto){
        Move move = new Move();
        move.setLocation(moveDto.getLocation());
        move.setSymbol(moveDto.getSymbol());
        move.setGame(gameRepository.findById(moveDto.getGameId()).orElse(null));
        move.setUserId(moveDto.getUserId());
        return move;
    }

    public  MovesDto entityToMoveDto(Move move){
        MovesDto moveDto = new MovesDto();
        moveDto.setLocation(move.getLocation());
        moveDto.setSymbol(move.getSymbol());
        moveDto.setGameId(move.getGame().getGameId());
        moveDto.setUserId(move.getUserId());
        return moveDto;
    }

    public List<MovesDto> movesToDtos(List<Move> moves){
        return moves.stream().map(this::entityToMoveDto).collect(Collectors.toList());
    }

    public Game gameDtoTOEntity(GameDto gameDto){
        Game game = new Game();
        game.setSize(gameDto.getSize());
        game.setUser1Id(gameDto.getUser1Id());
        game.setStatus(gameDto.getStatus());
        return game;
    }
    public GameDto entityToGameDto(Game game){
        GameDto gameDto = new GameDto();
        gameDto.setGameId(game.getGameId());
        gameDto.setUser1Id(game.getUser1Id());
        if(game.getUser2Id() != null){
            gameDto.setUser2Id(game.getUser2Id());
        }
        gameDto.setSize(game.getSize());
        gameDto.setStatus(game.getStatus());
        return gameDto;
    }
    
    public List<GameDto> entitiesToGameDtos(List<Game> games){
        List<GameDto> gameDtos = new ArrayList<>();
        for (Game game : games) {
            gameDtos.add(entityToGameDto(game));
        }
        return gameDtos;
    }
    
}
