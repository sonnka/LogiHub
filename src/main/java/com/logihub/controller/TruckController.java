package com.logihub.controller;

import com.logihub.exception.TruckException;
import com.logihub.exception.UserException;
import com.logihub.model.request.TruckRequest;
import com.logihub.model.request.UpdateTruckRequest;
import com.logihub.model.response.TruckDTO;
import com.logihub.service.TruckService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @PostMapping("/api/truck-manager/{user-id}/trucks")
    public TruckDTO createTruck(Authentication auth,
                                @PathVariable("user-id") Long userId,
                                @RequestBody @Valid TruckRequest truckRequest) throws UserException {
        return truckService.createTruck(auth.getName(), userId, truckRequest);
    }

    @PatchMapping("/api/truck-manager/{user-id}/trucks/{truck-id}")
    public TruckDTO updateTruck(Authentication auth,
                                @PathVariable("user-id") Long userId,
                                @PathVariable("truck-id") Long truckId,
                                @RequestBody @Valid UpdateTruckRequest truckRequest)
            throws UserException, TruckException {
        return truckService.updateTruck(auth.getName(), userId, truckId, truckRequest);
    }

    @DeleteMapping("/api/truck-manager/{user-id}/trucks/{truck-id}")
    public void deleteTruck(Authentication auth,
                            @PathVariable("user-id") Long userId,
                            @PathVariable("truck-id") Long truckId)
            throws UserException, TruckException {
        truckService.deleteTruck(auth.getName(), userId, truckId);
    }
}
