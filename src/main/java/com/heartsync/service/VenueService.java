package com.heartsync.service;

import com.heartsync.model.Venue;
import com.heartsync.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue getById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found: " + id));
    }

    public List<Venue> getByCategory(String category) {
        return venueRepository.findByCategory(category);
    }

    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    public void delete(Long id) {
        venueRepository.deleteById(id);
    }
}