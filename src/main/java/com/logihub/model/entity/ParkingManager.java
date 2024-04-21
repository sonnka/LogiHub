package com.logihub.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logihub.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "parking_managers")
public class ParkingManager extends User {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parking_company_id")
    private ParkingCompany company;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "parkingManager")
    @JsonIgnore
    @Column(name = "places")
    private List<ParkingPlace> places;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "parkingManager")
    @JsonIgnore
    @Column(name = "invoices")
    private List<Invoice> invoices;

    public ParkingManager() {
        this.setRole(Role.PARKING_MANAGER);
    }
}
