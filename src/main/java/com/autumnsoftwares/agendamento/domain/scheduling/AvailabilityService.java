package com.autumnsoftwares.agendamento.domain.scheduling;

import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.barber.BarberRepository;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;

import com.autumnsoftwares.agendamento.domain.services.ServiceType;
import com.autumnsoftwares.agendamento.domain.services.ServiceTypeRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    private final BarberRepository barberRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final SchedulingRepository schedulingRepository;

    public AvailabilityService(BarberRepository barberRepository, com.autumnsoftwares.agendamento.domain.services.ServiceTypeRepository serviceTypeRepository, SchedulingRepository schedulingRepository) {
        this.barberRepository = barberRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.schedulingRepository = schedulingRepository;
    }

    public List<LocalTime> findAvailableSlots(Integer barberId, Integer serviceTypeId, LocalDate date) {
        // 1. Buscar os dados necessários
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));

        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceTypeId));

        BarberShop barberShop = barber.getBarberShop();
        int serviceDuration = serviceType.getDurationInMinutes();

        // 2. Buscar agendamentos existentes para o dia
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Scheduling> existingSchedules = schedulingRepository.findAllByBarberAndStartTimeBetween(barber, startOfDay, endOfDay);

        // 3. Gerar todos os slots possíveis e filtrar
        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime potentialSlot = barberShop.getOpeningTime();

        while (!potentialSlot.isAfter(barberShop.getClosingTime().minusMinutes(serviceDuration))) {
            LocalDateTime slotStart = potentialSlot.atDate(date);
            LocalDateTime slotEnd = slotStart.plusMinutes(serviceDuration);

            boolean isOverlapping = false;
            for (Scheduling existing : existingSchedules) {
                // Lógica de sobreposição: (StartA < EndB) and (EndA > StartB)
                if (slotStart.isBefore(existing.getEndTime()) && slotEnd.isAfter(existing.getStartTime())) {
                    isOverlapping = true;
                    break;
                }
            }
            if (!isOverlapping) {
                availableSlots.add(potentialSlot);
            }
            potentialSlot = potentialSlot.plusMinutes(serviceType.getDurationInMinutes());
        }
        return availableSlots;
    }
}