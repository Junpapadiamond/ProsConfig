package com.sportsmotivation.prosconfig.service;

import com.sportsmotivation.prosconfig.entity.Hardware;
import com.sportsmotivation.prosconfig.repository.HardwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HardwareService {
    
    private final HardwareRepository hardwareRepository;
    
    @Autowired
    public HardwareService(HardwareRepository hardwareRepository) {
        this.hardwareRepository = hardwareRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Hardware> getHardwareByPlayer(Long playerId) {
        return hardwareRepository.findByPlayerId(playerId);
    }
    
    @Transactional(readOnly = true)
    public List<Hardware> getHardwareByCategory(Hardware.HardwareCategory category) {
        return hardwareRepository.findByCategory(category);
    }
    
    @Transactional(readOnly = true)
    public List<Hardware> getHardwareByPlayerAndCategory(Long playerId, Hardware.HardwareCategory category) {
        return hardwareRepository.findByPlayerIdAndCategory(playerId, category);
    }
    
    @Transactional(readOnly = true)
    public List<Hardware> searchHardware(String searchTerm) {
        return hardwareRepository.searchByBrandOrModel(searchTerm);
    }
    
    @Transactional(readOnly = true)
    public List<String> getBrandsByCategory(Hardware.HardwareCategory category) {
        return hardwareRepository.findDistinctBrandsByCategory(category);
    }
    
    @Transactional(readOnly = true)
    public List<String> getModelsByBrandAndCategory(String brand, Hardware.HardwareCategory category) {
        return hardwareRepository.findDistinctModelsByBrandAndCategory(brand, category);
    }
    
    @Transactional(readOnly = true)
    public Optional<Hardware> getHardwareById(Long id) {
        return hardwareRepository.findById(id);
    }
    
    public Hardware createHardware(Hardware hardware) {
        return hardwareRepository.save(hardware);
    }
    
    public Hardware updateHardware(Long id, Hardware hardwareDetails) {
        return hardwareRepository.findById(id)
            .map(hardware -> {
                hardware.setCategory(hardwareDetails.getCategory());
                hardware.setBrand(hardwareDetails.getBrand());
                hardware.setModel(hardwareDetails.getModel());
                hardware.setSpecifications(hardwareDetails.getSpecifications());
                return hardwareRepository.save(hardware);
            })
            .orElseThrow(() -> new RuntimeException("Hardware not found with id: " + id));
    }
    
    public void deleteHardware(Long id) {
        hardwareRepository.deleteById(id);
    }
}