package com.Laboratorio3.zoo_fantastico.service;

import com.Laboratorio3.zoo_fantastico.exception.BusinessException;
import com.Laboratorio3.zoo_fantastico.entity.Creature;
import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.Laboratorio3.zoo_fantastico.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;



    public Zone createZone(Zone zone){
        return zoneRepository.save(zone);
    }

    public List<Zone> getAllZones(){
        return zoneRepository.findAll();
    }
    public CreatureRepository creatureRepository;

    public Zone getZoneById(Long id){
        return zoneRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Zone not found"));
    }
    @Autowired
    public ZoneService(ZoneRepository zoneRepository,
                       CreatureRepository creatureRepository) {
        this.zoneRepository = zoneRepository;
        this.creatureRepository = creatureRepository;
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
        List<Creature> lista= creatureRepository.findByZoneId(id);
        System.out.println(">>> deleteZone() ejecutado. Criaturas encontradas: " + lista.size());
        if(lista.isEmpty()){
            zoneRepository.deleteById(id);
        }
        else {
            System.out.println("Criaturas asignadas : "+lista.size());
            throw new BusinessException("Zone has creatures, cannot delete");}



    }

}
