package com.Laboratorio3.zoo_fantastico.service;

import java.util.List;

import com.Laboratorio3.zoo_fantastico.dto.CreatureDTO;
import com.Laboratorio3.zoo_fantastico.entity.Creature;
import com.Laboratorio3.zoo_fantastico.repository.CreatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CreatureService {

    private final CreatureRepository creatureRepository;

    private final ZoneService zoneService;

    // ---------- helpers ----------
    private void validate(CreatureDTO dto) {
        if (dto.getSize() < 0) {
            throw new RuntimeException("size must be >= 0");
        }
        if (dto.getDangerLevel() < 1 || dto.getDangerLevel() > 10) {
            throw new RuntimeException("dangerLevel must be between 1 and 10");
        }
        if (dto.getZoneId() == null) {
            throw new RuntimeException("zoneId is required");
        }
    }

    private Creature mapper(Creature target, CreatureDTO dto) {
        target.setName(dto.getName());
        target.setSpecies(dto.getSpecies());
        target.setSize(dto.getSize());
        target.setDangerLevel(dto.getDangerLevel());
        target.setHealthStatus(dto.getHealthStatus());
        target.setZone(zoneService.getZoneById(dto.getZoneId()));
        return target;
    }

    @Autowired
    public CreatureService(CreatureRepository creatureRepository, ZoneService zoneService) {
        this.creatureRepository = creatureRepository;
        this.zoneService = zoneService;
    }

    public Creature createCreature(CreatureDTO creature) {
        return creatureRepository.save(mapper(new Creature(), creature));
    }

    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Creature not found"));
    }

    public Creature updateCreature(Long id, CreatureDTO updatedCreature) {
        Creature creature = getCreatureById(id);
        return creatureRepository.save(mapper(creature, updatedCreature));
    }

    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
    }
}