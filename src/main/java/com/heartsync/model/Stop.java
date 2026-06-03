package com.heartsync.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_slot")
    private LocalTime timeSlot;

    private Integer position;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
}