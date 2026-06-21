package com.heartsync.repository;

import com.heartsync.model.Venue;
import com.heartsync.model.VenueCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByCategory(VenueCategory category);
}