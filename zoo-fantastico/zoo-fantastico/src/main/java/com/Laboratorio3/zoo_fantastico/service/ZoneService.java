package com.Laboratorio3.zoo_fantastico.service;

import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone){
        return zoneRepository.save(zone);
    }

    public List<Zone> getAllZones(){
        return zoneRepository.findAll();
    }

    public Zone getZoneById(Long id){
        return zoneRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Zone not found"));
    }

    public Zone updateZone(Long id, Zone updatedZone){
        Zone zone = getZoneById(id);
        zone.setName(updatedZone.getName());
        zone.setDescription(updatedZone.getDescription());
        zone.setCapacity(updatedZone.getCapacity());
        return zoneRepository.save(zone);
    }

    public void deleteZone(Long id){
        Zone zone = getZoneById(id);
        zoneRepository.deleteById(id);
    }

}
