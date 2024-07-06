package com.bteam.Booking_Beacon.domain.booking.entity;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name = "event_file")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Description("event 등록시 같이 올리는 파일과의 관계 테이블")
public class EventFileEntity {
    @Id
    @Column(nullable = false, name = "file_id")
    private Long fileId;

    @Column(name = "concert_id", nullable = true)
    private Long concertId;

    @Column(name = "musical_id", nullable = true)
    private Long musicalId;

    @Builder
    public EventFileEntity(Long fileId, Long concertId, Long musicalId) {
        this.fileId = fileId;
        this.concertId = concertId;
        this.musicalId = musicalId;
    }

}
