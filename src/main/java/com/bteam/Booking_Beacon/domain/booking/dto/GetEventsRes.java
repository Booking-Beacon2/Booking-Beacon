package com.bteam.Booking_Beacon.domain.booking.dto;

import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.booking.entity.EventEntity;
import com.bteam.Booking_Beacon.global.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Builder
public class GetEventsRes {
    private Long id;
    private EventType eventType;
    private Long partnerId;
    private String partnerName;
    private String title;
    private String description;
    private String address;
    private String detailAddress;
    private String zipCode;
    private Date createDate;

    public static GetEventsRes of(EventEntity e) {
        return GetEventsRes.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .eventType(e.getEventType())
                .address(e.getAddress())
                .detailAddress(e.getDetailAddress())
                .zipCode(e.getZipCode())
                .createDate(e.getCreatedDate())
                .partnerId(e.getPartner().getPartnerId())
                .partnerName(e.getPartner().getPartnerName())
                .build();
    }
}
