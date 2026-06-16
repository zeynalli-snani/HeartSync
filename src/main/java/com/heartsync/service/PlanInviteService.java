package com.heartsync.service;

import com.heartsync.exception.InvalidInputException;
import com.heartsync.exception.ResourceNotFoundException;
import com.heartsync.model.*;
import com.heartsync.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanInviteService {

    private final PlanInviteRepository inviteRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    public void sendInvite(Long planId, String partnerUsername, User sender) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found."));

        if (!plan.getOwner().getId().equals(sender.getId())) {
            throw new InvalidInputException("Only the plan owner can send invites.");
        }

        if (plan.getPartner() != null) {
            throw new InvalidInputException("This plan already has a partner.");
        }

        User invitedUser = userRepository.findByUsername(partnerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with username: " + partnerUsername));

        if (invitedUser.getId().equals(sender.getId())) {
            throw new InvalidInputException("You cannot invite yourself.");
        }

        if (inviteRepository.existsByPlanAndInvitedUserAndStatus(plan, invitedUser, InviteStatus.PENDING)) {
            throw new InvalidInputException("An invite has already been sent to this user.");
        }

        PlanInvite invite = PlanInvite.builder()
                .plan(plan)
                .invitedUser(invitedUser)
                .status(InviteStatus.PENDING)
                .build();

        inviteRepository.save(invite);
    }

    public List<PlanInvite> getPendingInvitesForUser(User user) {
        return inviteRepository.findByInvitedUserAndStatus(user, InviteStatus.PENDING);
    }

    public void acceptInvite(Long inviteId, User currentUser) {
        PlanInvite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found."));

        if (!invite.getInvitedUser().getId().equals(currentUser.getId())) {
            throw new InvalidInputException("This invite is not for you.");
        }

        Plan plan = invite.getPlan();
        plan.setPartner(currentUser);
        planRepository.save(plan);

        invite.setStatus(InviteStatus.ACCEPTED);
        inviteRepository.save(invite);
    }

    public void rejectInvite(Long inviteId, User currentUser) {
        PlanInvite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found."));

        if (!invite.getInvitedUser().getId().equals(currentUser.getId())) {
            throw new InvalidInputException("This invite is not for you.");
        }

        invite.setStatus(InviteStatus.REJECTED);
        inviteRepository.save(invite);
    }

    public void leaveplan(Long planId, User currentUser) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found."));

        if (plan.getPartner() == null || !plan.getPartner().getId().equals(currentUser.getId())) {
            throw new InvalidInputException("You are not a partner in this plan.");
        }

        plan.setPartner(null);
        planRepository.save(plan);
    }
}