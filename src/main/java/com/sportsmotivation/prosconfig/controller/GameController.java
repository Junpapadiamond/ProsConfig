package com.sportsmotivation.prosconfig.controller;

import com.sportsmotivation.prosconfig.dto.GameDTO;
import com.sportsmotivation.prosconfig.entity.Game;
import com.sportsmotivation.prosconfig.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {
    
    private final GameService gameService;
    
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<Game> games = gameService.getAllActiveGames();
        List<GameDTO> gameDTOs = games.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(gameDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id)
            .map(game -> ResponseEntity.ok(convertToDTO(game)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<GameDTO> getGameByName(@PathVariable String name) {
        return gameService.getGameByName(name)
            .map(game -> ResponseEntity.ok(convertToDTO(game)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) {
        try {
            Game game = convertToEntity(gameDTO);
            Game savedGame = gameService.createGame(game);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedGame));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Long id, 
                                             @Valid @RequestBody GameDTO gameDTO) {
        try {
            Game gameDetails = convertToEntity(gameDTO);
            Game updatedGame = gameService.updateGame(id, gameDetails);
            return ResponseEntity.ok(convertToDTO(updatedGame));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        try {
            gameService.deleteGame(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    private GameDTO convertToDTO(Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setName(game.getName());
        dto.setDisplayName(game.getDisplayName());
        dto.setReleaseDate(game.getReleaseDate());
        dto.setIsActive(game.getIsActive());
        return dto;
    }
    
    private Game convertToEntity(GameDTO dto) {
        Game game = new Game();
        game.setName(dto.getName());
        game.setDisplayName(dto.getDisplayName());
        game.setReleaseDate(dto.getReleaseDate());
        return game;
    }
}