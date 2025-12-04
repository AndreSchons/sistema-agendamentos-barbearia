package com.autumnsoftwares.agendamento.domain.scheduling.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autumnsoftwares.agendamento.domain.scheduling.SchedulingStatus;

public class SchedulingResponseDTO {
    private Integer id;
    private Integer barberId;
    private Integer serviceId;
    private Integer customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SchedulingStatus status;
    private BigDecimal price;

    public SchedulingResponseDTO() {
    }

    public SchedulingResponseDTO(Integer id, Integer barberId, Integer serviceId, Integer customerId, LocalDateTime startTime, LocalDateTime endTime, SchedulingStatus status, BigDecimal price) {
        this.id = id;
        this.barberId = barberId;
        this.serviceId = serviceId;
        this.customerId = customerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBarberId() {
        return barberId;
    }

    public void setBarberId(Integer barberId) {
        this.barberId = barberId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public SchedulingStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulingStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
