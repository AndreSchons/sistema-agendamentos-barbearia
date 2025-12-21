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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/scheduling")
@Tag(name = "scheduling controller")
public class SchedulingController {
    
    private final SchedulingService schedulingService;
    private final AvailableSlotsService availableSlotsService;

    public SchedulingController(SchedulingService schedulingService, AvailableSlotsService availableSlotsService) {
        this.schedulingService = schedulingService;
        this.availableSlotsService = availableSlotsService;
    }

    @Operation(summary = "Agendar um serviço", description = "Metodo para criar um novo agendamento")
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente, Barbeiro ou Serviço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @PostMapping
    public ResponseEntity<SchedulingResponseDTO> createScheduling(@RequestBody @Valid SchedulingCreateRequestDTO schedulingCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        SchedulingResponseDTO createdScheduling = schedulingService.createScheduling(schedulingCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("/scheduling/{id}")
            .buildAndExpand(createdScheduling.getId())
            .toUri();
        return ResponseEntity.created(uri).body(createdScheduling);
    }

    @Operation(summary = "Cancelar agendamento", description = "Metodo para cancelar um agendamento existente")
    @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @PostMapping("/cancell")
    public ResponseEntity<Void> cancellScheduling(@RequestParam Integer schedulingId) {
        schedulingService.cancellScheduling(schedulingId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Buscar agendamento por ID", description = "Metodo para buscar detalhes de um agendamento")
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado")
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping("/{id}")
    public ResponseEntity<SchedulingResponseDTO> getSchedulingById(@PathVariable Integer id) {
        return schedulingService.getSchedulingById(id)
            .map(scheduling -> ResponseEntity.ok(scheduling))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar horários disponíveis", description = "Metodo para listar horários disponíveis para um barbeiro e serviço em uma data específica")
    @ApiResponse(responseCode = "200", description = "Horários disponíveis listados com sucesso")
    @ApiResponse(responseCode = "404", description = "Barbeiro ou Serviço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(@RequestParam Integer barberId, @RequestParam Integer serviceTypeId, @RequestParam LocalDate localDate) {
        List<LocalTime> availableSlots = availableSlotsService.getAvailableSlots(barberId, serviceTypeId, localDate);
        return ResponseEntity.ok(availableSlots);
    }
}