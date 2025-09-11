package com.sportsmotivation.prosconfig.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PlayerDTO {
    
    private Long id;
    
    @NotBlank(message = "Player name is required")
    @Size(max = 100, message = "Player name must not exceed 100 characters")
    private String name;
    
    @Size(max = 100, message = "Real name must not exceed 100 characters")
    private String realName;
    
    private Long teamId;
    
    @Size(max = 3, message = "Country code must not exceed 3 characters")
    private String country;
    
    private Boolean isActive;
    
    public PlayerDTO() {}
    
    public PlayerDTO(String name, String realName, String country) {
        this.name = name;
        this.realName = realName;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}