package com.bteam.Booking_Beacon.domain.booking.entity;

import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.global.constant.BookingStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@DynamicUpdate
@Entity()
@Setter
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, name = "cnt")
    private Integer cnt; // 매수

    @Column(nullable = false, name = "status")
    private BookingStatusType status; // 상태 (1 예약완료, 2 취소완료_고객, 3 취소완료_파트너, 4 취소완료_관리자)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_schedule_id")
    private EventScheduleEntity eventSchedule;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Version
    private Integer version;
}

