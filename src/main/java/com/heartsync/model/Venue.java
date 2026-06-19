package com.heartsync.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "venues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private VenueCategory category;

    @Column(name = "photo_url")
    private String photoUrl;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "venue")
    private List<Stop> stops;

    @OneToMany(mappedBy = "venue")
    private List<Wishlist> wishlistEntries;
}