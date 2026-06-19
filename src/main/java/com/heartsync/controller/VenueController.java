package com.heartsync.controller;

import com.heartsync.model.User;
import com.heartsync.model.Venue;
import com.heartsync.model.VenueCategory;
import com.heartsync.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;
    private final VenueSuggestionService suggestionService;
    private final WishlistService wishlistService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    private User getCurrentUser(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @GetMapping("/map")
    public String mapPage(Model model) throws Exception {
        List<Venue> venues = venueService.getAllVenues();

        ObjectMapper mapper = new ObjectMapper();
        String venuesJson = venues.isEmpty() ? "[]" : mapper.writeValueAsString(
                venues.stream().map(v -> {
                    String categoryLabel = v.getCategory() != null ? v.getCategory().getLabel() : "Other";
                    String categoryColor = v.getCategory() != null ? v.getCategory().getColor() : "#6b7280";
                    return Map.of(
                            "id", v.getId(),
                            "name", v.getName() != null ? v.getName() : "",
                            "category", categoryLabel,
                            "color", categoryColor,
                            "description", v.getDescription() != null ? v.getDescription() : "",
                            "photoUrl", v.getPhotoUrl() != null ? v.getPhotoUrl() : "",
                            "latitude", v.getLatitude() != null ? v.getLatitude() : 0.0,
                            "longitude", v.getLongitude() != null ? v.getLongitude() : 0.0
                    );
                }).toList()
        );

        model.addAttribute("venuesJson", venuesJson);
        model.addAttribute("categories", VenueCategory.values());
        return "venues/map";
    }

    @GetMapping("/venues/suggest")
    public String suggestPage() {
        return "venues/suggest";
    }

    @PostMapping("/venues/suggest")
    public String submitSuggestion(@RequestParam String name,
                                   @RequestParam String description,
                                   @RequestParam String category,
                                   @RequestParam(required = false) String photoUrl,
                                   @RequestParam(required = false) MultipartFile photoFile,
                                   @RequestParam Double latitude,
                                   @RequestParam Double longitude,
                                   Authentication auth) {
        User user = getCurrentUser(auth);
        suggestionService.submit(name, description, category,
                photoUrl, photoFile, latitude, longitude, user, fileUploadService);
        return "redirect:/map?suggested";
    }

    @PostMapping("/wishlist/add")
    public String addToWishlist(@RequestParam Long venueId, Authentication auth) {
        User user = getCurrentUser(auth);
        wishlistService.addToWishlist(user, venueId);
        return "redirect:/map";
    }

    @PostMapping("/wishlist/remove")
    public String removeFromWishlist(@RequestParam Long venueId, Authentication auth) {
        User user = getCurrentUser(auth);
        wishlistService.removeFromWishlist(user, venueId);
        return "redirect:/wishlist";
    }

    @GetMapping("/wishlist")
    public String wishlistPage(Authentication auth, Model model) {
        User user = getCurrentUser(auth);
        model.addAttribute("wishlist", wishlistService.getWishlistForUser(user));
        return "venues/wishlist";
    }
}