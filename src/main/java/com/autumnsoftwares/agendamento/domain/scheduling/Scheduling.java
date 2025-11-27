package com.autumnsoftwares.agendamento.domain.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import com.autumnsoftwares.agendamento.domain.services.ServiceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "schedules")
public class Scheduling {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType service;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SchedulingStatus status;

    @NotBlank
    @Column(nullable = false)
    private String customerName;

    @NotBlank
    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private BigDecimal price;

    public Scheduling(){}

    public Scheduling(
        Barber barber,
        ServiceType serviceType,
        LocalDateTime startTime,
        String customerName,
        String customerPhone){

        if(barber == null) throw new IllegalArgumentException("Barber cannot be null");
        if(serviceType == null) throw new IllegalArgumentException("ServiceType cannot be null");
        if(startTime == null || endTime == null) throw new IllegalArgumentException("Start time and end time cannot be null");
        if(startTime.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Cannot schedule in the past");
        if(customerName == null || customerName.isBlank()) throw new IllegalArgumentException("Customer name cannot be blank");
        if(customerPhone == null || customerPhone.isBlank()) throw new IllegalArgumentException("Customer phone cannot be blank");
        this.barber = barber;
        this.service = serviceType;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(serviceType.getDurationInMinutes());
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.price = serviceType.getPrice();
        this.status = SchedulingStatus.SCHEDULED;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public ServiceType getService() {
        return service;
    }

    public void setService(ServiceType service) {
        this.service = service;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
