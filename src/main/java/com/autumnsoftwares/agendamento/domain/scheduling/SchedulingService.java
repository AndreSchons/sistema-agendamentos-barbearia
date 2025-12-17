package com.autumnsoftwares.agendamento.domain.scheduling;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
import jakarta.transaction.Transactional;

@Service
public class SchedulingService {

    private static final Logger log = LoggerFactory.getLogger(SchedulingService.class);

    private final SchedulingRepository schedulingRepository;
    private final SchedulingMapper schedulingMapper;
    private final CustomerRepository customerRepository;
    private final BarberRepository barberRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public SchedulingService(
            SchedulingRepository schedulingRepository, 
            SchedulingMapper schedulingMapper, 
            CustomerRepository customerRepository, 
            BarberRepository barberRepository,
            ServiceTypeRepository serviceTypeRepository) {
        this.schedulingRepository = schedulingRepository;
        this.schedulingMapper = schedulingMapper;
        this.customerRepository = customerRepository;
        this.barberRepository = barberRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Transactional
    public SchedulingResponseDTO createScheduling(SchedulingCreateRequestDTO schedulingCreateRequestDTO) {
        Customer customer = customerRepository.findById(schedulingCreateRequestDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        Barber barber = barberRepository.findById(schedulingCreateRequestDTO.getBarberId())
                .orElseThrow(() -> new EntityNotFoundException("Barber not found!"));

        ServiceType serviceType = serviceTypeRepository.findById(schedulingCreateRequestDTO.getServiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found!"));

        boolean available = schedulingRepository.existsOverlappingSchedule(barber, schedulingCreateRequestDTO.getStartTime(), schedulingCreateRequestDTO.getStartTime().plusMinutes(serviceType.getDurationInMinutes()));
                
        if(!available){
            Scheduling scheduling = new Scheduling(barber, serviceType, customer, schedulingCreateRequestDTO.getStartTime());
            customer.getSchedulings().add(scheduling);            
            Scheduling savedScheduling = schedulingRepository.save(scheduling);
            return schedulingMapper.toResponseDTO(savedScheduling);
        }
        return null;
    }

    @Transactional
    public void cancellScheduling(Integer schedulingId) {
        Scheduling scheduling = schedulingRepository.findById(schedulingId)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduling not found with id: " + schedulingId));        
        scheduling.setStatus(SchedulingStatus.CANCELLED);
    }

    public Optional<SchedulingResponseDTO> getSchedulingById(Integer id) {
        return schedulingRepository.findById(id).map(schedulingMapper::toResponseDTO);
    }

    @Scheduled(fixedRate = 60000) // Roda a cada 60000 ms = 1 minuto
    @Transactional
    public void updateStatusForCompletedSchedules() {
        log.info("Running scheduled task to update completed schedules...");
        List<Scheduling> pastSchedules = schedulingRepository.findAllPastAndScheduled(LocalDateTime.now());

        if (pastSchedules.isEmpty()) {
            log.info("No schedules to update.");
            return;
        }

        pastSchedules.forEach(schedule -> schedule.setStatus(SchedulingStatus.COMPLETED));
        schedulingRepository.saveAll(pastSchedules);
        log.info("Updated {} schedules to COMPLETED status.", pastSchedules.size());
    }
    
}
