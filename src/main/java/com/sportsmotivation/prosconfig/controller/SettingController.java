package com.sportsmotivation.prosconfig.controller;

import com.sportsmotivation.prosconfig.dto.SettingDTO;
import com.sportsmotivation.prosconfig.entity.Setting;
import com.sportsmotivation.prosconfig.service.SettingService;
import com.sportsmotivation.prosconfig.service.PlayerService;
import com.sportsmotivation.prosconfig.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingController {
    
    private final SettingService settingService;
    private final PlayerService playerService;
    private final GameService gameService;
    
    @Autowired
    public SettingController(SettingService settingService, 
                           PlayerService playerService, 
                           GameService gameService) {
        this.settingService = settingService;
        this.playerService = playerService;
        this.gameService = gameService;
    }
    
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<SettingDTO>> getSettingsByPlayer(@PathVariable Long playerId) {
        List<Setting> settings = settingService.getVerifiedSettingsByPlayer(playerId);
        List<SettingDTO> settingDTOs = settings.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(settingDTOs);
    }
    
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Page<SettingDTO>> getSettingsByGame(
            @PathVariable Long gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Setting> settings = settingService.getRecentSettingsByGame(gameId, pageable);
        Page<SettingDTO> settingDTOs = settings.map(this::convertToDTO);
        
        return ResponseEntity.ok(settingDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SettingDTO> getSettingById(@PathVariable Long id) {
        return settingService.getSettingById(id)
            .map(setting -> ResponseEntity.ok(convertToDTO(setting)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<SettingDTO> createSetting(@Valid @RequestBody SettingDTO settingDTO) {
        try {
            Setting setting = convertToEntity(settingDTO);
            Setting savedSetting = settingService.createSetting(setting);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedSetting));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SettingDTO> updateSetting(@PathVariable Long id, 
                                                   @Valid @RequestBody SettingDTO settingDTO) {
        try {
            Setting settingDetails = convertToEntity(settingDTO);
            Setting updatedSetting = settingService.updateSetting(id, settingDetails);
            return ResponseEntity.ok(convertToDTO(updatedSetting));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/verify")
    public ResponseEntity<SettingDTO> verifySetting(@PathVariable Long id) {
        try {
            Setting verifiedSetting = settingService.verifySetting(id);
            return ResponseEntity.ok(convertToDTO(verifiedSetting));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        try {
            settingService.deleteSetting(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/unverified")
    public ResponseEntity<List<SettingDTO>> getUnverifiedSettings() {
        List<Setting> settings = settingService.getUnverifiedSettings();
        List<SettingDTO> settingDTOs = settings.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(settingDTOs);
    }
    
    private SettingDTO convertToDTO(Setting setting) {
        SettingDTO dto = new SettingDTO();
        dto.setId(setting.getId());
        dto.setPlayerId(setting.getPlayer().getId());
        dto.setGameId(setting.getGame().getId());
        dto.setPlayerName(setting.getPlayer().getName());
        dto.setGameName(setting.getGame().getDisplayName());
        dto.setSensitivity(setting.getSensitivity());
        dto.setDpi(setting.getDpi());
        dto.setEdpi(setting.getEdpi());
        dto.setCrosshairSettings(setting.getCrosshairSettings());
        dto.setKeybinds(setting.getKeybinds());
        dto.setVideoSettings(setting.getVideoSettings());
        dto.setIsVerified(setting.getIsVerified());
        return dto;
    }
    
    private Setting convertToEntity(SettingDTO dto) {
        Setting setting = new Setting();
        
        playerService.getPlayerById(dto.getPlayerId())
            .ifPresent(setting::setPlayer);
        
        gameService.getGameById(dto.getGameId())
            .ifPresent(setting::setGame);
        
        setting.setSensitivity(dto.getSensitivity());
        setting.setDpi(dto.getDpi());
        setting.setCrosshairSettings(dto.getCrosshairSettings());
        setting.setKeybinds(dto.getKeybinds());
        setting.setVideoSettings(dto.getVideoSettings());
        
        return setting;
    }
}