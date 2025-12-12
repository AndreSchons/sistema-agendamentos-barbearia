package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.DataConflictException;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

@Service
public class BarberAccountService {
    
    private final BarberAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final BarberAccountMapper accountMapper;

    public BarberAccountService(BarberAccountRepository accountRepository, PasswordEncoder passwordEncoder, BarberAccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
    }

    public BarberAccountResponseDTO createBarberAccount(BarberAccountCreateRequestDTO requestDTO) {        
        BarberAccount savedAccount = createAndReturnEntity(requestDTO.getEmail(), requestDTO.getPassword());
        return accountMapper.toResponseDTO(savedAccount);
    }

    // Método para uso interno, como no BarberService
    public BarberAccount createAndReturnEntity(String email, String password) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new DataConflictException("Email already in use: " + email);
        }
        BarberAccount newAccount = new BarberAccount();
        newAccount.setEmail(email);
        newAccount.setPasswordHash(passwordEncoder.encode(password));
        return accountRepository.save(newAccount);
    }


    public Optional<BarberAccountResponseDTO> findById(Integer id) {
        return accountRepository.findById(id)
                .map(accountMapper::toResponseDTO);
    }

    public void deleteById(Integer id) {
        BarberAccount accountToDelete = accountRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        accountRepository.delete(accountToDelete);
    }

    public void deleteByEmail(String email){
        BarberAccount accountToDelete = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with email: " + email));
        accountRepository.delete(accountToDelete);
    }

    public Optional<BarberAccountResponseDTO> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(accountMapper::toResponseDTO);
    }
}
