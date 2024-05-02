package com.logihub.repository;

import com.logihub.model.entity.Company;
import com.logihub.model.entity.ParkingPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Long> {

    Page<ParkingPlace> findAllByParkingManager_Company(Company company, Pageable pageable);

    Page<ParkingPlace> findAllByParkingManager_CompanyAndParkingManagerIsEmpty(Company company, Pageable pageable);
}
