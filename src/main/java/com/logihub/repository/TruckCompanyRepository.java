package com.logihub.repository;

import com.logihub.model.entity.TruckCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckCompanyRepository extends JpaRepository<TruckCompany, Long> {
}
