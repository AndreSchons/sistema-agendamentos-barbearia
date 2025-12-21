package com.autumnsoftwares.agendamento.domain.barber;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberResponseDTO;
import com.autumnsoftwares.agendamento.domain.barber.dto.BarberUpdateRequestDTO;
import com.autumnsoftwares.agendamento.infra.security.SecurityConfigurations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/barber")
@Tag(name = "barber controller")
public class BarberController {
    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @PostMapping
    @Operation(summary = "Salva os dados do barbeiro", description = "Metodo para salvar dados do barbeiro")
    @ApiResponse(responseCode = "201", description = "Barbeiro salvo com sucesso!")
    @ApiResponse(responseCode = "403", description = "Email e phone ja em uso/ BarberShop inexistente")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<BarberResponseDTO> createBarber(@RequestBody @Valid BarberCreateRequestDTO barberRequest, UriComponentsBuilder uriComponentsBuilder) {
        BarberResponseDTO createdBarber = barberService.createBarber(barberRequest);
        URI uri = uriComponentsBuilder.path("barber/{id}")
                .buildAndExpand(createdBarber.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdBarber);
    }

    @Operation(summary = "Pegar barbeiro por ID", description = "Metodo para pegar os dados do barbeiro via ID")
    @ApiResponse(responseCode = "200", description = "Barbeiro encontrado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> getBarberById(@PathVariable Integer id) {
        return barberService.getBarberById(id)
                .map(barberDTO -> ResponseEntity.ok(barberDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar barbeiro por ID", description = "Metodo para atualizar os dados do barbeiro via ID")
    @ApiResponse(responseCode = "200", description = "Barbeiro atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @PutMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> updateById(@PathVariable Integer id, @RequestBody @Valid BarberUpdateRequestDTO updateDTO) {
        BarberResponseDTO responseDTO = barberService.updateById(id, updateDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Deletar barbeiro por ID", description = "Metodo para deletar o barbeiro via ID")
    @ApiResponse(responseCode = "200", description = "Barbeiro deletado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Barbeiro nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        barberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add barbeiro a uma barbearia", description = "Metodo para add o barbeiro a respectiva barbearia")
    @ApiResponse(responseCode = "200", description = "Barbearia add com sucesso")
    @ApiResponse(responseCode = "404", description = "Barbeiro/Barbearia nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @PostMapping("/set-barber-shop")
    public ResponseEntity<Void> setBarberShop(@RequestParam Integer barberShopId, @RequestParam Integer barberId) {
        barberService.setBarberShop(barberShopId, barberId);
        return ResponseEntity.ok().build();
    }
}
