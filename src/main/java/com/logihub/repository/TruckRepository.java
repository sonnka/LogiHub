package com.logihub.repository;

import com.logihub.model.entity.Company;
import com.logihub.model.entity.Truck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    Page<Truck> findAllByTruckManager_Company(Company company, Pageable pageable);
}