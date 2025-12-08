package com.autumnsoftwares.agendamento.domain.scheduling.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class SchedulingCreateRequestDTO {

    @NotNull
    private Integer barberId;

    @NotNull
    private Integer serviceTypeId;

    @NotNull
    private Integer customerId;

    @NotNull
    @Future
    private LocalDateTime startTime;

    public SchedulingCreateRequestDTO() {
    }

    public SchedulingCreateRequestDTO(Integer barberId, Integer serviceTypeId, Integer customerId, LocalDateTime startTime) {
        this.barberId = barberId;
        this.serviceTypeId = serviceTypeId;
        this.customerId = customerId;
        this.startTime = startTime;
    }

    public Integer getBarberId() {
        return barberId;
    }

    public void setBarberId(Integer barberId) {
        this.barberId = barberId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
