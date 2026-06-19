package com.heartsync.service;

import com.heartsync.exception.ResourceNotFoundException;
import com.heartsync.model.Venue;
import com.heartsync.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }

    public List<Venue> getByCategory(String category) {
        return venueRepository.findByCategory(category);
    }

    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    public Venue saveWithImage(String name, String description, String category,
                               String photoUrl, MultipartFile photoFile,
                               Double latitude, Double longitude,
                               FileUploadService fileUploadService) {

        String finalPhotoUrl = photoUrl;

        if (photoFile != null && !photoFile.isEmpty()) {
            finalPhotoUrl = fileUploadService.store(photoFile);
        }

        Venue venue = Venue.builder()
                .name(name)
                .description(description)
                .category(category)
                .photoUrl(finalPhotoUrl)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return venueRepository.save(venue);
    }

    public void delete(Long id) {
        venueRepository.deleteById(id);
    }
}