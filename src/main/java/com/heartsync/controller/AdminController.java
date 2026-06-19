package com.heartsync.controller;

import com.heartsync.model.Venue;
import com.heartsync.model.VenueSuggestion;
import com.heartsync.service.FileUploadService;
import com.heartsync.service.UserService;
import com.heartsync.service.VenueService;
import com.heartsync.service.VenueSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final VenueService venueService;
    private final VenueSuggestionService suggestionService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("pendingCount", suggestionService.getPending().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("venueCount", venueService.getAllVenues().size());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/venues")
    public String manageVenues(Model model) {
        model.addAttribute("venues", venueService.getAllVenues());
        return "admin/venues";
    }

    @GetMapping("/venues/new")
    public String newVenuePage() {
        return "admin/venue-form";
    }

    @PostMapping("/venues/new")
    public String createVenue(@RequestParam String name,
                              @RequestParam String description,
                              @RequestParam String category,
                              @RequestParam(required = false) String photoUrl,
                              @RequestParam(required = false) MultipartFile photoFile,
                              @RequestParam Double latitude,
                              @RequestParam Double longitude) {
        venueService.saveWithImage(name, description, category,
                photoUrl, photoFile, latitude, longitude, fileUploadService);
        return "redirect:/admin/venues";
    }

    @GetMapping("/venues/{id}/edit")
    public String editVenuePage(@PathVariable Long id, Model model) {
        model.addAttribute("venue", venueService.getById(id));
        return "admin/venue-form";
    }

    @PostMapping("/venues/{id}/edit")
    public String editVenue(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String description,
                            @RequestParam String category,
                            @RequestParam(required = false) String photoUrl,
                            @RequestParam(required = false) MultipartFile photoFile,
                            @RequestParam Double latitude,
                            @RequestParam Double longitude) {
        Venue venue = venueService.getById(id);
        venue.setName(name);
        venue.setDescription(description);
        venue.setCategory(category);

        if (photoFile != null && !photoFile.isEmpty()) {
            venue.setPhotoUrl(fileUploadService.store(photoFile));
        } else if (photoUrl != null && !photoUrl.isBlank()) {
            venue.setPhotoUrl(photoUrl);
        }

        venueService.save(venue);
        return "redirect:/admin/venues";
    }

    @PostMapping("/venues/{id}/delete")
    public String deleteVenue(@PathVariable Long id) {
        venueService.delete(id);
        return "redirect:/admin/venues";
    }

    @GetMapping("/suggestions")
    public String manageSuggestions(Model model) {
        model.addAttribute("suggestions", suggestionService.getPending());
        return "admin/suggestions";
    }

    @GetMapping("/suggestions/{id}/review")
    public String reviewSuggestion(@PathVariable Long id, Model model) {
        model.addAttribute("suggestion", suggestionService.getById(id));
        return "admin/suggestion-review";
    }

    @PostMapping("/suggestions/{id}/update")
    public String updateSuggestion(@PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam String description,
                                   @RequestParam String category,
                                   @RequestParam String photoUrl,
                                   @RequestParam Double latitude,
                                   @RequestParam Double longitude) {
        suggestionService.update(id, name, description, category, photoUrl, latitude, longitude);
        return "redirect:/admin/suggestions/" + id + "/review";
    }

    @PostMapping("/suggestions/{id}/approve")
    public String approveSuggestion(@PathVariable Long id) {
        suggestionService.approve(id);
        return "redirect:/admin/suggestions";
    }

    @PostMapping("/suggestions/{id}/reject")
    public String rejectSuggestion(@PathVariable Long id,
                                   @RequestParam(required = false) String adminNote) {
        suggestionService.reject(id, adminNote);
        return "redirect:/admin/suggestions";
    }
}