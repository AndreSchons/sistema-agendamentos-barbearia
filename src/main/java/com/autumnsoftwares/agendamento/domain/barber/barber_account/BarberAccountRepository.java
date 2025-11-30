package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberAccountRepository extends JpaRepository<BarberAccount, Integer> {   
    Optional<BarberAccount> findByEmail(String email);
}