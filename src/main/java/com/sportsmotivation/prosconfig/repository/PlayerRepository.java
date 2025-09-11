package com.sportsmotivation.prosconfig.repository;

import com.sportsmotivation.prosconfig.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    List<Player> findByIsActiveTrue();
    
    Optional<Player> findByNameIgnoreCase(String name);
    
    Page<Player> findByIsActiveTrue(Pageable pageable);
    
    @Query("SELECT p FROM Player p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.realName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isActive = true")
    Page<Player> searchByNameOrRealName(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    List<Player> findByCountryAndIsActiveTrue(String country);
    
    List<Player> findByTeamIdAndIsActiveTrue(Long teamId);
    
    @Query("SELECT COUNT(p) FROM Player p WHERE p.isActive = true")
    long countActivePlayers();
}