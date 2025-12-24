package com.autumnsoftwares.agendamento.domain.services_type;

import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTypeServiceTest {

    @InjectMocks
    private ServiceTypeService serviceTypeService;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private ServiceTypeMapper serviceTypeMapper;

    @Test
    @DisplayName("Should create service successfully")
    void testCreateService_Success() {
        ServiceTypeCreateRequestDTO requestDTO = new ServiceTypeCreateRequestDTO();
        requestDTO.setName("Corte");
        requestDTO.setPrice(BigDecimal.TEN);
        requestDTO.setDurationInMinutes(30);

        ServiceType serviceType = new ServiceType();
        ServiceType savedService = new ServiceType();
        savedService.setId(1);
        ServiceTypeResponseDTO responseDTO = new ServiceTypeResponseDTO();

        when(serviceTypeMapper.toEntity(any(ServiceTypeCreateRequestDTO.class))).thenReturn(serviceType);
        when(serviceTypeRepository.save(any(ServiceType.class))).thenReturn(savedService);
        when(serviceTypeMapper.toResponseDTO(any(ServiceType.class))).thenReturn(responseDTO);

        ServiceTypeResponseDTO result = serviceTypeService.createService(requestDTO);

        assertNotNull(result);
        verify(serviceTypeRepository).save(serviceType);
    }

    @Test
    @DisplayName("Should delete service by id successfully")
    void testDeleteById_Success() {
        Integer id = 1;
        ServiceType serviceType = new ServiceType();
        when(serviceTypeRepository.findById(id)).thenReturn(Optional.of(serviceType));

        serviceTypeService.deleteById(id);

        verify(serviceTypeRepository).delete(serviceType);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent service")
    void testDeleteById_Failure() {
        Integer id = 99;
        when(serviceTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> serviceTypeService.deleteById(id));
        verify(serviceTypeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return all services successfully")
    void testGetAllServices_Success() {
        ServiceType serviceType = new ServiceType();
        ServiceTypeResponseDTO responseDTO = new ServiceTypeResponseDTO();
        
        when(serviceTypeRepository.findAll()).thenReturn(List.of(serviceType));
        when(serviceTypeMapper.toResponseDTO(serviceType)).thenReturn(responseDTO);

        List<ServiceTypeResponseDTO> result = serviceTypeService.getAllServices();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
