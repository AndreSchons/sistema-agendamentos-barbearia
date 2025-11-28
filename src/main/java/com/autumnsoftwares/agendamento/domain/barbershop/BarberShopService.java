package com.autumnsoftwares.agendamento.domain.barbershop;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BarberShopService {
    private final BarberShopRepository barberShopRepository;

    public BarberShopService(BarberShopRepository barberShopRepository){
        this.barberShopRepository = barberShopRepository;
    }

    public BarberShop creatBarberShop(BarberShop barberShop) {
        return barberShopRepository.save(barberShop);
    }  

    public Optional<BarberShop> findById(Integer id) {
        return barberShopRepository.findById(id);
    }

    public void deleteById(Integer id) {
        BarberShop barberShopToDelete = barberShopRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("BarberShop not found with id: " + id));
        barberShopRepository.delete(barberShopToDelete);
    }

    public BarberShop updateById(Integer id, BarberShop barberShop) {
        BarberShop existingBarberShop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BarberShop not found with id: " + id));
        existingBarberShop.setName(barberShop.getName());
        existingBarberShop.setPhone(barberShop.getPhone());
        existingBarberShop.setAddress(barberShop.getAddress());
        return barberShopRepository.save(existingBarberShop);
    }  

    public Optional<BarberShop> findByName(String name) {
        return barberShopRepository.findByName(name);
    }
}
