package com.autumnsoftwares.agendamento.infra.security;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private BarberAccountRepository barberAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura primeiro no repositório de barbeiros
        UserDetails user = barberAccountRepository.findByEmail(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }
}