package com.autumnsoftwares.agendamento.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerResponseDTO;
import com.autumnsoftwares.agendamento.domain.customer.dto.CustomerUpdateRequestDTO;
import com.autumnsoftwares.agendamento.domain.scheduling.Scheduling;
import com.autumnsoftwares.agendamento.domain.scheduling.SchedulingMapper;
import com.autumnsoftwares.agendamento.domain.scheduling.dto.SchedulingResponseDTO;
import com.autumnsoftwares.agendamento.domain.services_type.ServiceType;
import com.autumnsoftwares.agendamento.infra.exception.DataConflictException;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private SchedulingMapper schedulingMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Should create and save a customer and return the same")
    void testCreateCustomerSuccess() {
        CustomerCreateRequestDTO customerRequest = new CustomerCreateRequestDTO("teste", "12345678910");
        Customer customerToSave = new Customer("teste", "12345678910");
        Customer savedCustomer = new Customer("teste", "12345678910");
        savedCustomer.setId(1); // Simula o ID gerado pelo banco
        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(1, "teste", "12345678910");

        when(customerRepository.getByPhone(customerRequest.getPhone())).thenReturn(Optional.empty());
        when(customerMapper.toEntity(customerRequest)).thenReturn(customerToSave);
        when(customerRepository.save(customerToSave)).thenReturn(savedCustomer);
        when(customerMapper.toResponseDTO(savedCustomer)).thenReturn(expectedResponse);

        CustomerResponseDTO result = customerService.createCustomer(customerRequest);

        assertThat(result).isEqualTo(expectedResponse);
        verify(customerRepository, times(1)).save(customerToSave);
    }

    @Test
    @DisplayName("Should throw a exception when the phone was used by another costumer")
    void testCreateCustomerFailure() {
        CustomerCreateRequestDTO customerRequest = new CustomerCreateRequestDTO("teste", "12345678910");

        when(customerRepository.getByPhone(customerRequest.getPhone())).thenReturn(Optional.of(new Customer()));

        assertThrows(DataConflictException.class, () -> {
            customerService.createCustomer(customerRequest);
        });
    }

    @Test
    @DisplayName("Should delete a customer if exists")
    void testDeleteByIdSuccess() {
        Integer id = 1;
        Customer customer = new Customer("teste", "12345678910");
        customer.setId(id);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerService.deleteById(id);

        verify(customerRepository,times(1)).findById(id);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    @DisplayName("Should throw an exception")
    void testDeleteByIdFailure() {
        Integer id = 1;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.deleteById(id);
        });

        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, never()).delete(any());
    }
    
    @Test
    @DisplayName("Should return a customer")
    void testFindByIdSuccess() {
        Integer id = 1;
        Customer customer = new Customer("teste", "12345678910");
        customer.setId(id);
        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(id, "teste", "12345678910");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(customer)).thenReturn(expectedResponse);

        Optional<CustomerResponseDTO> result = customerService.findById(id);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expectedResponse);
       
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw an Exception")
    void testFindByIdFailure() {
        Integer id = 1;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findById(id);
        });

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).toResponseDTO(any());
    }
    
    @Test
    @DisplayName("Should return a customer using his phone")
    void testGetCustomerByPhone() {
        Integer id = 1;
        Customer customer = new Customer("teste", "12345678910");
        customer.setId(id);
        CustomerResponseDTO expectedResult = new CustomerResponseDTO(id, "teste", "12345678910");

        when(customerRepository.getByPhone("12345678910")).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(customer)).thenReturn(expectedResult);

        Optional<CustomerResponseDTO> result = customerService.getCustomerByPhone("12345678910");

        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expectedResult);
        verify(customerRepository, times(1)).getByPhone("12345678910");
    }

    @Test
    @DisplayName("Shoud throw an Exception when the metod is called")
    void testGetCustomerByPhoneFailure() {
        String phone = "12345678910";

        when(customerRepository.getByPhone(phone)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerByPhone(phone);
        });

        verify(customerRepository, times(1)).getByPhone(phone);
        verify(customerMapper, never()).toResponseDTO(any());
    }
    

    @Test
    @DisplayName("Should return a list of all customers")
    void testFindAll() {
        List<Customer> list = new ArrayList<>();
        List<CustomerResponseDTO> expectedList = new ArrayList<>();
        Customer customer = new Customer("teste", "12345678910");
        customer.setId(1);
        CustomerResponseDTO response = new CustomerResponseDTO(1, "teste", "12345678910");
        expectedList.add(response);
        list.add(customer);

        when(customerRepository.findAll()).thenReturn(list);
        when(customerMapper.toResponseDTO(customer)).thenReturn(response);

        List<CustomerResponseDTO> result = customerService.findAll();

        assertThat(result).isEqualTo(expectedList);
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toResponseDTO(customer);
    }

    @Test
    @DisplayName("Should return an empty list when no customers are found")
    void testFindAllEmpty() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());

        List<CustomerResponseDTO> result = customerService.findAll();

        assertThat(result).isEmpty();
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Should return a list of schedulings")
    void testGetSchedulingsByPhoneSuccess() {
        String phone = "12345678910";
        Customer customer = new Customer("teste", phone);
        
        ServiceType serviceType = mock(ServiceType.class);
        when(serviceType.getDurationInMinutes()).thenReturn(30);
        when(serviceType.getPrice()).thenReturn(java.math.BigDecimal.TEN);

        Scheduling scheduling = new Scheduling(new Barber(), serviceType, customer, LocalDateTime.now());
        customer.getSchedulings().add(scheduling);

        SchedulingResponseDTO responseDTO = mock(SchedulingResponseDTO.class);

        when(customerRepository.getByPhone(phone)).thenReturn(Optional.of(customer));
        when(schedulingMapper.toResponseDTO(scheduling)).thenReturn(responseDTO);

        List<SchedulingResponseDTO> result = customerService.getSchedulingsByPhone(phone);

        assertThat(result).contains(responseDTO);
        verify(customerRepository, times(1)).getByPhone(phone);
        verify(schedulingMapper, times(1)).toResponseDTO(scheduling);
    }   

    @Test
    @DisplayName("Should throw an exception when getting schedulings by phone")
    void testGetSchedulingsByPhoneFailure() {
        String phone = "12345678910";
        
        when(customerRepository.getByPhone(phone)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getSchedulingsByPhone(phone);
        });

        verify(customerRepository, times(1)).getByPhone(phone);
        verify(schedulingMapper, never()).toResponseDTO(any());
    }
    
    @Test
    @DisplayName("Should update the Customer and return the same")
    void testUpdateByIdSuccess() {
        Integer id = 1;
        Customer customer = new Customer("teste", "12345678910");
        customer.setId(id);

        CustomerUpdateRequestDTO updateRequest = new CustomerUpdateRequestDTO("update", "9876543210");
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(id, "update", "9876543210");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);
        when(customerMapper.toResponseDTO(customer)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.updateById(id, updateRequest);

        assertThat(result).isEqualTo(responseDTO);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Should throw an exception when id is not found")
    void testUpdateByIdFailure() {
        Integer id = 2;
        CustomerUpdateRequestDTO request = new CustomerUpdateRequestDTO("teste", "12345678910");


        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateById(id, request);
        });

        verify(customerRepository, times(1)).findById(id);
        verify(customerMapper, never()).toResponseDTO(any());
    }
}
