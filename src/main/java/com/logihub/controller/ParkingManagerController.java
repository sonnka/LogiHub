package com.logihub.controller;

import com.logihub.exception.AuthException;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.model.response.ParkingManagerDTO;
import com.logihub.service.ParkingManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ParkingManagerController {

    private ParkingManagerService parkingManagerService;

    @PostMapping("/api/register/parking-manager")
    public ParkingManagerDTO register(@Valid @RequestBody RegisterRequest newParkingManager)
            throws AuthException {
        return parkingManagerService.registerParkingManager(newParkingManager);
    }

    @PostMapping("/api/register/parking-company")
    public CompanyDTO registerParkingCompany(@Valid @RequestBody CompanyRequest newParkingCompany)
            throws AuthException {
        return parkingManagerService.registerParkingCompany(newParkingCompany);
    }
}
