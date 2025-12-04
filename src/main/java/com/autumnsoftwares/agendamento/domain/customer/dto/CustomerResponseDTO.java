package com.autumnsoftwares.agendamento.domain.customer.dto;

public class CustomerResponseDTO {

    private Integer id;
    private String name;
    private String phone;

    public CustomerResponseDTO(Integer id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Integer getId() {return id;}

    public String getName() {return name;}

    public String getPhone() {return phone;}

    
}
