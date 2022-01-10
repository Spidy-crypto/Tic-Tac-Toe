package com.example.demo.repository;

import com.example.demo.model.entity.Move;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface MoveRepository extends CrudRepository<Move, Long> {
     List<Move> findByGameGameId(Long gameId);
     List<Move> findByUserUserId(Long userId);

}