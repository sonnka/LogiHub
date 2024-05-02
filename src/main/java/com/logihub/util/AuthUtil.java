package com.logihub.util;

import com.logihub.exception.UserException;
import com.logihub.model.entity.ParkingManager;
import com.logihub.model.entity.TruckManager;
import com.logihub.repository.AdminRepository;
import com.logihub.repository.ParkingManagerRepository;
import com.logihub.repository.TruckManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUtil {

    private AdminRepository adminRepository;
    private TruckManagerRepository truckManagerRepository;
    private ParkingManagerRepository parkingManagerRepository;

    public TruckManager findTruckManagerByEmailAndId(String email, Long managerId) throws UserException {
        var truckManager = truckManagerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.TRUCK_MANAGER_NOT_FOUND));

        if (!truckManager.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return truckManager;
    }

    public TruckManager findTruckManagerByEmailAndIdAndCheckByAdmin(String email, Long userId) throws UserException {
        var truckManager = truckManagerRepository.findById(userId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.TRUCK_MANAGER_NOT_FOUND));

        if (!truckManager.getEmail().equals(email) && (!adminRepository.existsByEmailIgnoreCase(email))) {
            throw new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND);
        }

        return truckManager;
    }

    public ParkingManager findParkingManagerByEmailAndId(String email, Long managerId) throws UserException {
        var parkingManger = parkingManagerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.PARKING_MANAGER_NOT_FOUND));

        if (!parkingManger.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return parkingManger;
    }

    public ParkingManager findParkingManagerByEmailAndIdAndCheckByAdmin(String email, Long userId)
            throws UserException {
        var parkingManager = parkingManagerRepository.findById(userId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.PARKING_MANAGER_NOT_FOUND));

        if (!parkingManager.getEmail().equals(email) && (!adminRepository.existsByEmailIgnoreCase(email))) {
            throw new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND);
        }

        return parkingManager;
    }
}
