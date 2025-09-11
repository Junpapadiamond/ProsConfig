package com.sportsmotivation.prosconfig.controller;

import com.sportsmotivation.prosconfig.dto.PlayerDTO;
import com.sportsmotivation.prosconfig.entity.Player;
import com.sportsmotivation.prosconfig.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {
    
    private final PlayerService playerService;
    
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @GetMapping
    public ResponseEntity<Page<PlayerDTO>> getAllPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Player> players = playerService.getAllActivePlayers(pageable);
        Page<PlayerDTO> playerDTOs = players.map(this::convertToDTO);
        
        return ResponseEntity.ok(playerDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
            .map(player -> ResponseEntity.ok(convertToDTO(player)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<PlayerDTO>> searchPlayers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Player> players = playerService.searchPlayers(query, pageable);
        Page<PlayerDTO> playerDTOs = players.map(this::convertToDTO);
        
        return ResponseEntity.ok(playerDTOs);
    }
    
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        Player player = convertToEntity(playerDTO);
        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedPlayer));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, 
                                                 @Valid @RequestBody PlayerDTO playerDTO) {
        try {
            Player playerDetails = convertToEntity(playerDTO);
            Player updatedPlayer = playerService.updatePlayer(id, playerDetails);
            return ResponseEntity.ok(convertToDTO(updatedPlayer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        try {
            playerService.deletePlayer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/country/{country}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByCountry(@PathVariable String country) {
        List<Player> players = playerService.getPlayersByCountry(country);
        List<PlayerDTO> playerDTOs = players.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByTeam(@PathVariable Long teamId) {
        List<Player> players = playerService.getPlayersByTeam(teamId);
        List<PlayerDTO> playerDTOs = players.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(playerDTOs);
    }
    
    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setRealName(player.getRealName());
        dto.setTeamId(player.getTeamId());
        dto.setCountry(player.getCountry());
        dto.setIsActive(player.getIsActive());
        return dto;
    }
    
    private Player convertToEntity(PlayerDTO dto) {
        Player player = new Player();
        player.setName(dto.getName());
        player.setRealName(dto.getRealName());
        player.setTeamId(dto.getTeamId());
        player.setCountry(dto.getCountry());
        return player;
    }
}