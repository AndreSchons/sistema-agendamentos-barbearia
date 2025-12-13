package com.autumnsoftwares.agendamento.domain.scheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.BarberRepository;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceType;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceTypeRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

@Service
public class AvailableSlotsService {

    private final BarberRepository barberRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final SchedulingRepository schedulingRepository;

    public AvailableSlotsService(
            BarberRepository barberRepository, 
            ServiceTypeRepository serviceTypeRepository,
            SchedulingRepository schedulingRepository) {
        this.barberRepository = barberRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.schedulingRepository = schedulingRepository;
    }


    public List<LocalTime> getAvailableSlots(Integer barberId, Integer serviceTypeId, LocalDate date){
        // 1. Buscar as entidades necessárias do banco de dados
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Type not found with id: " + serviceTypeId));

        BarberShop barberShop = barber.getBarberShop();

        // 2. Obter horários da barbearia e duração do serviço
        LocalTime openingTime = barberShop.getStartTime();
        LocalTime closingTime = barberShop.getEndTime();
        int serviceDuration = serviceType.getDurationInMinutes();

        // 3. Buscar agendamentos existentes para o barbeiro no dia especificado
        LocalDateTime dayStart = date.atStartOfDay(); // Dia as 00:00:00
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay(); // Acaba no outro dia as 00:00:00
        List<Scheduling> existingSchedules = schedulingRepository.findByBarberAndStartTimeBetween(barber, dayStart, dayEnd);

        // 4. Gerar e filtrar os horários disponíveis
        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime potentialSlot = openingTime;

        while (potentialSlot.plusMinutes(serviceDuration).isBefore(closingTime) || potentialSlot.plusMinutes(serviceDuration).equals(closingTime)) {
            LocalDateTime slotStart = date.atTime(potentialSlot);
            LocalDateTime slotEnd = slotStart.plusMinutes(serviceDuration);

            boolean isOverlapping = existingSchedules.stream()
                    .anyMatch(existing -> existing.getStartTime().isBefore(slotEnd) && existing.getEndTime().isAfter(slotStart));

            if (!isOverlapping) {
                availableSlots.add(potentialSlot);
            }
            
            potentialSlot = potentialSlot.plusMinutes(serviceType.getDurationInMinutes()); // Avança para o próximo slot (ex: de 15 em 15 minutos)
        }
        return availableSlots;
    }
}