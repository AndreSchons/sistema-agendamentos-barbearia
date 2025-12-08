package com.autumnsoftwares.agendamento.domain.scheduling;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.BarberRepository;
import com.autumnsoftwares.agendamento.domain.customer.Customer;
import com.autumnsoftwares.agendamento.domain.customer.CustomerRepository;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;
import com.autumnsoftwares.agendamento.domain.services.ServiceType;
import com.autumnsoftwares.agendamento.domain.services.ServiceTypeRepository;
import com.autumnsoftwares.agendamento.mapper.SchedulingMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SchedulingService {

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
        // Busca as entidades relacionadas a partir dos IDs do DTO
        Customer customer = customerRepository.findById(schedulingCreateRequestDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found!"));

        Barber barber = barberRepository.findById(schedulingCreateRequestDTO.getBarberId())
                .orElseThrow(() -> new EntityNotFoundException("Barber not found!"));
 
        ServiceType serviceType = serviceTypeRepository.findById(schedulingCreateRequestDTO.getServiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found!"));

        Scheduling scheduling = new Scheduling(barber, serviceType, schedulingCreateRequestDTO.getStartTime(), customer);

        Scheduling savedScheduling = schedulingRepository.save(scheduling);
        return schedulingMapper.toResponseDTO(savedScheduling);
    }

    public Optional<SchedulingResponseDTO> getSchedulingById(Integer id) {
        return schedulingRepository.findById(id).map(schedulingMapper::toResponseDTO);
    }

    
}
