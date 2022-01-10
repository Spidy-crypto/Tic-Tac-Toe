package com.example.demo.service;

import com.example.demo.converter.Converter;
import com.example.demo.dto.MovesDto;
import com.example.demo.model.entity.Move;
import com.example.demo.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveService {

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private Converter converter;

    public boolean checkStatus(List<Character> trio){
        return trio.get(0) == trio.get(1) && trio.get(1) == trio.get(2);
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
        if(validateMove(move)){
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
                    moveDto.setResult(true);
                }
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



    public boolean validateMove(Move move){
        List<Move> moves = getMoves(move.getGame().getGameId());
        return moves.stream().filter(m -> m.getLocation() == move.getLocation()).count() == 0;
    }

    public List<Move> getMoves(Long gameId) {
        return moveRepository.findByGameGameId(gameId);
    }
}
