package com.autumnsoftwares.agendamento.domain.barbershop;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopResponseDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopUpdateRequestDTO;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import com.autumnsoftwares.agendamento.mapper.BarberShopMapper;

@Service
public class BarberShopService {

    private final BarberShopRepository barberShopRepository;
    private final BarberShopMapper barberShopMapper;

    public BarberShopService(BarberShopRepository barberShopRepository, BarberShopMapper barberShopMapper){
        this.barberShopRepository = barberShopRepository;
        this.barberShopMapper = barberShopMapper;
    }

    @Transactional
    public BarberShopResponseDTO createBarberShop(BarberShopCreateRequestDTO barberShopCreateRequestDTO) {
        BarberShop barberShopToSave = barberShopMapper.toEntity(barberShopCreateRequestDTO);
        BarberShop savedBarberShop = barberShopRepository.save(barberShopToSave);
        return barberShopMapper.toResponseDTO(savedBarberShop);
    }  

    @Transactional(readOnly = true)
    public Optional<BarberShopResponseDTO> findById(Integer id) {
        return barberShopRepository.findById(id).map(barberShopMapper::toResponseDTO);
    }

    @Transactional
    public void deleteById(Integer id) {
        BarberShop barberShopToDelete = barberShopRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("BarberShop not found with id: " + id));
        barberShopRepository.delete(barberShopToDelete);
    }

    @Transactional
    public BarberShopResponseDTO updateById(Integer id, BarberShopUpdateRequestDTO barberShopUpdateRequestDTO) {
        BarberShop existingBarberShop = barberShopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BarberShop not found with id: " + id));
        existingBarberShop.setName(barberShopUpdateRequestDTO.getName());
        existingBarberShop.setPhone(barberShopUpdateRequestDTO.getPhone());
        existingBarberShop.setAddress(barberShopUpdateRequestDTO.getAddress());
        BarberShop updatedBarberShop = barberShopRepository.save(existingBarberShop);
        return barberShopMapper.toResponseDTO(updatedBarberShop);
    }  

    public Optional<BarberShopResponseDTO> findByName(String name) {
        return barberShopRepository.findByName(name).map(barberShopMapper::toResponseDTO);
    }
}
