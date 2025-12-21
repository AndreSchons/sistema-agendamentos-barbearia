package com.autumnsoftwares.agendamento.domain.customer;

import java.net.URI;
import java.util.List;

import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
@Tag(name = "customer controller")
public class CustomerController {
    
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Salva os dados de um cliente", description = "Metodo para salvar dados do cliente")
    @ApiResponse(responseCode = "201", description = "Cliente salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados invalidos")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerCreateRequestDTO customerCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customerCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("customer/{id}")
                .buildAndExpand(createdCustomer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdCustomer);
    }

    @Operation(summary = "Pega um cliente pelo numero de telefone", description = "Metodo para pegar cliente por telefone")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping("/search")
    public ResponseEntity<CustomerResponseDTO> getCustomerByPhone(@RequestParam("phone") String phone) {
        return customerService.getCustomerByPhone(phone)
        .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deletar cliente por ID", description = "Metodo para deletar o cliente via ID")
    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os clientes", description = "Metodo para listar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Pegar cliente por ID", description = "Metodo para pegar os dados do cliente via ID")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar cliente por ID", description = "Metodo para atualizar os dados do cliente via ID")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomerUpdateRequestDTO updateRequestDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateById(id, updateRequestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Listar agendamentos do cliente", description = "Metodo para listar agendamentos buscando pelo telefone do cliente")
    @ApiResponse(responseCode = "200", description = "Agendamentos encontrados")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    @GetMapping("/search/schedulings")
    public ResponseEntity<List<SchedulingResponseDTO>> getSchedulingsByPhone(@RequestParam("phone") String phone) {
        List<SchedulingResponseDTO> schedulings = customerService.getSchedulingsByPhone(phone);
        return ResponseEntity.ok(schedulings);

    }
}
