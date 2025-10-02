package com.laboratorio3.zoo_fantastico.controller;
import com.laboratorio3.zoo_fantastico.entity.Creature;
import com.laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.laboratorio3.zoo_fantastico.service.CreatureService;
import com.laboratorio3.zoo_fantastico.service.ResourceNotFoundException;
import com.laboratorio3.zoo_fantastico.repository.CreatureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("/api/creatures")
public class CreatureController {
    private final CreatureService creatureService;
    @Autowired
    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }
    @PostMapping
    public ResponseEntity<Creature> createCreature(@RequestBody Creature creature) {
        Creature newCreature = creatureService.createCreature(creature);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCreature);
    }
    @GetMapping
    public List<Creature> getAllCreatures() {
        return creatureService.getAllCreatures();
    }
    @GetMapping("/{id}")
    public Creature getCreatureById(@PathVariable Long id) {
        return creatureService.getCreatureById(id);
    }
    @PutMapping("/{id}")
    public Creature updateCreature(@PathVariable Long id, @RequestBody Creature updatedCreature) {
        return creatureService.updateCreature(id, updatedCreature);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreature(@PathVariable Long id) {
        creatureService.deleteCreature(id);
        return ResponseEntity.noContent().build();
    }
}