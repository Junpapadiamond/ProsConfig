package com.sportsmotivation.prosconfig.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class SettingDTO {
    
    private Long id;
    
    @NotNull(message = "Player ID is required")
    private Long playerId;
    
    @NotNull(message = "Game ID is required")
    private Long gameId;
    
    private String playerName;
    private String gameName;
    
    @Positive(message = "Sensitivity must be positive")
    private BigDecimal sensitivity;
    
    @Positive(message = "DPI must be positive")
    private Integer dpi;
    
    private BigDecimal edpi;
    private String crosshairSettings;
    private String keybinds;
    private String videoSettings;
    private Boolean isVerified;
    
    public SettingDTO() {}
    
    public SettingDTO(Long playerId, Long gameId, BigDecimal sensitivity, Integer dpi) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.sensitivity = sensitivity;
        this.dpi = dpi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public BigDecimal getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(BigDecimal sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Integer getDpi() {
        return dpi;
    }

    public void setDpi(Integer dpi) {
        this.dpi = dpi;
    }

    public BigDecimal getEdpi() {
        return edpi;
    }

    public void setEdpi(BigDecimal edpi) {
        this.edpi = edpi;
    }

    public String getCrosshairSettings() {
        return crosshairSettings;
    }

    public void setCrosshairSettings(String crosshairSettings) {
        this.crosshairSettings = crosshairSettings;
    }

    public String getKeybinds() {
        return keybinds;
    }

    public void setKeybinds(String keybinds) {
        this.keybinds = keybinds;
    }

    public String getVideoSettings() {
        return videoSettings;
    }

    public void setVideoSettings(String videoSettings) {
        this.videoSettings = videoSettings;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}