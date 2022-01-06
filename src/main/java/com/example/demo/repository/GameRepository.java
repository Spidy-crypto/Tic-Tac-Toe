package com.example.demo.repository;

import com.example.demo.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByUser1IdUserId(Long userId);
    List<Game> findByUser2IdUserId(Long userId);

    @Query(value = "SELECT COUNT(*) FROM GAME WHERE USER1ID_USER_ID = ?1 AND STATUS != 'Completed'", nativeQuery = true)
    int findActiveGame(Long userId);

}
