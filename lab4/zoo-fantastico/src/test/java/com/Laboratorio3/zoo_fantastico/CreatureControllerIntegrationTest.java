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
class CreatureControllerIntegrationTest {

    @Value("${server.port}")
    int serverPort;

    private WebTestClient webTestClient;

    @Autowired
    private CreatureRepository creatureRepo;

    @Autowired
    private ZoneRepository zoneRepo;

    private Long bosqueId;
    private Long cavernaId;

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

        // Crear y guardar zonas
        Zone bosque = zoneRepo.save(new Zone(null, "Bosque", "Zona norte", 10));
        Zone caverna = zoneRepo.save(new Zone(null, "Desierto", "Zona sur", 5));

        bosqueId = bosque.getId();
        cavernaId = caverna.getId();

        // Crear y guardar criaturas (usando el constructor completo)
        creatureRepo.save(new Creature(null, "Fénix", "Ave", 12.5, 3, "NORMAL", bosque));
        creatureRepo.save(new Creature(null, "Dragón", "Reptil", 250.0, 9, "CRITICAL", caverna));
    }

    @Test
    void listarCorrecto() {
        webTestClient.get()
                .uri(url("/api/creatures"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Creature.class)
                .hasSize(2);
    }

    @Test
    void crearCorrectamente() {
        var body = Map.of(
                "name", "Unicornio",
                "type", "Mitológica",
                "weight", 200.0,
                "dangerLevel", 5,
                "status", "NORMAL",
                "zoneId", bosqueId
        );

        webTestClient.post()
                .uri(url("/api/creatures"))
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Creature.class)
                .value(c -> {
                    assertEquals("Unicornio", c.getName());
                    assertEquals(5, c.getDangerLevel());
                });
    }

    @Test
    void actualizarCorrectamente() {
        Long id = creatureRepo.findAll().stream()
                .filter(c -> c.getName().equals("Fénix"))
                .findFirst()
                .get()
                .getId();

        var cambios = Map.of(
                "name", "Fénix Dorado",
                "type", "Ave de fuego",
                "weight", 15.0,
                "dangerLevel", 4,
                "status", "NORMAL",
                "zoneId", cavernaId
        );

        webTestClient.put()
                .uri(url("/api/creatures/" + id))
                .bodyValue(cambios)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Creature.class)
                .value(c -> {
                    assertEquals("Fénix Dorado", c.getName());
                    assertEquals(4, c.getDangerLevel());
                });
    }

    @Test
    void eliminarCorrectamente_noCritico() {
        Long id = creatureRepo.findAll().stream()
                .filter(c -> c.getName().equals("Fénix"))
                .findFirst()
                .get()
                .getId();

        webTestClient.delete()
                .uri(url("/api/creatures/" + id))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(url("/api/creatures"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Creature.class)
                .hasSize(1);
    }

    @Test
    void eliminarCritico_debeFallar_contraReglaNegocio() {
        Long idCritico = creatureRepo.findAll().stream()
                .filter(c -> c.getName().equals("Dragón"))
                .findFirst()
                .get()
                .getId();

        // Si el controlador devuelve 409 cuando no se puede eliminar una criatura crítica
        webTestClient.delete()
                .uri(url("/api/creatures/" + idCritico))
                .exchange()
                .expectStatus().isEqualTo(409);

        // Verifica que la criatura siga existiendo
        webTestClient.get()
                .uri(url("/api/creatures/" + idCritico))
                .exchange()
                .expectStatus().isOk();
    }
}