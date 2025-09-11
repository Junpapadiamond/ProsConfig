package com.sportsmotivation.prosconfig.controller;

import com.sportsmotivation.prosconfig.entity.Hardware;
import com.sportsmotivation.prosconfig.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hardware")
@CrossOrigin(origins = "*")
public class HardwareController {
    
    private final HardwareService hardwareService;
    
    @Autowired
    public HardwareController(HardwareService hardwareService) {
        this.hardwareService = hardwareService;
    }
    
    @GetMapping
    public ResponseEntity<List<Hardware>> getAllHardware() {
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Hardware>> getHardwareByPlayer(@PathVariable Long playerId) {
        List<Hardware> hardware = hardwareService.getHardwareByPlayer(playerId);
        return ResponseEntity.ok(hardware);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Hardware>> getHardwareByCategory(@PathVariable Hardware.HardwareCategory category) {
        List<Hardware> hardware = hardwareService.getHardwareByCategory(category);
        return ResponseEntity.ok(hardware);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Hardware>> searchHardware(@RequestParam String query) {
        List<Hardware> hardware = hardwareService.searchHardware(query);
        return ResponseEntity.ok(hardware);
    }
    
    @GetMapping("/brands/{category}")
    public ResponseEntity<List<String>> getBrandsByCategory(@PathVariable Hardware.HardwareCategory category) {
        List<String> brands = hardwareService.getBrandsByCategory(category);
        return ResponseEntity.ok(brands);
    }
    
    @GetMapping("/models")
    public ResponseEntity<List<String>> getModelsByBrandAndCategory(
            @RequestParam String brand,
            @RequestParam Hardware.HardwareCategory category) {
        List<String> models = hardwareService.getModelsByBrandAndCategory(brand, category);
        return ResponseEntity.ok(models);
    }
}