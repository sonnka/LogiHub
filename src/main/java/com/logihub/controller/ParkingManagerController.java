package com.logihub.controller;

import com.logihub.exception.AuthException;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.ParkingManagerDTO;
import com.logihub.service.ParkingManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ParkingManagerController {

    private ParkingManagerService parkingManagerService;

    @PostMapping("/api/register/parking-manager")
    public ResponseEntity<ParkingManagerDTO> register(@Valid @RequestBody RegisterRequest newParkingManager)
            throws AuthException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(parkingManagerService.registerParkingManager(newParkingManager));
    }

}
