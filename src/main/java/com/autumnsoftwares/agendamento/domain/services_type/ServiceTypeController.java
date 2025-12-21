package com.autumnsoftwares.agendamento.domain.services_type;

import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeResponseDTO;
import com.autumnsoftwares.agendamento.infra.security.SecurityConfigurations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/services")
@Tag(name = "service type controller")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Operation(summary = "Criar novo tipo de serviço", description = "Metodo para criar um novo tipo de serviço (Requer ADMIN)")
    @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @PostMapping
    public ResponseEntity<ServiceTypeResponseDTO> createService(@RequestBody @Valid ServiceTypeCreateRequestDTO createRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        ServiceTypeResponseDTO createdService = serviceTypeService.createService(createRequestDTO);
        URI uri = uriComponentsBuilder.path("/services/{id}")
            .buildAndExpand(createdService.getId())
            .toUri();
        return ResponseEntity.created(uri).body(createdService);
    }

    @Operation(summary = "Listar todos os tipos de serviço", description = "Metodo para listar todos os serviços disponíveis")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @GetMapping
    public ResponseEntity<List<ServiceTypeResponseDTO>> getAllServices() {
        return ResponseEntity.ok(serviceTypeService.getAllServices());
    }

    @Operation(summary = "Deletar tipo de serviço", description = "Metodo para deletar um tipo de serviço por ID")
    @ApiResponse(responseCode = "204", description = "Serviço deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @SecurityRequirement(name = SecurityConfigurations.SECURITY)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Integer id) {
        serviceTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
