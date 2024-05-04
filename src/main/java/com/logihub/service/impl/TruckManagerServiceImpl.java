package com.logihub.service.impl;

import com.logihub.exception.AuthException;
import com.logihub.exception.UserException;
import com.logihub.model.entity.TruckManager;
import com.logihub.model.enums.Role;
import com.logihub.model.request.RegisterRequest;
import com.logihub.model.request.UpdateTruckManagerRequest;
import com.logihub.model.response.TruckManagerDTO;
import com.logihub.repository.TruckCompanyRepository;
import com.logihub.repository.TruckManagerRepository;
import com.logihub.repository.UserRepository;
import com.logihub.service.TruckManagerService;
import com.logihub.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TruckManagerServiceImpl implements TruckManagerService {

    private final UserRepository userRepository;
    private final TruckManagerRepository truckManagerRepository;
    private final TruckCompanyRepository truckCompanyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;

    @Override
    public TruckManagerDTO registerTruckManager(RegisterRequest newTruckManager) throws AuthException {
        if (userRepository.existsByEmailIgnoreCase(newTruckManager.getEmail())) {
            throw new AuthException(AuthException.AuthExceptionProfile.EMAIL_OCCUPIED);
        }

        var company = truckCompanyRepository.findById(newTruckManager.getCompanyId()).orElseThrow(
                () -> new AuthException(AuthException.AuthExceptionProfile.COMPANY_NOT_FOUND)
        );

        TruckManager truckManager = TruckManager.builder()
                .email(newTruckManager.getEmail())
                .firstName(newTruckManager.getFirstName())
                .lastName(newTruckManager.getLastName())
                .password(passwordEncoder.encode(newTruckManager.getPassword()))
                .role(Role.TRUCK_MANAGER)
                .company(company)
                .build();

        return new TruckManagerDTO(truckManagerRepository.save(truckManager));
    }

    @Override
    public TruckManagerDTO getTruckManager(String email, Long userId) throws UserException {
        var truckManager = authUtil.findTruckManagerByEmailAndId(email, userId);
        return new TruckManagerDTO(truckManager);
    }

    @Override
    public TruckManagerDTO updateTruckManager(String email, Long userId,
                                              UpdateTruckManagerRequest updatedTruckManager) throws UserException {
        var truckManager = authUtil.findTruckManagerByEmailAndId(email, userId);

        truckManager.setFirstName(updatedTruckManager.getFirstName());
        truckManager.setLastName(updatedTruckManager.getLastName());
        truckManager.setAvatar(updatedTruckManager.getAvatar());

        return new TruckManagerDTO(truckManagerRepository.save(truckManager));
    }

    @Override
    public void deleteTruckManager(String email, Long userId) throws UserException {
        authUtil.findTruckManagerByEmailAndIdAndCheckByAdmin(email, userId);

        truckManagerRepository.deleteById(userId);
    }

    @Override
    public TruckManagerDTO changeTruckManagerCompany(String email, Long userId, Long companyId)
            throws UserException, AuthException {
        var truckManager = authUtil.findTruckManagerByEmailAndId(email, userId);

        var company = truckCompanyRepository.findById(companyId).orElseThrow(
                () -> new AuthException(AuthException.AuthExceptionProfile.COMPANY_NOT_FOUND)
        );

        truckManager.setCompany(company);

        return new TruckManagerDTO(truckManagerRepository.save(truckManager));
    }
}
