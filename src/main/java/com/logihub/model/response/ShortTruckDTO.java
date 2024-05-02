package com.logihub.model.response;

import com.logihub.model.entity.Truck;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortTruckDTO {

    private Long id;

    private String number;

    private String truckManagerEmail;

    public ShortTruckDTO(Truck truck) {
        this.id = truck.getId();
        this.number = truck.getNumber();
        this.truckManagerEmail = truck.getTruckManager().getEmail();
    }
}
