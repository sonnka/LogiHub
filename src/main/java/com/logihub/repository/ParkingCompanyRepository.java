package com.logihub.repository;

import com.logihub.model.entity.ParkingCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingCompanyRepository extends JpaRepository<ParkingCompany, Long> {
}
