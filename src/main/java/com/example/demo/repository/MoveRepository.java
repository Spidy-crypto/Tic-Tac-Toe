package com.example.demo.repository;

import com.example.demo.model.entity.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MoveRepository extends CrudRepository<Move, Long> {
     public List<Move> findByGameGameId(Long gameId);
     List<Move> findByUserUserId(Long userId);

}