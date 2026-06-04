package com.heartsync.service;

import com.heartsync.model.*;
import com.heartsync.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final PlanRepository planRepository;

    public void saveReflection(Long planId, String notes, Integer rating) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));

        Reflection reflection = reflectionRepository.findByPlan(plan)
                .orElse(Reflection.builder().plan(plan).build());

        reflection.setNotes(notes);
        reflection.setRating(rating);
        reflectionRepository.save(reflection);
    }

    public Reflection getByPlan(Plan plan) {
        return reflectionRepository.findByPlan(plan).orElse(null);
    }
}