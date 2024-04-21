package com.logihub.service.impl;

import com.logihub.exception.AuthException;
import com.logihub.model.entity.TruckManager;
import com.logihub.model.enums.Role;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.response.TruckManagerDTO;
import com.logihub.repository.TruckCompanyRepository;
import com.logihub.repository.UserRepository;
import com.logihub.service.TruckManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TruckManagerServiceImpl implements TruckManagerService {

    private UserRepository userRepository;
    private TruckCompanyRepository truckCompanyRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public TruckManagerDTO registerTruckManager(RegisterRequest newTruckManager) throws AuthException {

        if (userRepository.existsByEmailIgnoreCase(newTruckManager.getEmail())) {
            throw new AuthException(AuthException.AuthExceptionProfile.EMAIL_OCCUPIED);
        }

        var company = truckCompanyRepository.findById(newTruckManager.getCompanyId()).orElseThrow(
                () -> new AuthException(AuthException.AuthExceptionProfile.EMAIL_OCCUPIED)
        );

        TruckManager truckManager = TruckManager.builder()
                .email(newTruckManager.getEmail())
                .firstName(newTruckManager.getFirstName())
                .lastName(newTruckManager.getLastName())
                .password(passwordEncoder.encode(newTruckManager.getPassword()))
                .role(Role.TRUCK_MANAGER)
                .company(company)
                .build();

        return new TruckManagerDTO(userRepository.save(truckManager));
    }
}
