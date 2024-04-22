package com.logihub.service;

import com.logihub.exception.AuthException;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.model.response.ParkingManagerDTO;

public interface ParkingManagerService {

    ParkingManagerDTO registerParkingManager(RegisterRequest newParkingManager) throws AuthException;

    CompanyDTO registerParkingCompany(CompanyRequest newParkingCompany) throws AuthException;
}
