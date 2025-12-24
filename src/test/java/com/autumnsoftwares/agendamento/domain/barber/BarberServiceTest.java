package com.autumnsoftwares.agendamento.domain.barber;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccountService;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShopRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberServiceTest {

    @InjectMocks
    private BarberService barberService;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private BarberMapper barberMapper;

    @Mock
    private BarberShopRepository barberShopRepository;

    @Mock
    private BarberAccountService barberAccountService;

    @Test
    @DisplayName("Should create barber successfully")
    void testCreateBarber_Success() {
        BarberCreateRequestDTO requestDTO = new BarberCreateRequestDTO();
        requestDTO.setEmail("barber@test.com");
        requestDTO.setPassword("123456");
        requestDTO.setBarberShopId(1);

        BarberAccount account = new BarberAccount();
        Barber barber = new Barber();
        BarberShop barberShop = new BarberShop();
        Barber savedBarber = new Barber();
        savedBarber.setId(1);
        BarberResponseDTO responseDTO = new BarberResponseDTO();

        when(barberAccountService.createAndReturnEntity(anyString(), anyString())).thenReturn(account);
        when(barberMapper.toEntity(any(BarberCreateRequestDTO.class))).thenReturn(barber);
        when(barberShopRepository.findById(1)).thenReturn(Optional.of(barberShop));
        when(barberRepository.save(any(Barber.class))).thenReturn(savedBarber);
        when(barberMapper.toResponseDTO(any(Barber.class))).thenReturn(responseDTO);

        BarberResponseDTO result = barberService.createBarber(requestDTO);

        assertNotNull(result);
        verify(barberRepository).save(barber);
    }

    @Test
    @DisplayName("Should throw exception when creating barber with invalid barber shop id")
    void testCreateBarber_Failure() {
        BarberCreateRequestDTO requestDTO = new BarberCreateRequestDTO();
        requestDTO.setEmail("barber@test.com");
        requestDTO.setPassword("123456");
        requestDTO.setBarberShopId(99);

        BarberAccount account = new BarberAccount();
        Barber barber = new Barber();

        when(barberAccountService.createAndReturnEntity(anyString(), anyString())).thenReturn(account);
        when(barberMapper.toEntity(any(BarberCreateRequestDTO.class))).thenReturn(barber);
        when(barberShopRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberService.createBarber(requestDTO));
        verify(barberRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete barber successfully")
    void testDeleteById_Success() {
        Integer id = 1;
        Barber barber = new Barber();
        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));

        barberService.deleteById(id);

        verify(barberRepository).delete(barber);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent barber")
    void testDeleteById_Failure() {
        Integer id = 99;
        when(barberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberService.deleteById(id));
        verify(barberRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return barber by id successfully")
    void testGetBarberById_Success() {
        Integer id = 1;
        Barber barber = new Barber();
        BarberResponseDTO responseDTO = new BarberResponseDTO();
        when(barberRepository.findById(id)).thenReturn(Optional.of(barber));
        when(barberMapper.toResponseDTO(barber)).thenReturn(responseDTO);

        Optional<BarberResponseDTO> result = barberService.getBarberById(id);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Should return empty when barber not found")
    void testGetBarberById_Failure() {
        Integer id = 99;
        when(barberRepository.findById(id)).thenReturn(Optional.empty());

        Optional<BarberResponseDTO> result = barberService.getBarberById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should set barber shop successfully")
    void testSetBarberShop_Success() {
        Integer barberShopId = 1;
        Integer barberId = 2;
        BarberShop barberShop = new BarberShop();
        Barber barber = new Barber();

        when(barberShopRepository.findById(barberShopId)).thenReturn(Optional.of(barberShop));
        when(barberRepository.findById(barberId)).thenReturn(Optional.of(barber));

        barberService.setBarberShop(barberShopId, barberId);

        verify(barberRepository).save(barber);
        assertEquals(barberShop, barber.getBarberShop());
    }

    @Test
    @DisplayName("Should throw exception when setting barber shop if shop not found")
    void testSetBarberShop_Failure() {
        Integer barberShopId = 99;
        Integer barberId = 2;

        when(barberShopRepository.findById(barberShopId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberService.setBarberShop(barberShopId, barberId));
        verify(barberRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update barber successfully")
    void testUpdateById_Success() {
        Integer id = 1;
        BarberUpdateRequestDTO updateDTO = new BarberUpdateRequestDTO();
        updateDTO.setName("New Name");
        updateDTO.setPhone("12345678901");

        Barber existingBarber = new Barber();
        Barber updatedBarber = new Barber();
        BarberResponseDTO responseDTO = new BarberResponseDTO();

        when(barberRepository.findById(id)).thenReturn(Optional.of(existingBarber));
        when(barberRepository.save(existingBarber)).thenReturn(updatedBarber);
        when(barberMapper.toResponseDTO(updatedBarber)).thenReturn(responseDTO);

        BarberResponseDTO result = barberService.updateById(id, updateDTO);

        assertNotNull(result);
        verify(barberRepository).save(existingBarber);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent barber")
    void testUpdateById_Failure() {
        Integer id = 99;
        BarberUpdateRequestDTO updateDTO = new BarberUpdateRequestDTO();
        when(barberRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberService.updateById(id, updateDTO));
        verify(barberRepository, never()).save(any());
    }
}
