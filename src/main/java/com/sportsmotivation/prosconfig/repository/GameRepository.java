package com.sportsmotivation.prosconfig.repository;

import com.sportsmotivation.prosconfig.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    List<Game> findByIsActiveTrue();
    
    Optional<Game> findByNameIgnoreCase(String name);
    
    @Query("SELECT g FROM Game g WHERE g.isActive = true ORDER BY g.displayName")
    List<Game> findAllActiveOrderByDisplayName();
    
    boolean existsByNameIgnoreCase(String name);
}