package com.autumnsoftwares.agendamento.domain.barbershop;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barbershop.dto.BarberShopResponseDTO;
import com.autumnsoftwares.agendamento.infra.security.SecurityConfigurations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/barber-shop")
@Tag(name = "barber shop controller")
public class BarberShopController {
    private final BarberShopService barberShopService;

    public BarberShopController(BarberShopService barberShopService) {
        this.barberShopService = barberShopService;
    }

    @Operation(summary = "Criar barbearia", description = "Metodo para criar uma nova barbearia")
    @ApiResponse(responseCode = "201", description = "Barbearia criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @PostMapping
    public ResponseEntity<BarberShopResponseDTO> createBarberShop(@RequestBody @Valid BarberShopCreateRequestDTO barberShopCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        BarberShopResponseDTO createdBarberShop = barberShopService.createBarberShop(barberShopCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("barber-shop/{id}")
                .buildAndExpand(createdBarberShop.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdBarberShop);
    }

    @Operation(summary = "Buscar barbearia por ID", description = "Metodo para buscar barbearia por ID")
    @ApiResponse(responseCode = "200", description = "Barbearia encontrada")
    @ApiResponse(responseCode = "404", description = "Barbearia não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping("/{id}")
    public ResponseEntity<BarberShopResponseDTO> getBarberShopById(@PathVariable Integer id) {
        return barberShopService.findById(id)
                .map(barberShop -> ResponseEntity.ok(barberShop))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar barbearia por nome", description = "Metodo para buscar barbearia por nome")
    @ApiResponse(responseCode = "200", description = "Barbearia encontrada")
    @ApiResponse(responseCode = "404", description = "Barbearia não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping("/name/{name}")
    public ResponseEntity<BarberShopResponseDTO> getBarberShopByName(@PathVariable String name) {
        return barberShopService.findByName(name)
                .map(barberShop -> ResponseEntity.ok(barberShop))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletar barbearia", description = "Metodo para deletar barbearia por ID")
    @ApiResponse(responseCode = "204", description = "Barbearia deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Barbearia não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        barberShopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar barbearia", description = "Metodo para atualizar dados da barbearia")
    @ApiResponse(responseCode = "200", description = "Barbearia atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Barbearia não encontrada")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @PutMapping("/{id}")
    public ResponseEntity<BarberShopResponseDTO> updateBarberShop(@PathVariable Integer id, @RequestBody @Valid BarberShopUpdateRequestDTO updateRequestDTO) {
        BarberShopResponseDTO updatedBarberShop = barberShopService.updateById(id, updateRequestDTO);
        return ResponseEntity.ok(updatedBarberShop);
    }
}