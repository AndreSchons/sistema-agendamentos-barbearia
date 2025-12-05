package com.autumnsoftwares.agendamento.domain.services.dto;
import java.math.BigDecimal;

public class ServiceTypeResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationInMinutes;

    public ServiceTypeResponseDTO() {
    }

    public ServiceTypeResponseDTO(Integer id, String name, String description, BigDecimal price, Integer durationInMinutes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationInMinutes = durationInMinutes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
