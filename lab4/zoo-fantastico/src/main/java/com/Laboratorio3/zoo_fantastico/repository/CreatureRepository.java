package com.Laboratorio3.zoo_fantastico.repository;
import com.Laboratorio3.zoo_fantastico.entity.Creature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreatureRepository extends JpaRepository<Creature, Long> {
    List<Creature> findByZoneId(Long zoneId);
}
