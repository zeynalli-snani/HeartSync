package com.heartsync.repository;

import com.heartsync.model.InviteStatus;
import com.heartsync.model.PlanInvite;
import com.heartsync.model.User;
import com.heartsync.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanInviteRepository extends JpaRepository<PlanInvite, Long> {
    List<PlanInvite> findByInvitedUserAndStatus(User user, InviteStatus status);
    Optional<PlanInvite> findByPlanAndInvitedUser(Plan plan, User user);
    boolean existsByPlanAndInvitedUserAndStatus(Plan plan, User user, InviteStatus status);
}