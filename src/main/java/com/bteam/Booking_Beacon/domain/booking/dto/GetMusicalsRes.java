package com.bteam.Booking_Beacon.domain.booking.dto;

import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GetMusicalsRes {
    private MusicalEntity musical;
    private List<Long> fileIds;
}
