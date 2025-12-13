package com.autumnsoftwares.agendamento.domain.barber;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/barber")
public class BarberController {
    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @PostMapping
    public ResponseEntity<BarberResponseDTO> createBarber(@RequestBody @Valid BarberCreateRequestDTO barberRequest, UriComponentsBuilder uriComponentsBuilder) {
        BarberResponseDTO createdBarber = barberService.createBarber(barberRequest);
        URI uri = uriComponentsBuilder.path("barber/{id}")
                .buildAndExpand(createdBarber.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdBarber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> getBarberById(@PathVariable Integer id) {
        return barberService.getBarberById(id)
                .map(barberDTO -> ResponseEntity.ok(barberDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarberResponseDTO> updateById(@PathVariable Integer id, @RequestBody @Valid BarberUpdateRequestDTO updateDTO) {
        BarberResponseDTO responseDTO = barberService.updateById(id, updateDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        barberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/schedulings")
    public ResponseEntity<List<SchedulingResponseDTO>> getSchedulings(@RequestParam Integer barberId, @RequestParam LocalDate date){
        List<SchedulingResponseDTO> schedulings = barberService.getSchedulings(barberId, date);
        return ResponseEntity.ok(schedulings);
    }
}
