package com.logihub.service;

import com.logihub.exception.TruckException;
import com.logihub.exception.UserException;
import com.logihub.model.request.TruckRequest;
import com.logihub.model.request.UpdateTruckRequest;
import com.logihub.model.response.TruckDTO;

public interface TruckService {

    TruckDTO createTruck(String email, Long userId, TruckRequest truckRequest) throws UserException;

    TruckDTO updateTruck(String email, Long userId, Long truckId, UpdateTruckRequest truckRequest)
            throws UserException, TruckException;

    void deleteTruck(String email, Long userId, Long truckId) throws UserException, TruckException;
}
