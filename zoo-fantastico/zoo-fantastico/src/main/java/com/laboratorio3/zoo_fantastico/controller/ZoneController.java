package com.laboratorio3.zoo_fantastico.controller;

import com.laboratorio3.zoo_fantastico.entity.Zone;
import com.laboratorio3.zoo_fantastico.repository.ZoneRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {
    private final ZoneRepository repo;
    public ZoneController(ZoneRepository repo){ this.repo = repo; }

    @PostMapping public Zone create(@RequestBody Zone z){ return repo.save(z); }
    @GetMapping  public List<Zone> all(){ return repo.findAll(); }
}
