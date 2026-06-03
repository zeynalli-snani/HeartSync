package com.heartsync.repository;

import com.heartsync.model.Plan;
import com.heartsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByOwner(User owner);
    List<Plan> findByPartner(User partner);
}