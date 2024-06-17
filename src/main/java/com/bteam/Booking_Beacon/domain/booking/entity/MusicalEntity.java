package com.bteam.Booking_Beacon.domain.booking.entity;

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
@Table(name = "musical")
public class MusicalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "musical_id")
    private Long musicalId;

    @Column(nullable = false, length = 50, name = "title")
    private String title;

    @Column(nullable = false, length = 1000, name = "description")
    private String description;

    @Column(nullable = false, name = "capacity")
    private Integer capacity;

    @Column(nullable = false, name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(nullable = false, name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endDate;

    @Builder
    public MusicalEntity(String title, String description, Integer capacity, Timestamp endDate) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.endDate = endDate;
    }
}
