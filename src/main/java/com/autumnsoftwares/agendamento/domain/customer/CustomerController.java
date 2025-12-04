package com.autumnsoftwares.agendamento.domain.customer;

import java.net.URI;
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
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerCreateRequestDTO customerCreateRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(customerCreateRequestDTO);
        URI uri = uriComponentsBuilder.path("customer/{id}")
                .buildAndExpand(createdCustomer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdCustomer);
    }

    @GetMapping("/search")
    public ResponseEntity<CustomerResponseDTO> getCustomerByPhone(@RequestParam("phone") String phone) {
        return customerService.getCustomerByPhone(phone)
        .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Integer id, @RequestBody @Valid CustomerUpdateRequestDTO updateRequestDTO) {
        CustomerResponseDTO updatedCustomer = customerService.updateById(id, updateRequestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
}
