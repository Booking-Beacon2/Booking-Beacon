package com.bteam.Booking_Beacon.domain.booking.dto;

import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterMusicalReq {
    private String title;
    private String description;
    private Integer capacity;
    private Timestamp endDate;

    public MusicalEntity toEntity() {
        return MusicalEntity.builder()
                .title(title)
                .description(description)
                .capacity(capacity)
                .endDate(endDate)
                .build();
    }
}
