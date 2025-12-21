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
import com.autumnsoftwares.agendamento.infra.security.SecurityConfigurations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
@Tag(name = "barber account controller")
public class BarberAccountController {
    
    private final BarberAccountService accountService;

    public BarberAccountController(BarberAccountService accountService){
        this.accountService = accountService;
    }

    @Operation(summary = "Criar conta de barbeiro", description = "Metodo para criar uma nova conta de acesso")
    @ApiResponse(responseCode = "201", description = "Conta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @PostMapping
    public ResponseEntity<BarberAccountResponseDTO> createBarberAccount(@RequestBody @Valid BarberAccountCreateRequestDTO requestDTO, UriComponentsBuilder uriComponentsBuilder) {
        BarberAccountResponseDTO createdAccount = accountService.createBarberAccount(requestDTO);
        URI uri = uriComponentsBuilder.path("account/{id}")
                .buildAndExpand(createdAccount.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdAccount);           
    }

    @Operation(summary = "Buscar conta por ID", description = "Metodo para buscar conta de barbeiro por ID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping("/{id}")
    public ResponseEntity<BarberAccountResponseDTO> getBarberById(@PathVariable Integer id) {
        return accountService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar conta por Email", description = "Metodo para buscar conta de barbeiro por Email")
    @ApiResponse(responseCode = "200", description = "Conta encontrada")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping("/search")
    public ResponseEntity<BarberAccountResponseDTO> getBarberByEmail(@RequestParam("email") String email) {
        return accountService.findByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletar conta por ID", description = "Metodo para deletar conta de barbeiro por ID")
    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
