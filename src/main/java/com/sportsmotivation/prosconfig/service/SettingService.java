package com.sportsmotivation.prosconfig.service;

import com.sportsmotivation.prosconfig.entity.Setting;
import com.sportsmotivation.prosconfig.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SettingService {
    
    private final SettingRepository settingRepository;
    
    @Autowired
    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Setting> getSettingsByPlayer(Long playerId) {
        return settingRepository.findByPlayerId(playerId);
    }
    
    @Transactional(readOnly = true)
    public List<Setting> getVerifiedSettingsByPlayer(Long playerId) {
        return settingRepository.findVerifiedSettingsByPlayerId(playerId);
    }
    
    @Transactional(readOnly = true)
    public Page<Setting> getSettingsByGame(Long gameId, Pageable pageable) {
        return settingRepository.findVerifiedSettingsByGameId(gameId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Setting> getRecentSettingsByGame(Long gameId, Pageable pageable) {
        return settingRepository.findRecentVerifiedSettingsByGame(gameId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Setting> getSettingByPlayerAndGame(Long playerId, Long gameId) {
        return settingRepository.findByPlayerIdAndGameId(playerId, gameId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Setting> getSettingById(Long id) {
        return settingRepository.findById(id);
    }
    
    public Setting createSetting(Setting setting) {
        return settingRepository.save(setting);
    }
    
    public Setting updateSetting(Long id, Setting settingDetails) {
        return settingRepository.findById(id)
            .map(setting -> {
                setting.setSensitivity(settingDetails.getSensitivity());
                setting.setDpi(settingDetails.getDpi());
                setting.setCrosshairSettings(settingDetails.getCrosshairSettings());
                setting.setKeybinds(settingDetails.getKeybinds());
                setting.setVideoSettings(settingDetails.getVideoSettings());
                return settingRepository.save(setting);
            })
            .orElseThrow(() -> new RuntimeException("Setting not found with id: " + id));
    }
    
    public Setting verifySetting(Long id) {
        return settingRepository.findById(id)
            .map(setting -> {
                setting.setIsVerified(true);
                return settingRepository.save(setting);
            })
            .orElseThrow(() -> new RuntimeException("Setting not found with id: " + id));
    }
    
    public void deleteSetting(Long id) {
        settingRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Setting> getUnverifiedSettings() {
        return settingRepository.findByIsVerifiedFalse();
    }
    
    @Transactional(readOnly = true)
    public long getVerifiedSettingsCount() {
        return settingRepository.countVerifiedSettings();
    }
}