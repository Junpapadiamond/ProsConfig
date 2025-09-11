package com.sportsmotivation.prosconfig.service;

import com.sportsmotivation.prosconfig.entity.Game;
import com.sportsmotivation.prosconfig.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {
    
    private final GameRepository gameRepository;
    
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Game> getAllActiveGames() {
        return gameRepository.findAllActiveOrderByDisplayName();
    }
    
    @Transactional(readOnly = true)
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Game> getGameByName(String name) {
        return gameRepository.findByNameIgnoreCase(name);
    }
    
    public Game createGame(Game game) {
        if (gameRepository.existsByNameIgnoreCase(game.getName())) {
            throw new RuntimeException("Game with name '" + game.getName() + "' already exists");
        }
        game.setIsActive(true);
        return gameRepository.save(game);
    }
    
    public Game updateGame(Long id, Game gameDetails) {
        return gameRepository.findById(id)
            .map(game -> {
                game.setDisplayName(gameDetails.getDisplayName());
                game.setReleaseDate(gameDetails.getReleaseDate());
                return gameRepository.save(game);
            })
            .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }
    
    public void deleteGame(Long id) {
        gameRepository.findById(id)
            .ifPresentOrElse(
                game -> {
                    game.setIsActive(false);
                    gameRepository.save(game);
                },
                () -> { throw new RuntimeException("Game not found with id: " + id); }
            );
    }
}