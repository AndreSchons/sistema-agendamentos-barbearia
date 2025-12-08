package com.autumnsoftwares.agendamento.domain.scheduling;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {
    
    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
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
}