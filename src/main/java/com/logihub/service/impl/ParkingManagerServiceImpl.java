package com.logihub.service.impl;

import com.logihub.exception.AuthException;
import com.logihub.model.entity.ParkingCompany;
import com.logihub.model.entity.ParkingManager;
import com.logihub.model.enums.CompanyType;
import com.logihub.model.enums.Role;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.model.response.ParkingManagerDTO;
import com.logihub.repository.ParkingCompanyRepository;
import com.logihub.repository.UserRepository;
import com.logihub.service.ParkingManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParkingManagerServiceImpl implements ParkingManagerService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ParkingCompanyRepository parkingCompanyRepository;

    @Override
    public ParkingManagerDTO registerParkingManager(RegisterRequest newParkingManager) throws AuthException {

        if (userRepository.existsByEmailIgnoreCase(newParkingManager.getEmail())) {
            throw new AuthException(AuthException.AuthExceptionProfile.EMAIL_OCCUPIED);
        }

        var company = parkingCompanyRepository.findById(newParkingManager.getCompanyId()).orElseThrow(
                () -> new AuthException(AuthException.AuthExceptionProfile.COMPANY_NOT_FOUND)
        );

        ParkingManager parkingManager = ParkingManager.builder()
                .email(newParkingManager.getEmail())
                .firstName(newParkingManager.getFirstName())
                .lastName(newParkingManager.getLastName())
                .password(passwordEncoder.encode(newParkingManager.getPassword()))
                .role(Role.TRUCK_MANAGER)
                .company(company)
                .build();

        return new ParkingManagerDTO(userRepository.save(parkingManager));
    }

    @Override
    public CompanyDTO registerParkingCompany(CompanyRequest newParkingCompany) throws AuthException {
        var company = parkingCompanyRepository.findByName(newParkingCompany.getName()).orElse(null);

        if (company != null) {
            throw new AuthException(AuthException.AuthExceptionProfile.NAME_OCCUPIED);
        }

        return new CompanyDTO(
                parkingCompanyRepository.save(ParkingCompany.builder()
                        .name(newParkingCompany.getName())
                        .logo(newParkingCompany.getLogo())
                        .type(CompanyType.PARKING_COMPANY)
                        .build()
                )
        );
    }
}
