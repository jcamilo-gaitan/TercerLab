package com.Laboratorio3.zoo_fantastico.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data // Esto es opcional pueden usarlo o crear los getter y setters
@NoArgsConstructor
@AllArgsConstructor
public class Creature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

}