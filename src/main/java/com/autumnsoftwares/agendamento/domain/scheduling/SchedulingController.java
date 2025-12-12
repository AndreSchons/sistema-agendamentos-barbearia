package com.autumnsoftwares.agendamento.domain.scheduling;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {
    
    private final SchedulingService schedulingService;
    private final AvailableSlotsService availableSlotsService;

    public SchedulingController(SchedulingService schedulingService, AvailableSlotsService availableSlotsService) {
        this.schedulingService = schedulingService;
        this.availableSlotsService = availableSlotsService;
    }

    @PostMapping
    public ResponseEntity<SchedulingResponseDTO> createScheduling(@RequestBody @Valid SchedulingCreateRequestDTO schedulingCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        SchedulingResponseDTO createdScheduling = schedulingService.createScheduling(schedulingCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("/scheduling/{id}")
            .buildAndExpand(createdScheduling.getId())
            .toUri();
        return ResponseEntity.created(uri).body(createdScheduling);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulingResponseDTO> getSchedulingById(@PathVariable Integer id) {
        return schedulingService.getSchedulingById(id)
            .map(scheduling -> ResponseEntity.ok(scheduling))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(@RequestParam Integer barberId, @RequestParam Integer serviceTypeId, @RequestParam LocalDate localDate) {
        List<LocalTime> availableSlots = availableSlotsService.getAvailableSlots(barberId, serviceTypeId, localDate);
        return ResponseEntity.ok(availableSlots);
    }
}