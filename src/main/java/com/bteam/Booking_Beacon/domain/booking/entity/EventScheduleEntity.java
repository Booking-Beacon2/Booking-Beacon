package com.bteam.Booking_Beacon.domain.booking.entity;

import com.bteam.Booking_Beacon.global.constant.EventType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity()
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "event_schedule")
public class EventScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @Column(nullable = false, name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate; // 시작 시간

    @Column(nullable = false, name = "running_time")
    private Long runningTime; // 러닝 타임

    @Column(nullable = false, name = "capacity")
    private Integer capacity; // 수용 인원

    @Version
    private Integer version;

    @Builder
    public EventScheduleEntity(Date startDate, Long runningTime, EventEntity event, Integer capacity) {
        this.event = event;
        this.startDate = startDate;
        this.runningTime = runningTime;
        this.capacity = capacity;
    }
}
