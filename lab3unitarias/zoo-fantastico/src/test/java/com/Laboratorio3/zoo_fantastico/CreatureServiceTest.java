package com.Laboratorio3.zoo_fantastico;
import static org.junit.jupiter.api.Assertions.*;

import com.Laboratorio3.zoo_fantastico.dto.CreatureDTO;
import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.Laboratorio3.zoo_fantastico.service.CreatureService;
import com.Laboratorio3.zoo_fantastico.entity.Creature;
import com.Laboratorio3.zoo_fantastico.service.ResourceNotFoundException;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.Laboratorio3.zoo_fantastico.service.ZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
public class CreatureServiceTest {
    @Mock
    private  CreatureRepository creatureRepository;
    @Mock
    private ZoneService zoneService;
    @InjectMocks
    private CreatureService creatureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /*private CreatureDTO creaturedto(Long id, String name, String species, double siz, int dglevel, String hlthstatus, Long idzona) {
        CreatureDTO x=new CreatureDTO();
        x.setId(id);
        x.setName(name);
        x.setSpecies(species);
        x.setSize(siz);
        x.setDangerLevel(dglevel);
        x.setHealthStatus(hlthstatus);
        x.setZoneId(idzona);
        return x;
    }*/
    @Test
    void testGetCreatureById_ShouldReturnCreature_WhenCreatureExists() {
        // Arrange
        Long creatureId = 1L;
        Creature expectedCreature = new Creature();
        expectedCreature.setId(creatureId);
        when(creatureRepository.findById(creatureId)).thenReturn(Optional.of(expectedCreature));
        // Act
        Creature actualCreature = creatureService.getCreatureById(creatureId);
        // Assert
        assertNotNull(actualCreature);
        assertEquals(expectedCreature, actualCreature);
        verify(creatureRepository, times(1)).findById(creatureId);
    }
    @Test
    void testGetCreatureById_ShouldThrowException_WhenCreatureDoesNotExist() {
        // Arrange
        Long creatureId = 2L;
        when(creatureRepository.findById(creatureId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            creatureService.getCreatureById(creatureId);
        });
        verify(creatureRepository, times(1)).findById(creatureId);
    }
    @Test
    void testCreateCreature_ShouldReturnSavedCreature() {
        Creature crt = new Creature();
        crt.setName("Fénix");
        when(creatureRepository.save(any(Creature.class))).thenReturn(crt);
        Creature savedCreature = creatureService.createCreature(crt);
        assertNotNull(savedCreature);
        assertEquals("Fénix", savedCreature.getName());
    }
    @Test
    void deleteCreature_shouldCallDeleteById_whenFound() {
        Zone zone=new Zone(3L, "Z", "d", 1);
        Creature crt= new Creature(3L,"juan","sapo",32,9,"NO CRITICAL",zone);
        when(creatureRepository.findById(3L)).thenReturn(Optional.of(crt));

        creatureService.deleteCreature(3L);

        verify(creatureRepository).delete(crt);
    }
    @Test
    void updateCreature_ShouldReturnUpdatedCreature(){
        Zone zone=new Zone(3L, "Z", "d", 1);
        Creature creature=new Creature(3L,"perro feo","perros",9,10,"Critical",zone);
        CreatureDTO updated=new CreatureDTO(null,"perro hermoso","perros",7,10,"Critical",3L);
        when(creatureRepository.findById(3L)).thenReturn(Optional.of(creature));
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);
                Creature resultado=creatureService.updateCreature(3L,updated);


        assertEquals("perro hermoso",resultado.getName());
        assertEquals(7,resultado.getSize());


    }
    @Test
    void deleteCreature_ShouldNotDeleteCreatureWhenHealthStatusIsCritical(){
        Zone zone=new Zone(3L, "Z", "d", 1);
        Creature creature=new Creature(3L,"perro feo","perros",9,10,"Critical",zone);
        when(creatureRepository.findById(3L)).thenReturn(Optional.of(creature));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> creatureService.deleteCreature(3L));
        assertEquals("Cannot delete a creature in critical health", exception.getMessage());
        verify(creatureRepository, never()).deleteById(anyLong());
        verify(creatureRepository, never()).delete(any(Creature.class));
    }
    @Test
    void getAllCreatures_shouldReturnList() {
        Zone zone=new Zone(3L, "Z", "d", 1);
        Creature creature=new Creature(3L,"perro feo","perros",9,10,"Critical",zone);
        Creature crt= new Creature(3L,"juan","sapo",32,9,"NO CRITICAL",zone);
        when(creatureRepository.findAll()).thenReturn(List.of(
                creature,crt
        ));

        List<Creature> out = creatureService.getAllCreatures();

        assertEquals(2, out.size());
        verify(creatureRepository).findAll();
    }

}

