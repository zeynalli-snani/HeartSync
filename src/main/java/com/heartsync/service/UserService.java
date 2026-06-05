package com.heartsync.service;

import com.heartsync.model.User;
import com.heartsync.model.VenueSuggestion;
import com.heartsync.repository.UserRepository;
import com.heartsync.repository.VenueSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VenueSuggestionRepository venueSuggestionRepository;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deactivateUser(Long id) {
        User user = getById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = getById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    public List<VenueSuggestion> getSuggestionsForUser(User user) {
        return venueSuggestionRepository.findBySubmittedBy(user);
    }
}