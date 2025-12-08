package com.autumnsoftwares.agendamento.domain.scheduling.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autumnsoftwares.agendamento.domain.scheduling.SchedulingStatus;

public class SchedulingResponseDTO {
    private Integer id;
    private String barberName;
    private String serviceTypeName;
    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SchedulingStatus status;
    private BigDecimal price;

    public SchedulingResponseDTO() {
    }

    public SchedulingResponseDTO(Integer id, String barberName, String serviceTypeName, String customerName, LocalDateTime startTime, LocalDateTime endTime, SchedulingStatus status, BigDecimal price) {
        this.id = id;
        this.barberName = barberName;
        this.serviceTypeName = serviceTypeName;
        this.customerName = customerName;
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

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getServiceName() {
        return serviceTypeName;
    }

    public void setServiceName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getcustomerName() {
        return customerName;
    }

    public void setcustomerName(String customerName) {
        this.customerName = customerName;
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
