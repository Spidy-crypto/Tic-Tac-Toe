package com.example.demo.repository;

import com.example.demo.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * FROM GAME WHERE USER1ID = ?1 OR USER2ID = ?1", nativeQuery = true)
    List<Game> findByUserId(Long userId);

}
