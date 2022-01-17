package com.example.demo.service;

import com.example.demo.converter.Converter;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.GameDto;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Game;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;


    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private Converter converter;

    @Autowired
    SimpMessagingTemplate template;

    public GameDto createGame(GameDto gameDto) {
        try {
            gameDto.setStatus("Pending");
            Game game = converter.gameDtoTOEntity(gameDto);
            gameRepository.save(game);
            return converter.entityToGameDto(game);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean joinGame(Long userId, Long gameId) {
        try{
            Game game = gameRepository.findById(gameId).orElse(null);
            if(game == null || game.getUser2Id() != null){
                return false;
            }
            game.setStatus("Playing");
            game.setUser2Id(userId);
            gameRepository.save(game);
            template.convertAndSend("/topic/user2/" + gameId, true);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public BoardDto loadBoard(Long gameId) {
        BoardDto bd = new BoardDto();
        Game game = gameRepository.findById(gameId).orElse(null);
        if(game == null){
            return null;
        }
        bd.setSize(game.getSize());
        bd.setStatus(game.getStatus());
        bd.setGameId(game.getGameId());
        List<MovesDto> movesDtoList = converter.movesToDtos(moveRepository.findByGameGameId(game.getGameId()));
        bd.setMovesDtoList(movesDtoList);
        return bd;
    }

    public List<Game> getGames(Long userId){
        return gameRepository.findByUserId(userId);
    }

    public boolean terminateGame(Long gameId) {
        try{
            Game game = gameRepository.findById(gameId).orElse(null);
            if(game != null && !game.getStatus().equals("Completed")){
                game.setStatus("Terminated");
                gameRepository.save(game);
                template.convertAndSend("/topic/message/" + game.getGameId(), "Game is Terminated.");

                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }
}
