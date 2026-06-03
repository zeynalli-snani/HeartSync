package com.heartsync.repository;

import com.heartsync.model.Reflection;
import com.heartsync.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReflectionRepository extends JpaRepository<Reflection, Long> {
    Optional<Reflection> findByPlan(Plan plan);
}