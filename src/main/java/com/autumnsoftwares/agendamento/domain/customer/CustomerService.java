package com.autumnsoftwares.agendamento.domain.customer;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import com.autumnsoftwares.agendamento.mapper.CustomerMapper;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }


    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO) {
        Customer customerToSaved = customerMapper.toEntity(customerCreateRequestDTO);
        Customer savedCustomer = customerRepository.save(customerToSaved);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    public Optional<CustomerResponseDTO> getCustomerByPhone(String phone) {
        return customerRepository.getByPhone(phone).map(customerMapper::toResponseDTO);
    }

    @Transactional
    public void deleteById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponseDTO)
                .toList();
    }

    public Optional<CustomerResponseDTO> findById(Integer id) {
        return customerRepository.findById(id).map(customerMapper::toResponseDTO);
    }

    @Transactional
    public CustomerResponseDTO updateById(Integer id, CustomerUpdateRequestDTO updateRequestDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        existingCustomer.setName(updateRequestDTO.getName());
        existingCustomer.setPhone(updateRequestDTO.getPhone());
        return customerMapper.toResponseDTO(existingCustomer);
    }
}
