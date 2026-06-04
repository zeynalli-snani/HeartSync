package com.heartsync.service;

import com.heartsync.model.*;
import com.heartsync.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final VenueRepository venueRepository;

    public List<Wishlist> getWishlistForUser(User user) {
        return wishlistRepository.findByUser(user);
    }

    public void addToWishlist(User user, Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found: " + venueId));

        if (!wishlistRepository.existsByUserAndVenue(user, venue)) {
            Wishlist entry = Wishlist.builder()
                    .user(user)
                    .venue(venue)
                    .build();
            wishlistRepository.save(entry);
        }
    }

    public void removeFromWishlist(User user, Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found: " + venueId));

        wishlistRepository.findByUserAndVenue(user, venue)
                .ifPresent(wishlistRepository::delete);
    }
}