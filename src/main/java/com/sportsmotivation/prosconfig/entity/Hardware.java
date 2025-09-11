package com.sportsmotivation.prosconfig.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "hardware")
public class Hardware {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HardwareCategory category;
    
    @Size(max = 50)
    @Column(length = 50)
    private String brand;
    
    @Size(max = 100)
    @Column(length = 100)
    private String model;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private String specifications;
    
    public enum HardwareCategory {
        MOUSE, KEYBOARD, MONITOR, HEADSET, MOUSEPAD
    }
    
    public Hardware() {}
    
    public Hardware(Player player, HardwareCategory category, String brand, String model) {
        this.player = player;
        this.category = category;
        this.brand = brand;
        this.model = model;
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

    public HardwareCategory getCategory() {
        return category;
    }

    public void setCategory(HardwareCategory category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
}