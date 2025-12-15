package com.autumnsoftwares.agendamento.infra.security;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccount;
// import com.autumnsoftwares.agendamento.domain.barber.barber_account.BarberAccountRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // @Autowired
    // private BarberAccountRepository accountRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid BarberAccount barberAccount){
        var accountPassword = new UsernamePasswordAuthenticationToken(barberAccount.getEmail(), barberAccount.getPassword());
        var auth = this.authenticationManager.authenticate(accountPassword);
        var token = tokenService.generateToken((BarberAccount) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    // @PostMapping("/register")
    // public ResponseEntity<Void> register(@RequestBody @Valid BarberAccount barberAccount) {
    //     if(this.accountRepository.findByEmail(barberAccount.getEmail()) == null) return ResponseEntity.badRequest().build();

    //     String encryptedPassword = new BCryptPasswordEncoder().encode(barberAccount.getPassword());
    //     BarberAccount newAccount = new BarberAccount(barberAccount.getEmail(),encryptedPassword, barberAccount.getRole());
    //      this.accountRepository.save(newAccount);
    //      return ResponseEntity.ok().build();
    // }
}
