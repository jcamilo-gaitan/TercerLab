package com.laboratorio3.zoo_fantastico.service;

import java.util.List;
import com.laboratorio3.zoo_fantastico.entity.Creature;
import com.laboratorio3.zoo_fantastico.entity.Zone;
import com.laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.laboratorio3.zoo_fantastico.repository.ZoneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;
    private final ZoneRepository zoneRepository;

    @Autowired
    public CreatureService(CreatureRepository creatureRepository, ZoneRepository zoneRepository) {
        this.creatureRepository = creatureRepository;
        this.zoneRepository = zoneRepository;
    }
/*    public Creature createCreature(Creature c) {
        c.setZone(resolveZone(c.getZone())); // acepta { "zone": { "id": X } } o null
        return creatureRepository.save(c);
    }
 */

    public Creature createCreature(Creature creature) {
        return creatureRepository.save(creature);
    }

    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }

    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Creature not found"));
    }

    public Creature updateCreature(Long id, Creature updatedCreature) {
        Creature creature = getCreatureById(id);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        if (updatedCreature.getZone() != null) {
            creature.setZone(resolveZone(updatedCreature.getZone()));
        }
        return creatureRepository.save(creature);
    }

    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
    }

    private Zone resolveZone(Zone z) {
        if (z == null || z.getId() == null) return null;
        return zoneRepository.findById(z.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id " + z.getId()));
    }
}