package com.bteam.Booking_Beacon.domain.booking.entity;

import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.global.auth.Role;
import com.bteam.Booking_Beacon.global.constant.EventType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity()
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    @Column(nullable = false, name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType; // 공연 타입 (MUSICAL, CONCERT, ...)

    @Column(nullable = false, length = 50, name = "title")
    private String title; // 공연명

    @Column(nullable = false, length = 1000, name = "description")
    private String description; // 설명

    @Column(nullable = false, length = 5, name = "zip_code")
    private String zipCode; // 우편번호

    @Column(nullable = false, length = 200, name = "address")
    private String address; // 공연장소

    @Column(nullable = false, length = 200, name = "detail_address")
    private String detailAddress; // 공연장소_상세

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate; // 생성일

    @Version
    private Integer version;

    @Builder
    public EventEntity(EventType eventType, String title, String description, String zipCode, String address, String detailAddress, PartnerEntity partner) {
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.partner = partner;
    }
}
