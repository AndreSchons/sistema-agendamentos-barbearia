package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BarberAccountService {
    
    private final BarberAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public BarberAccountService(BarberAccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public BarberAccount createBarberAccount(BarberAccount barberAccount) {
        if (accountRepository.findByEmail(barberAccount.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use: " + barberAccount.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(barberAccount.getPasswordHash());
        barberAccount.setPasswordHash(hashedPassword);

        return accountRepository.save(barberAccount);
    }

    public Optional<BarberAccount> findById(Integer id) {
        return accountRepository.findById(id);
    }

    public void deleteById(Integer id) {
        BarberAccount accountToDelete = accountRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        accountRepository.delete(accountToDelete);
    }

    public void deleteByEmail(String email){
        Optional<BarberAccount> accountToDelete = this.findByEmail(email);
        accountRepository.delete(accountToDelete.get());
    }

    public Optional<BarberAccount> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
