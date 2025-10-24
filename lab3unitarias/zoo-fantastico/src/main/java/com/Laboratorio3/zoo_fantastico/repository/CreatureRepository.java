package com.Laboratorio3.zoo_fantastico.repository;
import com.Laboratorio3.zoo_fantastico.entity.Creature;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CreatureRepository extends JpaRepository<Creature, Long> {
}
