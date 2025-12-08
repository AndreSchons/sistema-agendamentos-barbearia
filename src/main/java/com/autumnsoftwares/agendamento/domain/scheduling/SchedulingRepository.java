package com.autumnsoftwares.agendamento.domain.scheduling;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling, Integer> {

    @Query("SELECT s FROM Scheduling s WHERE s.endTime < :now AND s.status = 'SCHEDULED'")
    List<Scheduling> findAllPastAndScheduled(LocalDateTime now);
}
