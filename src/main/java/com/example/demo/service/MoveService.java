package com.example.demo.service;

import com.example.demo.model.entity.Move;
import com.example.demo.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveService {

    @Autowired
    private MoveRepository moveRepository;

    public boolean addMove(Move move){
        if(validateMove(move)){
            try{
                moveRepository.save(move);
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    public boolean validateMove(Move move){
        List<Move> moves = getMoves(move.getGame().getGameId());
        return moves.stream().filter(m -> m.getLocation() == move.getLocation()).count() == 0;
    }

    public List<Move> getMoves(Long gameId) {
        return moveRepository.findByGameGameId(gameId);
    }
}
