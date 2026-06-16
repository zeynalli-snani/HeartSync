package com.heartsync.service;

import com.heartsync.model.*;
import com.heartsync.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.heartsync.exception.InvalidInputException;
import com.heartsync.exception.ResourceNotFoundException;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final StopRepository stopRepository;
    private final VenueRepository venueRepository;

    public List<Plan> getPlansForUser(User user) {
        List<Plan> owned = planRepository.findByOwner(user);
        List<Plan> shared = planRepository.findByPartner(user);
        owned.addAll(shared);
        return owned;
    }

    public Plan getById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found."));
    }

    public Plan create(String title, String description, LocalDate date, User owner) {
        Plan plan = Plan.builder()
                .title(title)
                .description(description)
                .date(date)
                .owner(owner)
                .completed(false)
                .build();
        return planRepository.save(plan);
    }

    public void addPartner(Long planId, String partnerUsername) {
        Plan plan = getById(planId);
        User partner = userRepository.findByUsername(partnerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with username: " + partnerUsername));
        plan.setPartner(partner);
        planRepository.save(plan);
    }

    public void addStop(Long planId, Long venueId, String timeSlot) {
        if (timeSlot == null || timeSlot.isBlank()) {
            throw new InvalidInputException("Please select a time for the stop.");
        }

        Plan plan = getById(planId);
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found."));

        Stop stop = Stop.builder()
                .plan(plan)
                .venue(venue)
                .timeSlot(java.time.LocalTime.parse(timeSlot))
                .build();
        stopRepository.save(stop);
    }

    public void addCustomStop(Long planId, String customVenueName, String timeSlot) {
        if (timeSlot == null || timeSlot.isBlank()) {
            throw new InvalidInputException("Please select a time for the stop.");
        }

        if (customVenueName == null || customVenueName.isBlank()) {
            throw new InvalidInputException("Please enter a name for the custom spot.");
        }

        Plan plan = getById(planId);
        Stop stop = Stop.builder()
                .plan(plan)
                .customVenueName(customVenueName)
                .timeSlot(java.time.LocalTime.parse(timeSlot))
                .build();
        stopRepository.save(stop);
    }

    public void removeStop(Long stopId) {
        stopRepository.deleteById(stopId);
    }

    public void completePlan(Long planId) {
        Plan plan = getById(planId);
        plan.setCompleted(true);
        planRepository.save(plan);
    }

    public void delete(Long planId) {
        planRepository.deleteById(planId);
    }
}