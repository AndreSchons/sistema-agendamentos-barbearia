package com.autumnsoftwares.agendamento.domain.barbershop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopResponseDTO;

@ExtendWith(MockitoExtension.class)
public class BarberShopServiceTest {

    @Mock
    private BarberShopRepository barberShopRepository;
    
    @Mock
    private BarberShopMapper barberShopMapper;

    @InjectMocks
    private BarberShopService barberShopService;

   
    @Test
    @DisplayName("Should create a barber shop and return a barberShopResponseDTO")
    void testCreateBarberShopSuccess() {
        BarberShopCreateRequestDTO requestDTO = new BarberShopCreateRequestDTO(
        "Rua teste",
        "teste",
        "12345678910",
        LocalTime.of(8, 0),
        LocalTime.of(18, 0));
        BarberShop barberShop = new BarberShop(requestDTO.getAddress(), requestDTO.getName(), requestDTO.getPhone(), requestDTO.getStartTime(), requestDTO.getEndTime());
        BarberShopResponseDTO expectedResponse = new BarberShopResponseDTO(1, "Rua teste", "teste", "12345678910");

        when(barberShopMapper.toEntity(requestDTO)).thenReturn(barberShop);
        when(barberShopRepository.save(barberShop)).thenReturn(barberShop);
        when(barberShopMapper.toResponseDTO(barberShop)).thenReturn(expectedResponse);

        BarberShopResponseDTO actualResponse = barberShopService.createBarberShop(requestDTO);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(barberShopRepository, times(1)).save(barberShop);
    }

    @Test
    @DisplayName("Should throw an exception when barber shop name already exists")
    void testCreateBarberShopFailure() {
        BarberShopCreateRequestDTO requestDTO = new BarberShopCreateRequestDTO(
        "Rua teste",
        "teste",
        "12345678910",
        LocalTime.of(8, 0),
        LocalTime.of(18, 0));

        // Simula que já existe uma barbearia com o mesmo nome
        when(barberShopRepository.findByName(requestDTO.getName())).thenReturn(Optional.of(new BarberShop()));

        // Verifica se uma exceção é lançada
        assertThrows(IllegalArgumentException.class, () -> {
            barberShopService.createBarberShop(requestDTO);
        });
    }
}
