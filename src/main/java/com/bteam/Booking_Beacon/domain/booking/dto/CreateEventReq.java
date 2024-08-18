package com.bteam.Booking_Beacon.domain.booking.dto;


import com.bteam.Booking_Beacon.domain.auth.dto.request.CreateEventScheduleReq;
import com.bteam.Booking_Beacon.global.annotation.ValidEnum;
import com.bteam.Booking_Beacon.global.constant.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class CreateEventReq {
    @NotNull
    @Length(max = 50)
    private String title;

    @NotNull
    @Length(max = 1000)
    private String description;

    @NotNull
    @Length(max = 200)
    private String address;

    @NotNull
    @Length(max = 200)
    private String detailAddress;

    @NotNull
    @Length(min = 5, max = 5)
    private String zipCode;

    @NotNull
    @ValidEnum(enumClass = EventType.class)
    private EventType eventType;

    @NotNull
    private List<CreateEventScheduleReq> schedules;
}


