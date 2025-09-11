package com.sportsmotivation.prosconfig.repository;

import com.sportsmotivation.prosconfig.entity.Hardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardwareRepository extends JpaRepository<Hardware, Long> {
    
    List<Hardware> findByPlayerId(Long playerId);
    
    List<Hardware> findByCategory(Hardware.HardwareCategory category);
    
    List<Hardware> findByPlayerIdAndCategory(Long playerId, Hardware.HardwareCategory category);
    
    @Query("SELECT h FROM Hardware h WHERE " +
           "(LOWER(h.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(h.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Hardware> searchByBrandOrModel(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT h.brand FROM Hardware h WHERE h.category = :category ORDER BY h.brand")
    List<String> findDistinctBrandsByCategory(@Param("category") Hardware.HardwareCategory category);
    
    @Query("SELECT DISTINCT h.model FROM Hardware h WHERE h.brand = :brand AND h.category = :category ORDER BY h.model")
    List<String> findDistinctModelsByBrandAndCategory(@Param("brand") String brand, 
                                                     @Param("category") Hardware.HardwareCategory category);
}