package com.bteam.Booking_Beacon.domain.booking.repository;

import com.bteam.Booking_Beacon.domain.booking.entity.EventFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventFileRepository extends JpaRepository<EventFileEntity, Long> {

    // save concert file
    @Modifying // select 이 아닌 insert 할 경우 필요
    @Query(nativeQuery = true, value = "insert into event_file(file_id, concert_id)values (:fileId, :concertId);")
    void saveConcertFile(@Param("fileId") Long fileId, @Param("concertId") Long concertId);

    // save musical file
    @Modifying // select 이 아닌 insert 할 경우 필요
    @Query(nativeQuery = true, value = "insert into event_file(file_id, musical_id)values (:fileId, :musicalId);")
    void saveMusicalFile(@Param("fileId") Long fileId, @Param("musicalId") Long musicalId);

    @Query(nativeQuery = true, value = "select * from event_file where file_id = :fileId and concert_id is not null;")
    EventFileEntity findConcertFile(@Param("fileId") Long fileId);

    @Query(nativeQuery = true, value = "select * from event_file where file_id = :fileId and musical_id is not null")
    EventFileEntity findMusicalFile(@Param("fileId") Long fileId);

    @Query(nativeQuery = true, value = "select file_id from event_file where musical_id = :musicalId")
    List<Long> findFileIdsByMusicalId(@Param("musicalId") Long musicalId);

}
