package com.autumnsoftwares.agendamento.domain.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.autumnsoftwares.agendamento.domain.customer.Customer;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchedulingStatus status;

    @Column(nullable = false)
    private BigDecimal price;

    public Scheduling(){}

    public Scheduling(
        Barber barber,
        ServiceType serviceType,
        LocalDateTime startTime,
        Customer customer
        ){

        if(barber == null) throw new IllegalArgumentException("Barber cannot be null");
        if(serviceType == null) throw new IllegalArgumentException("ServiceType cannot be null");
        if(startTime == null) throw new IllegalArgumentException("Start time cannot be null");
        if(startTime.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Cannot schedule in the past");
        if(customer == null) throw new IllegalArgumentException("Customer cannot be null");
        this.barber = barber;
        this.service = serviceType;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(serviceType.getDurationInMinutes());
        this.price = serviceType.getPrice();
        this.customer = customer;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
