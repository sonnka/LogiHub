package com.logihub.controller;

import com.logihub.exception.AuthException;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.model.response.TruckManagerDTO;
import com.logihub.service.TruckManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TruckManagerController {

    private TruckManagerService truckManagerService;

    @PostMapping("/api/register/truck-manager")
    public TruckManagerDTO register(@Valid @RequestBody RegisterRequest newTruckManager)
            throws AuthException {
        return truckManagerService.registerTruckManager(newTruckManager);
    }

    @PostMapping("/api/register/truck-company")
    public CompanyDTO registerTruckCompany(@Valid @RequestBody CompanyRequest newTruckCompany)
            throws AuthException {
        return truckManagerService.registerTruckCompany(newTruckCompany);
    }
}
