package com.heartsync.service;

import com.heartsync.exception.ResourceNotFoundException;
import com.heartsync.model.*;
import com.heartsync.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueSuggestionService {

    private final VenueSuggestionRepository suggestionRepository;
    private final VenueRepository venueRepository;

    public void submit(String name, String description, String category,
                       String photoUrl, Double latitude, Double longitude, User submittedBy) {

        VenueSuggestion suggestion = VenueSuggestion.builder()
                .name(name)
                .description(description)
                .category(category)
                .photoUrl(photoUrl)
                .latitude(latitude)
                .longitude(longitude)
                .submittedBy(submittedBy)
                .status(SuggestionStatus.PENDING)
                .build();

        suggestionRepository.save(suggestion);
    }

    public List<VenueSuggestion> getPending() {
        return suggestionRepository.findByStatus(SuggestionStatus.PENDING);
    }

    public List<VenueSuggestion> getAllSuggestions() {
        return suggestionRepository.findAll();
    }

    public VenueSuggestion getById(Long id) {
        return suggestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found"));
    }

    public List<VenueSuggestion> getByUser(User user) {
        return suggestionRepository.findBySubmittedBy(user);
    }

    // opening the suggestion to review and edit
    public VenueSuggestion update(Long id, String name, String description, String category,
                                  String photoUrl, Double latitude, Double longitude) {

        VenueSuggestion suggestion = getById(id);
        suggestion.setName(name);
        suggestion.setDescription(description);
        suggestion.setCategory(category);
        suggestion.setPhotoUrl(photoUrl);
        suggestion.setLatitude(latitude);
        suggestion.setLongitude(longitude);
        return suggestionRepository.save(suggestion);
    }

    // creating a real Venue from the suggestion
    public void approve(Long id) {
        VenueSuggestion suggestion = getById(id);

        Venue venue = Venue.builder()
                .name(suggestion.getName())
                .description(suggestion.getDescription())
                .category(suggestion.getCategory())
                .photoUrl(suggestion.getPhotoUrl())
                .latitude(suggestion.getLatitude())
                .longitude(suggestion.getLongitude())
                .build();

        venueRepository.save(venue);

        suggestion.setStatus(SuggestionStatus.APPROVED);
        suggestionRepository.save(suggestion);
    }

    // rejecting with an optional note
    public void reject(Long id, String adminNote) {
        VenueSuggestion suggestion = getById(id);
        suggestion.setStatus(SuggestionStatus.REJECTED);
        suggestion.setAdminNote(adminNote);
        suggestionRepository.save(suggestion);
    }
}