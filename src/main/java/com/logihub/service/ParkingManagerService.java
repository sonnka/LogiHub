package com.logihub.service;

import com.logihub.exception.AuthException;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.ParkingManagerDTO;

public interface ParkingManagerService {

    ParkingManagerDTO registerParkingManager(RegisterRequest newParkingManager) throws AuthException;
}
