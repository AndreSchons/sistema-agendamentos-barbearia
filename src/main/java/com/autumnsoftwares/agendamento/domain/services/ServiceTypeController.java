package com.autumnsoftwares.agendamento.domain.services;

import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @PostMapping
    public ResponseEntity<ServiceTypeResponseDTO> createService(@RequestBody @Valid ServiceTypeCreateRequestDTO createRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceTypeService.createService(createRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponseDTO>> getAllServices() {
        return ResponseEntity.ok(serviceTypeService.getAllServices());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Integer id) {
        serviceTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
