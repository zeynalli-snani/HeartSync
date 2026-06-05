package com.heartsync.repository;

import com.heartsync.model.SuggestionStatus;
import com.heartsync.model.VenueSuggestion;
import com.heartsync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VenueSuggestionRepository extends JpaRepository<VenueSuggestion, Long> {
    List<VenueSuggestion> findByStatus(SuggestionStatus status);
    List<VenueSuggestion> findBySubmittedBy(User user);
}