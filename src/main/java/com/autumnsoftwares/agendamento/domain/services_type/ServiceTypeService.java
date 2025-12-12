package com.autumnsoftwares.agendamento.domain.services_type;

import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.services_type.dto.ServiceTypeResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    public ServiceTypeResponseDTO createService(ServiceTypeCreateRequestDTO createRequestDTO) {
        ServiceType serviceType = serviceTypeMapper.toEntity(createRequestDTO);
        ServiceType savedService = serviceTypeRepository.save(serviceType);
        return serviceTypeMapper.toResponseDTO(savedService);
    }

    public List<ServiceTypeResponseDTO> getAllServices() {
        return serviceTypeRepository.findAll().stream()
                .map(serviceTypeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service type not found with id: " + id));
        serviceTypeRepository.delete(serviceType);
    }
}
