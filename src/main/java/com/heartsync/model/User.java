package com.heartsync.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Plan> ownedPlans;

    @OneToMany(mappedBy = "partner")
    private List<Plan> sharedPlans;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlist;

    @OneToMany(mappedBy = "submittedBy", cascade = CascadeType.ALL)
    private List<VenueSuggestion> venueSuggestions;
}