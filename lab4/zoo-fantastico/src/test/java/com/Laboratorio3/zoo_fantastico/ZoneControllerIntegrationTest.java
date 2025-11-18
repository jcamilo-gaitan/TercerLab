package com.Laboratorio3.zoo_fantastico;

import com.Laboratorio3.zoo_fantastico.entity.Creature;
import com.Laboratorio3.zoo_fantastico.entity.Zone;
import com.Laboratorio3.zoo_fantastico.repository.CreatureRepository;
import com.Laboratorio3.zoo_fantastico.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ZoneControllerIntegrationTest {

    @Value("${server.port}")
    int serverPort;

    private WebTestClient webTestClient;

    @Autowired
    private ZoneRepository zoneRepo;

    @Autowired
    private CreatureRepository creatureRepo;

    private String url(String path) {
        return "http://localhost:" + serverPort + path;
    }

    @BeforeEach
    void init() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + serverPort)
                .build();

        creatureRepo.deleteAll();
        zoneRepo.deleteAll();

        // Crear zonas de prueba
        Zone bosque = zoneRepo.save(new Zone(null, "Bosque", "Zona norte", 10));
        Zone desierto = zoneRepo.save(new Zone(null, "Desierto", "Zona sur", 5));

        // Crear criaturas asociadas a esas zonas (usando constructor completo correcto)
        creatureRepo.save(new Creature(null, "Fénix", "Ave", 12.5, 3, "NORMAL", bosque));
        creatureRepo.save(new Creature(null, "Dragón", "Reptil", 250.0, 9, "CRITICO", desierto));
    }

    @Test
    void listarCorrecto() {
        webTestClient.get()
                .uri(url("/api/zones"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Zone.class)
                .hasSize(2);
    }

    @Test
    void crearCorrectamente() {
        var body = Map.of("name", "Montaña");

        webTestClient.post()
                .uri(url("/api/zones"))
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Zone.class)
                .value(z -> assertEquals("Montaña", z.getName()));
    }

    @Test
    void actualizarCorrectamente() {
        Long id = zoneRepo.findAll().stream()
                .filter(z -> z.getName().equals("Desierto"))
                .findFirst()
                .get()
                .getId();

        var cambios = Map.of("name", "Desierto Rojo");

        webTestClient.put()
                .uri(url("/api/zones/" + id))
                .bodyValue(cambios)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Zone.class)
                .value(z -> assertEquals("Desierto Rojo", z.getName()));
    }

    @Test
    void eliminarCorrectamente_sinCriaturas() {
        // Crear una zona SIN criaturas
        Zone vacia = zoneRepo.save(new Zone(null, "Valle", "Centro", 3));

        webTestClient.delete()
                .uri(url("/api/zones/" + vacia.getId()))
                .exchange()
                .expectStatus().isEqualTo(204);

        // Siguen estando las 2 zonas iniciales (Bosque y Desierto)
        webTestClient.get()
                .uri(url("/api/zones"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Zone.class)
                .hasSize(2);
    }

    @Test
    void eliminarConCriaturas_debeFallar() {
        Zone zona = zoneRepo.save(new Zone(null, "Isla", "Tropical", 8));
        creatureRepo.save(new Creature(null, "Yeti", "Misterioso", 180.0, 6, "NORMAL", zona));

        webTestClient.delete()
                .uri(url("/api/zones/" + zona.getId()))
                .exchange()
                .expectStatus().isEqualTo(409); // en vez de 409 fijo

        // La zona debe seguir existiendo
        webTestClient.get()
                .uri(url("/api/zones/" + zona.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}
