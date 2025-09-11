package com.sportsmotivation.prosconfig.service;

import com.sportsmotivation.prosconfig.entity.Player;
import com.sportsmotivation.prosconfig.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {
    
    private final PlayerRepository playerRepository;
    
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Player> getAllActivePlayers() {
        return playerRepository.findByIsActiveTrue();
    }
    
    @Transactional(readOnly = true)
    public Page<Player> getAllActivePlayers(Pageable pageable) {
        return playerRepository.findByIsActiveTrue(pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByName(String name) {
        return playerRepository.findByNameIgnoreCase(name);
    }
    
    @Transactional(readOnly = true)
    public Page<Player> searchPlayers(String searchTerm, Pageable pageable) {
        return playerRepository.searchByNameOrRealName(searchTerm, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Player> getPlayersByCountry(String country) {
        return playerRepository.findByCountryAndIsActiveTrue(country);
    }
    
    @Transactional(readOnly = true)
    public List<Player> getPlayersByTeam(Long teamId) {
        return playerRepository.findByTeamIdAndIsActiveTrue(teamId);
    }
    
    public Player createPlayer(Player player) {
        player.setIsActive(true);
        return playerRepository.save(player);
    }
    
    public Player updatePlayer(Long id, Player playerDetails) {
        return playerRepository.findById(id)
            .map(player -> {
                player.setName(playerDetails.getName());
                player.setRealName(playerDetails.getRealName());
                player.setTeamId(playerDetails.getTeamId());
                player.setCountry(playerDetails.getCountry());
                return playerRepository.save(player);
            })
            .orElseThrow(() -> new RuntimeException("Player not found with id: " + id));
    }
    
    public void deletePlayer(Long id) {
        playerRepository.findById(id)
            .ifPresentOrElse(
                player -> {
                    player.setIsActive(false);
                    playerRepository.save(player);
                },
                () -> { throw new RuntimeException("Player not found with id: " + id); }
            );
    }
    
    @Transactional(readOnly = true)
    public long getActivePlayerCount() {
        return playerRepository.countActivePlayers();
    }
}