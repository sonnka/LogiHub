package com.logihub.service;

import com.logihub.exception.AuthException;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.model.response.TruckManagerDTO;

public interface TruckManagerService {

    TruckManagerDTO registerTruckManager(RegisterRequest newTruckManager) throws AuthException;

    CompanyDTO registerTruckCompany(CompanyRequest newTruckCompany) throws AuthException;
}