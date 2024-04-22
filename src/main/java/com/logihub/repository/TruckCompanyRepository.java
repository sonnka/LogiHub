package com.logihub.repository;

import com.logihub.model.entity.TruckCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TruckCompanyRepository extends JpaRepository<TruckCompany, Long> {

    Optional<TruckCompany> findByName(String name);
}
