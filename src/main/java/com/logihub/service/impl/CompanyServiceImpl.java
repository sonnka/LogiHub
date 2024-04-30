package com.logihub.service.impl;

import com.logihub.exception.AuthException;
import com.logihub.model.entity.ParkingCompany;
import com.logihub.model.entity.TruckCompany;
import com.logihub.model.enums.CompanyType;
import com.logihub.model.request.CompanyRequest;
import com.logihub.model.response.CompanyDTO;
import com.logihub.repository.ParkingCompanyRepository;
import com.logihub.repository.TruckCompanyRepository;
import com.logihub.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private TruckCompanyRepository truckCompanyRepository;
    private ParkingCompanyRepository parkingCompanyRepository;

    @Override
    public CompanyDTO addTruckCompany(CompanyRequest newTruckCompany) throws AuthException {
        var company = truckCompanyRepository.findByName(newTruckCompany.getName()).orElse(null);

        if (company != null) {
            throw new AuthException(AuthException.AuthExceptionProfile.NAME_OCCUPIED);
        }

        return new CompanyDTO(
                truckCompanyRepository.save(TruckCompany.builder()
                        .name(newTruckCompany.getName())
                        .logo(newTruckCompany.getLogo())
                        .type(CompanyType.TRUCK_COMPANY)
                        .build()
                )
        );
    }

    @Override
    public CompanyDTO addParkingCompany(CompanyRequest newParkingCompany) throws AuthException {
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

    @Override
    public List<CompanyDTO> getAllCompanies(CompanyType type) {
        return switch (type) {
            case TRUCK_COMPANY -> truckCompanyRepository.findAllByOrderByNameAsc()
                    .stream().map(CompanyDTO::new).toList();
            case PARKING_COMPANY -> parkingCompanyRepository.findAllByOrderByNameAsc()
                    .stream().map(CompanyDTO::new).toList();
        };
    }

    @Override
    public List<CompanyDTO> getCompaniesByName(CompanyType type, String name) {
        return switch (type) {
            case TRUCK_COMPANY -> truckCompanyRepository.findAllByNameContainingIgnoreCase(name)
                    .stream().map(CompanyDTO::new).toList();
            case PARKING_COMPANY -> parkingCompanyRepository.findAllByNameContainingIgnoreCase(name)
                    .stream().map(CompanyDTO::new).toList();
        };
    }
}
