package com.bteam.Booking_Beacon.domain.booking.dto;

import com.bteam.Booking_Beacon.domain.booking.entity.ConcertEntity;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.global.annotation.ValidEnum;
import com.bteam.Booking_Beacon.global.constant.EventType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterEventReq {
    private String title;
    private String description;
    private Integer capacity;
    private Timestamp endDate;
    @NotBlank(message = "The eventType is required.")
    /**
     * 원시타입이 아닌 경우, 자체적으로 valid error 를 제공하지 않기 때문에 따로 만들어줘야 한다.
     */
    @ValidEnum(enumClass = EventType.class)
    private EventType eventType;

    /**
     *
     * @return 테이블에 삽입할 대상들
     * dto 에서 entity 로 전환해주는 변환기 역할
     */
    public MusicalEntity toMusicalEntity() {
        return MusicalEntity.builder()
                .title(title)
                .description(description)
                .capacity(capacity)
                .endDate(endDate)
                .build();
    }

    public ConcertEntity toConcertEntity() {
        return ConcertEntity.builder()
                .title(title)
                .description(description)
                .capacity(capacity)
                .endDate(endDate)
                .build();
    }
}
