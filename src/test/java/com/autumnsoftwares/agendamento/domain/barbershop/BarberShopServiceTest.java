package com.autumnsoftwares.agendamento.domain.barbershop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
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
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopUpdateRequestDTO;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

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

    @Test
    @DisplayName("Should find a barber shop and return the same")
    void testFindBarberShopByIdSuccess() {
        BarberShop barberShop = new BarberShop("Rua teste", "teste", "12345678910", LocalTime.of(8, 0), LocalTime.of(18, 0));
        barberShop.setId(1);
        BarberShopResponseDTO expectedResponse = new BarberShopResponseDTO(1, "Rua teste", "teste", "12345678910");

        when(barberShopRepository.findById(1)).thenReturn(Optional.of(barberShop));
        when(barberShopMapper.toResponseDTO(barberShop)).thenReturn(expectedResponse);

        Optional<BarberShopResponseDTO> result = barberShopService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedResponse);
        verify(barberShopRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return empty when barber shop not found by id")
    void testFindBarberShopByIdFailure() {
        Integer id = 1;
        when(barberShopRepository.findById(id)).thenReturn(Optional.empty());

        Optional<BarberShopResponseDTO> result = barberShopService.findById(id);

        assertThat(result).isEmpty();
        verify(barberShopRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should find a barber shop and return the same")
    void testFindBarberShopByNameSuccess() {
        BarberShop barberShop = new BarberShop("Rua teste", "teste", "12345678910", LocalTime.of(8, 0), LocalTime.of(18, 0));
        BarberShopResponseDTO expectedResponse = new BarberShopResponseDTO(1, "Rua teste", "teste", "12345678910");

        when(barberShopRepository.findByName("teste")).thenReturn(Optional.of(barberShop));
        when(barberShopMapper.toResponseDTO(barberShop)).thenReturn(expectedResponse);

        Optional<BarberShopResponseDTO> result = barberShopService.findByName("teste");

        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expectedResponse);
        verify(barberShopRepository, times(1)).findByName("teste");
    }

    @Test
    @DisplayName("Should return empty when barber shop not found by name")
    void testFindBarberShopByNameFailure() {
        String shopName = "teste";
        when(barberShopRepository.findByName(shopName)).thenReturn(Optional.empty());

        Optional<BarberShopResponseDTO> result = barberShopService.findByName(shopName);

        assertThat(result.isEmpty());
        verify(barberShopRepository, times(1)).findByName(shopName);
    }

    @Test
    @DisplayName("Should delete the barber shop")
    void testDeleteBarberShopByIdSuccess() {
        Integer id = 1;
        BarberShop barberShop = new BarberShop("Rua teste", "teste", "12345678910", LocalTime.of(8, 0), LocalTime.of(18, 0));
        barberShop.setId(id);

        when(barberShopRepository.findById(id)).thenReturn(Optional.of(barberShop));

        barberShopService.deleteById(id);

        verify(barberShopRepository, times(1)).findById(id);
        verify(barberShopRepository, times(1)).delete(barberShop);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent barber shop")
    void testDeleteBarberShopByIdFailure() {
        Integer id = 1;
        when(barberShopRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberShopService.deleteById(id));

        verify(barberShopRepository, times(1)).findById(id);
        verify(barberShopRepository, never()).delete(any());
    }
    
    @Test
    @DisplayName("Should update the barberShop and return the same")
    void testUpdateBarberShopById() {
        Integer id = 1;
        BarberShop barberShop = new BarberShop("Rua teste", "teste", "12345678910", LocalTime.of(8, 0), LocalTime.of(18, 0));
        barberShop.setId(id);
        
        BarberShopUpdateRequestDTO updateRequest = new BarberShopUpdateRequestDTO("Rua update", "update", "10987654321");
        BarberShopResponseDTO responseDTO = new BarberShopResponseDTO(id, "Rua update", "update", "10987654321");

        when(barberShopRepository.findById(id)).thenReturn(Optional.of(barberShop));
        when(barberShopRepository.save(any(BarberShop.class))).thenAnswer(i -> i.getArguments()[0]);
        when(barberShopMapper.toResponseDTO(any(BarberShop.class))).thenReturn(responseDTO);

        BarberShopResponseDTO result = barberShopService.updateById(id, updateRequest);

        assertThat(result).isEqualTo(responseDTO);
        verify(barberShopRepository, times(1)).save(barberShop);
    }
}
