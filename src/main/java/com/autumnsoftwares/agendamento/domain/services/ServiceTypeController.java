package com.autumnsoftwares.agendamento.domain.services;

import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services.dto.ServiceTypeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @PostMapping
    public ResponseEntity<ServiceTypeResponseDTO> createService(@RequestBody @Valid ServiceTypeCreateRequestDTO createRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        ServiceTypeResponseDTO createdService = serviceTypeService.createService(createRequestDTO);
        URI uri = uriComponentsBuilder.path("/services/{id}")
            .buildAndExpand(createdService.getId())
            .toUri();
        return ResponseEntity.created(uri).body(createdService);
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
