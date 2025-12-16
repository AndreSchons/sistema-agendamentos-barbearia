package com.autumnsoftwares.agendamento.domain.barber;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccountService;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShopRepository;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

@Service
public class BarberService {
    
    private final BarberRepository barberRepository;
    private final BarberMapper barberMapper;
    private final BarberShopRepository barberShopRepository;
    private final BarberAccountService barberAccountService;

    public BarberService(BarberRepository barberRepository, BarberMapper barberMapper, BarberShopRepository barberShopRepository, BarberAccountService barberAccountService) {
        this.barberRepository = barberRepository;
        this.barberMapper = barberMapper;
        this.barberShopRepository = barberShopRepository;
        this.barberAccountService = barberAccountService;
    }

    @Transactional
    public BarberResponseDTO createBarber(BarberCreateRequestDTO barberRequestDTO) {
        BarberAccount savedAccount = barberAccountService.createAndReturnEntity(barberRequestDTO.getEmail(), barberRequestDTO.getPassword());
        Barber barberToSave = barberMapper.toEntity(barberRequestDTO);
        barberToSave.setAccount(savedAccount);

        if (barberRequestDTO.getBarberShopId() != null) {
            BarberShop barberShop = barberShopRepository.findById(barberRequestDTO.getBarberShopId())
                    .orElseThrow(() -> new ResourceNotFoundException("BarberShop not found with id: " + barberRequestDTO.getBarberShopId()));
            barberToSave.setBarberShop(barberShop);
        }

        Barber savedBarber = barberRepository.save(barberToSave);
        return barberMapper.toResponseDTO(savedBarber);
    }

    public Optional<BarberResponseDTO> getBarberById(Integer id) {
        return barberRepository.findById(id).map(barberMapper::toResponseDTO);
    }

    @Transactional
    public BarberResponseDTO updateById(Integer id, BarberUpdateRequestDTO updateDTO) {
        Barber existingBarber = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        existingBarber.setName(updateDTO.getName());
        existingBarber.setPhone(updateDTO.getPhone());   
        Barber updatedBarber = barberRepository.save(existingBarber);
        return barberMapper.toResponseDTO(updatedBarber);
    }

    public void deleteById(Integer id) {
        Barber barberToDelete = barberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + id));
        barberRepository.delete(barberToDelete);
    }


    public void setBarberShop(Integer barberShopId, Integer barberId) {
        BarberShop existingBarberShop = barberShopRepository.findById(barberShopId)
                    .orElseThrow(() -> new ResourceNotFoundException("BarberShop not found with id: " + barberShopId));
        Barber existingBarber = barberRepository.findById(barberId)
                    .orElseThrow(() -> new ResourceNotFoundException("Barber not found with id: " + barberId));
        existingBarber.setBarberShop(existingBarberShop);
        barberRepository.save(existingBarber);
    }
}
