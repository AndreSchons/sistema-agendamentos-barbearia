package com.autumnsoftwares.agendamento.domain.scheduling;

import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.BarberRepository;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceType;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceTypeRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailableSlotsServiceTest {

    @InjectMocks
    private AvailableSlotsService availableSlotsService;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private SchedulingRepository schedulingRepository;

    @Test
    @DisplayName("Should return available slots successfully filtering occupied ones")
    void testGetAvailableSlots_Success() {
        Integer barberId = 1;
        Integer serviceTypeId = 1;
        LocalDate date = LocalDate.now();

        BarberShop barberShop = new BarberShop();
        barberShop.setStartTime(LocalTime.of(9, 0));
        barberShop.setEndTime(LocalTime.of(12, 0));

        Barber barber = new Barber();
        barber.setId(barberId);
        barber.setBarberShop(barberShop);

        ServiceType serviceType = new ServiceType();
        serviceType.setId(serviceTypeId);
        serviceType.setDurationInMinutes(60);

        // Agendamento existente das 10:00 as 11:00
        Scheduling existingScheduling = new Scheduling();
        existingScheduling.setStartTime(LocalDateTime.of(date, LocalTime.of(10, 0)));
        existingScheduling.setEndTime(LocalDateTime.of(date, LocalTime.of(11, 0)));
        existingScheduling.setStatus(SchedulingStatus.SCHEDULED);

        when(barberRepository.findById(barberId)).thenReturn(Optional.of(barber));
        when(serviceTypeRepository.findById(serviceTypeId)).thenReturn(Optional.of(serviceType));
        when(schedulingRepository.findByBarberAndStartTimeBetween(any(Barber.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(existingScheduling));

        List<LocalTime> result = availableSlotsService.getAvailableSlots(barberId, serviceTypeId, date);

        // Esperado: 09:00 e 11:00 livres. 10:00 ocupado.
        assertEquals(2, result.size());
        assertTrue(result.contains(LocalTime.of(9, 0)));
        assertTrue(result.contains(LocalTime.of(11, 0)));
        assertFalse(result.contains(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("Should return all slots when no schedulings exist")
    void testGetAvailableSlots_AllFree() {
        Integer barberId = 1;
        Integer serviceTypeId = 1;
        LocalDate date = LocalDate.now();

        BarberShop barberShop = new BarberShop();
        barberShop.setStartTime(LocalTime.of(9, 0));
        barberShop.setEndTime(LocalTime.of(10, 0)); // Apenas 1 hora de funcionamento

        Barber barber = new Barber();
        barber.setBarberShop(barberShop);

        ServiceType serviceType = new ServiceType();
        serviceType.setDurationInMinutes(30); // Slots de 30 min

        when(barberRepository.findById(barberId)).thenReturn(Optional.of(barber));
        when(serviceTypeRepository.findById(serviceTypeId)).thenReturn(Optional.of(serviceType));
        when(schedulingRepository.findByBarberAndStartTimeBetween(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<LocalTime> result = availableSlotsService.getAvailableSlots(barberId, serviceTypeId, date);

        // Esperado: 09:00, 09:30
        assertEquals(2, result.size());
        assertTrue(result.contains(LocalTime.of(9, 0)));
        assertTrue(result.contains(LocalTime.of(9, 30)));
    }

    @Test
    @DisplayName("Should throw exception when barber not found")
    void testGetAvailableSlots_BarberNotFound() {
        Integer barberId = 99;
        when(barberRepository.findById(barberId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> availableSlotsService.getAvailableSlots(barberId, 1, LocalDate.now()));
    }
}
