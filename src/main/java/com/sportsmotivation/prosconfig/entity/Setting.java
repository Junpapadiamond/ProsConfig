package com.sportsmotivation.prosconfig.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settings", uniqueConstraints = {
    @UniqueConstraint(name = "unique_player_game", columnNames = {"player_id", "game_id"})
})
public class Setting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @Column(precision = 10, scale = 6)
    private BigDecimal sensitivity;
    
    private Integer dpi;
    
    @Column(name = "edpi", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal edpi;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "crosshair_settings", columnDefinition = "json")
    private String crosshairSettings;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String keybinds;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "video_settings", columnDefinition = "json")
    private String videoSettings;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;
    
    public Setting() {}
    
    public Setting(Player player, Game game, BigDecimal sensitivity, Integer dpi) {
        this.player = player;
        this.game = game;
        this.sensitivity = sensitivity;
        this.dpi = dpi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}