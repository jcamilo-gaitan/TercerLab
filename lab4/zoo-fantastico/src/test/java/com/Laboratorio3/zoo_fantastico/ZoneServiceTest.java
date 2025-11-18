package com.Laboratorio3.zoo_fantastico;

import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.Laboratorio3.zoo_fantastico.repository.ZoneRepository;
import com.Laboratorio3.zoo_fantastico.service.ZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;
    @Mock
    private CreatureRepository creatureRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Zone zone(Long id, String name, String desc, Integer cap) {
        Zone z = new Zone();
        z.setId(id);
        z.setName(name);
        z.setDescription(desc);
        z.setCapacity(cap);
        return z;
    }

    // createZone
    @Test
    void createZone_shouldSaveAndReturn() {
        Zone input = zone(null, "Selva Oscura", "Húmeda y densa", 3);
        Zone saved = zone(1L, "Selva Oscura", "Húmeda y densa", 3);

        when(zoneRepository.save(input)).thenReturn(saved);

        Zone out = zoneService.createZone(input);

        assertNotNull(out);
        assertEquals(1L, out.getId());
        verify(zoneRepository).save(input);
    }

    // getAllZones
    @Test
    void getAllZones_shouldReturnList() {
        when(zoneRepository.findAll()).thenReturn(List.of(
                zone(1L, "Selva", "desc", 3),
                zone(2L, "Desierto", "desc", 2)
        ));

        List<Zone> out = zoneService.getAllZones();

        assertEquals(2, out.size());
        verify(zoneRepository).findAll();
    }

    // getZoneById (found)
    @Test
    void getZoneById_shouldReturn_whenFound() {
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone(10L, "Montaña", "fría", 5)));

        Zone out = zoneService.getZoneById(10L);

        assertEquals(10L, out.getId());
        assertEquals("Montaña", out.getName());
        verify(zoneRepository).findById(10L);
    }

    // deleteZone (ok)
    @Test
    void deleteZone_shouldCallDeleteById_whenFound() {
        when(zoneRepository.findById(3L)).thenReturn(Optional.of(zone(3L, "Z", "d", 1)));

        zoneService.deleteZone(3L);

        verify(zoneRepository).deleteById(3L);
    }

}