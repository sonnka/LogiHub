package com.logihub.repository;

import com.logihub.model.entity.ParkingCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingCompanyRepository extends JpaRepository<ParkingCompany, Long> {

    Optional<ParkingCompany> findByName(String name);
}
