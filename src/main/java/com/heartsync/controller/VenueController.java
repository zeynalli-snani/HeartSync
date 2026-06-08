package com.heartsync.controller;

import com.heartsync.model.User;
import com.heartsync.service.UserService;
import com.heartsync.service.VenueService;
import com.heartsync.service.VenueSuggestionService;
import com.heartsync.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;
    private final VenueSuggestionService suggestionService;
    private final WishlistService wishlistService;
    private final UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @GetMapping("/map")
    public String mapPage(Model model) {
        model.addAttribute("venues", venueService.getAllVenues());
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
                                   @RequestParam String photoUrl,
                                   @RequestParam Double latitude,
                                   @RequestParam Double longitude,
                                   Authentication auth) {
        User user = getCurrentUser(auth);
        suggestionService.submit(name, description, category, photoUrl, latitude, longitude, user);
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