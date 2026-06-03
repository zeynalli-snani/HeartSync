package com.heartsync.repository;

import com.heartsync.model.Stop;
import com.heartsync.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StopRepository extends JpaRepository<Stop, Long> {
    List<Stop> findByPlanOrderByPositionAsc(Plan plan);
}