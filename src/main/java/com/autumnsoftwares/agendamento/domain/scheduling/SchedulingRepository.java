package com.autumnsoftwares.agendamento.domain.scheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.autumnsoftwares.agendamento.domain.barber.Barber;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {

    @Query("SELECT s FROM Scheduling s WHERE s.endTime < :now AND s.status = 'SCHEDULED'")
    List<Scheduling> findAllPastAndScheduled(LocalDateTime now);

    boolean existsByStartTime(LocalDateTime startTime);

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
            FROM Scheduling s
            WHERE s.barber = :barber
            AND s.status != 'CANCELLED'
            AND s.startTime < :endTime AND s.endTime > :startTime
            """)
    boolean existsOverlappingSchedule(Barber barber, LocalDateTime startTime, LocalDateTime endTime);

    List<Scheduling> findByBarberAndStartTimeBetween(Barber barber, LocalDateTime dayStart, LocalDateTime dayEnd);

    @Query("""
            SELECT s FROM Scheduling s WHERE s.barber = :barber AND CAST(s.startTime AS date) = :localDate
            """)
    List<Scheduling> findByBarberAndDate(Barber barber, LocalDate localDate);
}
