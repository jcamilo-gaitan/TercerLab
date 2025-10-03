package com.Laboratorio3.zoo_fantastico.controller;

import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone){
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createZone(zone));
    }

    @GetMapping
    public List<Zone> getAllZones(){
        return zoneService.getAllZones();
    }

    @GetMapping("/{id}")
    public Zone getZoneById(@PathVariable Long id){
        return zoneService.getZoneById(id);
    }

    @PutMapping("/{id}")
    public Zone updateZone(@PathVariable Long id, @RequestBody Zone updatedZone){
        return zoneService.updateZone(id, updatedZone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id){
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

}
