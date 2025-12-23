package com.autumnsoftwares.agendamento.domain.customer;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.DataConflictException;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import com.autumnsoftwares.agendamento.domain.scheduling.SchedulingMapper;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final SchedulingMapper schedulingMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, SchedulingMapper schedulingMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.schedulingMapper = schedulingMapper;
    }


    @Transactional
    public CustomerResponseDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO) {
        if(customerRepository.getByPhone(customerCreateRequestDTO.getPhone()).isPresent()) {
            throw new DataConflictException("Phone already registered!");
        }
        Customer customerToSaved = customerMapper.toEntity(customerCreateRequestDTO);
        Customer savedCustomer = customerRepository.save(customerToSaved);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    public Optional<CustomerResponseDTO> getCustomerByPhone(String phone) {
        Optional<Customer> customer = customerRepository.getByPhone(phone);
        if(customer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with phone:" + phone);
        }
        return customer.map(c -> customerMapper.toResponseDTO(c));
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
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        return customer.map((c) -> customerMapper.toResponseDTO(c));
    }

    public List<SchedulingResponseDTO> getSchedulingsByPhone(String phone) {
        Customer customer = customerRepository.getByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone: " + phone));

        return customer.getSchedulings().stream()
                .map(schedulingMapper::toResponseDTO)
                .toList();
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
