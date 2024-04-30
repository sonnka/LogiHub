package com.logihub.controller;

import com.logihub.exception.AuthException;
import com.logihub.model.request.RegisterRequest;
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

    @PostMapping("/api/truck-manager/register")
    public TruckManagerDTO register(@Valid @RequestBody RegisterRequest newTruckManager)
            throws AuthException {
        return truckManagerService.registerTruckManager(newTruckManager);
    }
}
