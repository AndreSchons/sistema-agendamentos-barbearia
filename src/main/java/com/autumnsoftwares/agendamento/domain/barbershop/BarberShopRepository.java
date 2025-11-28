package com.autumnsoftwares.agendamento.domain.barbershop;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberShopRepository extends JpaRepository<BarberShop, Integer>{
    
    Optional<BarberShop> findByName(String name);
}
