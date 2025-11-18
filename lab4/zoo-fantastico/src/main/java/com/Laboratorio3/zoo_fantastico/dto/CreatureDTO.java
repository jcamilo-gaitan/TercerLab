package com.Laboratorio3.zoo_fantastico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Esto es opcional pueden usarlo o crear los getter y setters
@NoArgsConstructor
@AllArgsConstructor
public class CreatureDTO {


    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;
    private Long zoneId;

}
