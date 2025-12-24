package com.autumnsoftwares.agendamento.domain.scheduling;

import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.BarberRepository;
import com.autumnsoftwares.agendamento.domain.customer.Customer;
import com.autumnsoftwares.agendamento.domain.customer.CustomerRepository;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceType;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceTypeRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchedulingServiceTest {

    @InjectMocks
    private SchedulingService schedulingService;

    @Mock
    private SchedulingRepository schedulingRepository;

    @Mock
    private SchedulingMapper schedulingMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Test
    @DisplayName("Should create scheduling successfully")
    void testCreateScheduling_Success() {
        SchedulingCreateRequestDTO requestDTO = new SchedulingCreateRequestDTO();
        requestDTO.setCustomerId(1);
        requestDTO.setBarberId(1);
        requestDTO.setServiceTypeId(1);
        requestDTO.setStartTime(LocalDateTime.now().plusHours(1));

        Customer customer = new Customer();
        customer.setSchedulings(new ArrayList<>());
        Barber barber = new Barber();
        ServiceType serviceType = new ServiceType();
        serviceType.setDurationInMinutes(30);
        serviceType.setPrice(BigDecimal.TEN);

        Scheduling scheduling = new Scheduling();
        SchedulingResponseDTO responseDTO = new SchedulingResponseDTO();

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(barberRepository.findById(1)).thenReturn(Optional.of(barber));
        when(serviceTypeRepository.findById(1)).thenReturn(Optional.of(serviceType));
        when(schedulingRepository.existsOverlappingSchedule(any(), any(), any())).thenReturn(false);
        when(schedulingRepository.save(any(Scheduling.class))).thenReturn(scheduling);
        when(schedulingMapper.toResponseDTO(any(Scheduling.class))).thenReturn(responseDTO);

        SchedulingResponseDTO result = schedulingService.createScheduling(requestDTO);

        assertNotNull(result);
        verify(schedulingRepository).save(any(Scheduling.class));
    }

    @Test
    @DisplayName("Should throw exception when creating scheduling with invalid customer")
    void testCreateScheduling_Failure() {
        SchedulingCreateRequestDTO requestDTO = new SchedulingCreateRequestDTO();
        requestDTO.setCustomerId(99);

        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> schedulingService.createScheduling(requestDTO));
        verify(schedulingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should cancel scheduling successfully")
    void testCancellScheduling_Success() {
        Integer id = 1;
        Scheduling scheduling = new Scheduling();
        scheduling.setStatus(SchedulingStatus.SCHEDULED);

        when(schedulingRepository.findById(id)).thenReturn(Optional.of(scheduling));

        schedulingService.cancellScheduling(id);

        assertEquals(SchedulingStatus.CANCELLED, scheduling.getStatus());
        verify(schedulingRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-existent scheduling")
    void testCancellScheduling_Failure() {
        Integer id = 99;
        when(schedulingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> schedulingService.cancellScheduling(id));
    }

    @Test
    @DisplayName("Should return scheduling by id successfully")
    void testGetSchedulingById_Success() {
        Integer id = 1;
        Scheduling scheduling = new Scheduling();
        SchedulingResponseDTO responseDTO = new SchedulingResponseDTO();

        when(schedulingRepository.findById(id)).thenReturn(Optional.of(scheduling));
        when(schedulingMapper.toResponseDTO(scheduling)).thenReturn(responseDTO);

        Optional<SchedulingResponseDTO> result = schedulingService.getSchedulingById(id);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Should return empty when scheduling not found")
    void testGetSchedulingById_Failure() {
        Integer id = 99;
        when(schedulingRepository.findById(id)).thenReturn(Optional.empty());

        Optional<SchedulingResponseDTO> result = schedulingService.getSchedulingById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update status for completed schedules successfully")
    void testUpdateStatusForCompletedSchedules_Success() {
        Scheduling scheduling = new Scheduling();
        scheduling.setStatus(SchedulingStatus.SCHEDULED);
        List<Scheduling> schedules = List.of(scheduling);

        when(schedulingRepository.findAllPastAndScheduled(any(LocalDateTime.class))).thenReturn(schedules);

        schedulingService.updateStatusForCompletedSchedules();

        assertEquals(SchedulingStatus.COMPLETED, scheduling.getStatus());
        verify(schedulingRepository).saveAll(schedules);
    }

    @Test
    @DisplayName("Should do nothing when no past schedules found")
    void testUpdateStatusForCompletedSchedules_Failure() {
        when(schedulingRepository.findAllPastAndScheduled(any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        schedulingService.updateStatusForCompletedSchedules();

        verify(schedulingRepository, never()).saveAll(any());
    }
}
