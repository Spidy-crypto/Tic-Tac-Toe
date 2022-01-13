package com.example.demo.service;

import com.example.demo.converter.Converter;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Game;
import com.example.demo.model.entity.Move;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveService {

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private Converter converter;

    @Autowired
    SimpMessagingTemplate template;

    public boolean checkStatus(List<Character> trio){
        for(int i=1;i< trio.size();i++){
            if(trio.get(i) != trio.get(i - 1)){
                return false;
            }
        }
        return true;
    }

    public boolean checkWinner(List<List<Character>> board, int size){
        List<Character> fdiag = new ArrayList<>();
        List<Character> bdiag = new ArrayList<>();
        List<Character> row;
        List<Character> col = new ArrayList<>();
        for (int i = 0; i<size;i++){
            row = board.get(i);
            if(checkStatus(row)){
                return true;
            }
            col.clear();
            for(int j=0;j<size;j++){
                col.add(board.get(j).get(i));
                if(i+j == size-1){
                    bdiag.add(board.get(i).get(j));
                }
            }
            if(checkStatus(col)){
                return true;
            }
            fdiag.add(board.get(i).get(i));
        }
        return checkStatus(fdiag) || checkStatus(bdiag);
    }

    //set board
    private void setBoard(Move move, List<List<Character>> board, int size) {
        int row = (int) (Math.ceil(move.getLocation() / (float)size)) -1;
        int col = move.getLocation() % size - 1;
        if(col < 0){
            col += size;
        }
        board.get(row).set(col, move.getSymbol());
    }

    public MovesDto addMove(Move move){

        MovesDto movesDto = new MovesDto();
        Game game = gameRepository.findById(move.getGame().getGameId()).orElse(null);

        if(validateMove(move,game)){
            try{
                List<Move> moves = getMoves(move.getGame().getGameId());
                MovesDto moveDto = converter.entityToMoveDto(move);

                List<List<Character>> board = new ArrayList<>();
                int size = move.getGame().getSize();
                int count = 1;
                for(int i=0;i<size;i++){
                    ArrayList<Character> row= new ArrayList<>();
                    for(int j=0;j<size;j++){
                        row.add((char) (count + '0'));
                        count++;
                    }
                    board.add(row);
                }

                for(Move m : moves){
                    setBoard(m, board, size);
                }
                setBoard(move, board, size);

                if(checkWinner(board,size)){
                    game.setStatus("Completed");

                    moveDto.setResult(true);
                    if(game.getWinnerId() == null){
                        game.setWinnerId(move.getUserId());
                    }
                    moveDto.setWinnerId(game.getWinnerId());
                    template.convertAndSend("/topic/message/" + game.getGameId(), move.getUserId() + " Wins the game!");
                }else{
                    if(moves.size() == game.getSize() * game.getSize() - 1){
                        game.setStatus("Completed");
                        template.convertAndSend("/topic/message/" + game.getGameId(), "Draw!!");

                    }
                }
                gameRepository.save(game);
                moveRepository.save(move);
                moveDto.setError(false);
                return moveDto;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        movesDto.setError(true);
        return movesDto;
    }



    public boolean validateMove(Move move, Game game){
        if(game.getStatus() == "Completed"){
            return false;
        }
        List<Move> moves = getMoves(move.getGame().getGameId());
        if(moves.size() == 0){
            if(move.getUserId() != game.getUser1Id()){
                return false;
            }
        }
        else if(move.getUserId() == moves.get(moves.size()-1).getUserId()){
            return false;
        }
        return moves.stream().filter(m -> m.getLocation() == move.getLocation()).count() == 0;
    }

    public List<Move> getMoves(Long gameId) {
        return moveRepository.findByGameGameId(gameId);
    }
}
