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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/barber-shop")
public class BarberShopController {
    private final BarberShopService barberShopService;

    public BarberShopController(BarberShopService barberShopService) {
        this.barberShopService = barberShopService;
    }

    @PostMapping
    public ResponseEntity<BarberShopResponseDTO> createBarberShop(@RequestBody @Valid BarberShopCreateRequestDTO barberShopCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        BarberShopResponseDTO createdBarberShop = barberShopService.createBarberShop(barberShopCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("barber-shop/{id}")
                .buildAndExpand(createdBarberShop.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdBarberShop);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberShopResponseDTO> getBarberShopById(@PathVariable Integer id) {
        return barberShopService.findById(id)
                .map(barberShop -> ResponseEntity.ok(barberShop))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BarberShopResponseDTO> getBarberShopByName(@PathVariable String name) {
        return barberShopService.findByName(name)
                .map(barberShop -> ResponseEntity.ok(barberShop))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        barberShopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberShopResponseDTO> updateBarberShop(@PathVariable Integer id, @RequestBody @Valid BarberShopUpdateRequestDTO updateRequestDTO) {
        BarberShopResponseDTO updatedBarberShop = barberShopService.updateById(id, updateRequestDTO);
        return ResponseEntity.ok(updatedBarberShop);
    }
}