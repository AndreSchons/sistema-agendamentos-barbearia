package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/account")
public class BarberAccountController {
    
    private final BarberAccountService accountService;

    public BarberAccountController(BarberAccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<BarberAccount> createBarberAccount(@RequestBody BarberAccount barberAccount, UriComponentsBuilder uriComponentsBuilder) {
        BarberAccount createdAccount = accountService.createBarberAccount(barberAccount);
        URI uri = uriComponentsBuilder.path("account/{id}")
                .buildAndExpand(createdAccount.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdAccount);           
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberAccount> getBarberById(@PathVariable Integer id) {
        return accountService.findById(id)
            .map(account -> ResponseEntity.ok(account))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{email}")
    public ResponseEntity<BarberAccount> getBarberByEmail(@PathVariable String email) {
        return accountService.findByEmail(email)
            .map(account -> ResponseEntity.ok(account))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteByEmail(@PathVariable String email) {
        accountService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }



}
