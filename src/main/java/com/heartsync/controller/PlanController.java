package com.heartsync.controller;

import com.heartsync.model.Plan;
import com.heartsync.model.User;
import com.heartsync.service.PlanService;
import com.heartsync.service.ReflectionService;
import com.heartsync.service.UserService;
import com.heartsync.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final UserService userService;
    private final VenueService venueService;
    private final ReflectionService reflectionService;

    private User getCurrentUser(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @GetMapping
    public String myPlans(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        model.addAttribute("plans", planService.getPlansForUser(user));
        return "plans/list";
    }

    @GetMapping("/new")
    public String newPlanPage() {
        return "plans/new";
    }

    @PostMapping("/new")
    public String createPlan(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String date,
                             Authentication auth) {
        User user = getCurrentUser(auth);
        planService.create(title, description, LocalDate.parse(date), user);
        return "redirect:/plans";
    }

    @GetMapping("/{id}")
    public String viewPlan(@PathVariable Long id, Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        Plan plan = planService.getById(id);
        model.addAttribute("plan", plan);
        model.addAttribute("venues", venueService.getAllVenues());
        model.addAttribute("reflection", reflectionService.getByPlan(plan));
        model.addAttribute("currentUser", user);
        return "plans/detail";
    }

    @PostMapping("/{id}/partner")
    public String addPartner(@PathVariable Long id,
                             @RequestParam String partnerUsername) {
        planService.addPartner(id, partnerUsername);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/stops/add")
    public String addStop(@PathVariable Long id,
                          @RequestParam Long venueId,
                          @RequestParam String timeSlot) {
        planService.addStop(id, venueId, timeSlot);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/stops/add-custom")
    public String addCustomStop(@PathVariable Long id,
                                @RequestParam String customVenueName,
                                @RequestParam String timeSlot) {
        planService.addCustomStop(id, customVenueName, timeSlot);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/stops/{stopId}/remove")
    public String removeStop(@PathVariable Long id,
                             @PathVariable Long stopId) {
        planService.removeStop(stopId);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/complete")
    public String completePlan(@PathVariable Long id) {
        planService.completePlan(id);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/reflection")
    public String saveReflection(@PathVariable Long id,
                                 @RequestParam String notes,
                                 @RequestParam Integer rating) {
        reflectionService.saveReflection(id, notes, rating);
        return "redirect:/plans/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePlan(@PathVariable Long id) {
        planService.delete(id);
        return "redirect:/plans";
    }
}