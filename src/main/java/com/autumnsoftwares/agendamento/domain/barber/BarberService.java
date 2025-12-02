package com.autumnsoftwares.agendamento.domain.barber;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccountService;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShop;
import com.autumnsoftwares.agendamento.domain.barbershop.BarberShopRepository;
import com.autumnsoftwares.agendamento.mapper.BarberMapper;

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
        // 1. Busca a BarberShop de forma segura, tratando o caso de não encontrar.
        BarberShop barberShop = barberShopRepository.findById(barberRequestDTO.getBarberShopId())
                .orElseThrow(() -> new RuntimeException("BarberShop not found with id: " + barberRequestDTO.getBarberShopId()));

        // 2. Delega a criação da conta para o BarberAccountService.
        // Ele já valida o email e criptografa a senha.
        BarberAccount account = new BarberAccount(barberRequestDTO.getEmail(), barberRequestDTO.getPassword());
        BarberAccount savedAccount = barberAccountService.createBarberAccount(account);

        // 3. Usa o mapper para converter os campos simples (name, phone).
        Barber barberToSave = barberMapper.toEntity(barberRequestDTO);
        barberToSave.setAccount(savedAccount);
        barberToSave.setBarberShop(barberShop);

        // 4. Salva a entidade Barber completa e retorna o DTO.
        Barber savedBarber = barberRepository.save(barberToSave);
        return barberMapper.toResponseDTO(savedBarber);
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
