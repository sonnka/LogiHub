package com.logihub.controller;

import com.logihub.exception.AuthException;
import com.logihub.model.enums.CompanyType;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/api/truck-company/register")
    public CompanyDTO registerTruckCompany(@Valid @RequestBody CompanyRequest newTruckCompany)
            throws AuthException {
        return companyService.addTruckCompany(newTruckCompany);
    }

    @PostMapping("/api/parking-company/register")
    public CompanyDTO registerParkingCompany(@Valid @RequestBody CompanyRequest newParkingCompany)
            throws AuthException {
        return companyService.addParkingCompany(newParkingCompany);
    }

    @GetMapping("/api/truck-company/search")
    public List<CompanyDTO> getAllTruckCompanies() {
        return companyService.getAllCompanies(CompanyType.TRUCK_COMPANY);
    }

    @GetMapping(value = "/api/truck-company/search", params = "name")
    public List<CompanyDTO> getAllTruckCompaniesByName(@RequestParam("name") String name) {
        return companyService.getCompaniesByName(CompanyType.TRUCK_COMPANY, name);
    }

    @GetMapping("/api/parking-company/search")
    public List<CompanyDTO> getAllParkingCompanies() {
        return companyService.getAllCompanies(CompanyType.PARKING_COMPANY);
    }

    @GetMapping(value = "/api/parking-company/search", params = "name")
    public List<CompanyDTO> getAllParkingCompaniesByName(@RequestParam("name") String name) {
        return companyService.getCompaniesByName(CompanyType.PARKING_COMPANY, name);
    }
}
