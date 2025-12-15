package com.autumnsoftwares.agendamento.domain.barbershop;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopCreateRequestDTO;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class BarberShopRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    BarberShopRepository barberShopRepository;

    @Test
    @DisplayName("Should get BarberShop successfully from DB")
    void testFindByNameSuccess() {
        BarberShopCreateRequestDTO data = new BarberShopCreateRequestDTO(
        "Rua teste",
        "teste",
        "12345678910",
        LocalTime.of(8, 0),
        LocalTime.of(18, 0));
        this.createBarberShop(data);
        
        Optional<BarberShop> result = this.barberShopRepository.findByName(data.getName());
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get BarberShop from DB when BarberShop not exists")
    void testFindByNameFailure() {
        BarberShopCreateRequestDTO data = new BarberShopCreateRequestDTO(
        "Rua teste",
        "teste",
        "12345678910",
        LocalTime.of(8, 0),
        LocalTime.of(18, 0));
        // this.createBarberShop(data); BARBERSHOP I'SNT CREATED
        
        Optional<BarberShop> result = this.barberShopRepository.findByName(data.getName());
        assertThat(result.isEmpty()).isTrue();
    }

    private BarberShop createBarberShop(BarberShopCreateRequestDTO data) {
        BarberShop barberShop = new BarberShop(data.getAddress(), data.getName(), data.getPhone(), data.getStartTime(), data.getEndTime());
        this.entityManager.persist(barberShop);
        return barberShop;
    }
}
