package com.heartsync.repository;

import com.heartsync.model.Wishlist;
import com.heartsync.model.User;
import com.heartsync.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndVenue(User user, Venue venue);
    boolean existsByUserAndVenue(User user, Venue venue);
}