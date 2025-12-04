package com.autumnsoftwares.agendamento.domain.scheduling.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import com.autumnsoftwares.agendamento.domain.scheduling.SchedulingStatus;

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

    private SchedulingStatus status;

    public SchedulingCreateRequestDTO() {
    }

    public SchedulingCreateRequestDTO(Integer barberId, Integer serviceTypeId, Integer customerId, LocalDateTime startTime, SchedulingStatus status) {
        this.barberId = barberId;
        this.serviceTypeId = serviceTypeId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.status = status;
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

    public SchedulingStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulingStatus status) {
        this.status = status;
    }
}
