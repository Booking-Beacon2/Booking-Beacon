package com.bteam.Booking_Beacon.domain.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity()
@Table(name = "partner")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "partner_id")
    private Long partnerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserEntity user;

    @Column(nullable = false, length = 100, name = "partner_name")
    private String partnerName;

    @Column(nullable = false, length = 10, name = "ein")
    @Pattern(regexp = "\\d+", message = "Must contain only numeric characters")
    private String ein;

    @Column(nullable = false, length = 11, name = "phone_number")
    @Pattern(regexp = "\\d+", message = "Must contain only numeric characters")
    private String phoneNumber;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Builder
    public PartnerEntity(String partnerName, UserEntity user, String ein, String phoneNumber) {
        this.partnerName = partnerName;
        this.user = user;
        this.ein = ein;
        this.phoneNumber = phoneNumber;
    }
}
