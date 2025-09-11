package com.sportsmotivation.prosconfig.repository;

import com.sportsmotivation.prosconfig.entity.Setting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    
    List<Setting> findByPlayerId(Long playerId);
    
    List<Setting> findByGameId(Long gameId);
    
    Optional<Setting> findByPlayerIdAndGameId(Long playerId, Long gameId);
    
    @Query("SELECT s FROM Setting s WHERE s.player.id = :playerId AND s.isVerified = true")
    List<Setting> findVerifiedSettingsByPlayerId(@Param("playerId") Long playerId);
    
    @Query("SELECT s FROM Setting s WHERE s.game.id = :gameId AND s.isVerified = true")
    Page<Setting> findVerifiedSettingsByGameId(@Param("gameId") Long gameId, Pageable pageable);
    
    @Query("SELECT s FROM Setting s JOIN s.player p WHERE " +
           "s.game.id = :gameId AND p.isActive = true AND s.isVerified = true " +
           "ORDER BY s.updatedAt DESC")
    Page<Setting> findRecentVerifiedSettingsByGame(@Param("gameId") Long gameId, Pageable pageable);
    
    @Query("SELECT COUNT(s) FROM Setting s WHERE s.isVerified = true")
    long countVerifiedSettings();
    
    List<Setting> findByIsVerifiedFalse();
}