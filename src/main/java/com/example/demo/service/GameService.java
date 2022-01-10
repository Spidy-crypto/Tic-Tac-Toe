package com.example.demo.service;

import com.example.demo.converter.Converter;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.GameDto;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Game;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MoveRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private Converter converter;

    public GameDto createGame(GameDto gameDto) {
        try {
            gameDto.setStatus("Pending");
            Game game = converter.gameDtoTOEntity(gameDto);
            int count = gameRepository.findActiveGame(gameDto.getUser1Id());
            if(count == 0){
                gameRepository.save(game);
                return converter.entityToGameDto(game);
            }
            return new GameDto();
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
            game.setUser2Id(userRepository.findById(userId).orElse(null));
            gameRepository.save(game);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //need to solve completed issue.
    public BoardDto loadBoard(Long userId) {
        BoardDto bd = new BoardDto();
        Game game = gameRepository.findByUser1IdUserId(userId).stream().findFirst().orElse(null);
        if(game == null){
            game = gameRepository.findByUser2IdUserId(userId).stream().findFirst().orElse(null);
        }
        if(game == null){
            return bd;
        }
        bd.setSize(game.getSize());
        bd.setGameId(game.getGameId());
        System.out.println(moveRepository);
        List<MovesDto> movesDtoList = converter.movesToDtos(moveRepository.findByGameGameId(game.getGameId()));
        bd.setMovesDtoList(movesDtoList);
        return bd;
    }
}
