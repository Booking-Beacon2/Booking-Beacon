package com.bteam.Booking_Beacon.domain.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UploadFileRes {
    private List<Long> fileId;
}
