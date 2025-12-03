package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
public class BarberAccountController {
    
    private final BarberAccountService accountService;

    public BarberAccountController(BarberAccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<BarberAccountResponseDTO> createBarberAccount(@RequestBody @Valid BarberAccountCreateRequestDTO requestDTO, UriComponentsBuilder uriComponentsBuilder) {
        BarberAccountResponseDTO createdAccount = accountService.createBarberAccount(requestDTO);
        URI uri = uriComponentsBuilder.path("account/{id}")
                .buildAndExpand(createdAccount.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdAccount);           
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberAccountResponseDTO> getBarberById(@PathVariable Integer id) {
        return accountService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<BarberAccountResponseDTO> getBarberByEmail(@RequestParam("email") String email) {
        return accountService.findByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
