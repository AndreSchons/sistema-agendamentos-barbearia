package com.autumnsoftwares.agendamento.domain.barber;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BarberService {
    
    private final BarberRepository barberRepository;

    public BarberService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    public Barber createBarber(Barber barber) {
        return barberRepository.save(barber);
    }

    public Optional<Barber> getBarberById(Integer id) {
        return barberRepository.findById(id);
    }

    public Barber updateById(Integer id, Barber barberDetails) {
        Barber existingBarber = barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id: " + id));
        existingBarber.setName(barberDetails.getName());
        existingBarber.setPhone(barberDetails.getPhone());
        return barberRepository.save(existingBarber);
    }

    public void deleteById(Integer id) {
        Barber barberToDelete = barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber not found with id: " + id));
        barberRepository.delete(barberToDelete);
    }
}
