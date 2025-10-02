package com.laboratorio3.zoo_fantastico.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Creature {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})  // ignora metadata de Hibernate

    private Zone zone; // se setea por id
}
